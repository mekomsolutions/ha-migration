package net.mekomsolutions.ha.migration;

import java.util.List;

import jakarta.jms.Connection;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.MessageProducer;
import jakarta.jms.Queue;
import jakarta.jms.Session;
import jakarta.jms.TextMessage;
import lombok.extern.slf4j.Slf4j;
import net.mekomsolutions.ha.migration.model.BaseEntity;
import tools.jackson.databind.json.JsonMapper;

@Slf4j
public class MessageUtils {
	
	private static final JsonMapper MAPPER = JsonMapper.builder().build();
	
	public static void sendBatch(ConnectionFactory cf, List<BaseEntity> items, String queueName) {
		if (log.isDebugEnabled()) {
			log.debug("Sending batch of {} items(s)", items.size());
		}
		
		try (Connection conn = cf.createConnection();
		        Session session = conn.createSession(true, Session.SESSION_TRANSACTED)) {
			Queue queue = session.createQueue(queueName);
			try (MessageProducer p = session.createProducer(queue)) {
				for (Object item : items) {
					TextMessage msg = session.createTextMessage(MAPPER.writeValueAsString(items));
					
					//#{body.metadata.setSourceIdentifier('{{db-sync.senderId}}')}
					//#{body.metadata.setDateSent(T(java.time.LocalDateTime).now())}
					//#{body.metadata.setOperation(getProperty('event').operation)}
					//#{body.metadata.setDbSyncVersion(T(org.openmrs.eip.dbsync.SyncConstants).VERSION)}
					p.send(msg);
				}
				
				session.commit();
				if (log.isDebugEnabled()) {
					log.info("Successfully sent a batch of {} item(s)", items.size());
				}
			}
		}
		catch (Exception e) {
			log.error("Failed to send batch of {} item(s)", items.size(), e);
			throw new RuntimeException(e);
		}
	}
	
}
