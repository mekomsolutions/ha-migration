package net.mekomsolutions.ha.migration;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import jakarta.jms.Connection;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.MessageProducer;
import jakarta.jms.Queue;
import jakarta.jms.Session;
import jakarta.jms.TextMessage;
import lombok.extern.slf4j.Slf4j;
import net.mekomsolutions.ha.migration.model.BaseEntity;
import net.mekomsolutions.ha.migration.model.Patient;
import net.mekomsolutions.ha.migration.model.PatientIdentifier;
import net.mekomsolutions.ha.migration.model.PersonAddress;
import net.mekomsolutions.ha.migration.model.PersonAttribute;
import net.mekomsolutions.ha.migration.model.PersonName;
import tools.jackson.databind.json.JsonMapper;

@Slf4j
public class MessageUtils {
	
	private static final JsonMapper MAPPER = JsonMapper.builder().build();
	
	private static final Map<Class<? extends BaseEntity>, String> TYPE_TO_SYNC_CLASS_MAP = Map.of(Patient.class,
	    Constants.SYNC_CLASS_PATIENT, PatientIdentifier.class, Constants.SYNC_CLASS_ID, PersonAddress.class,
	    Constants.SYNC_CLASS_ADDRESS, PersonAttribute.class, Constants.SYNC_CLASS_ATTRIBUTE, PersonName.class,
	    Constants.SYNC_CLASS_NAME);
	
	public static void sendBatch(ConnectionFactory cf, List<BaseEntity> items, String senderId, String queueName) {
		if (log.isDebugEnabled()) {
			log.debug("Sending batch of {} items(s)", items.size());
		}
		
		try (Connection c = cf.createConnection(); Session s = c.createSession(true, Session.SESSION_TRANSACTED)) {
			Queue queue = s.createQueue(queueName);
			try (MessageProducer p = s.createProducer(queue)) {
				LocalDateTime dateSent = LocalDateTime.now();
				for (BaseEntity item : items) {
					final SyncMetadata md = SyncMetadata.builder().sourceIdentifier(senderId)
					        .operation(Constants.DB_SYNC_OPERATION).dateSent(dateSent)
					        .dbSyncVersion(Constants.DB_SYNC_VERSION).build();
					final SyncModel model = SyncModel.builder().model(item)
					        .tableToSyncModelClass(TYPE_TO_SYNC_CLASS_MAP.get(item.getClass())).metadata(md).build();
					TextMessage msg = s.createTextMessage(MAPPER.writeValueAsString(model));
					p.send(msg);
				}
				
				s.commit();
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
