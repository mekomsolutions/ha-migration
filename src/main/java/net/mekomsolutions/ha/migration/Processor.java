package net.mekomsolutions.ha.migration;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.jms.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import net.mekomsolutions.ha.migration.itemizer.BaseRowItemizer;
import net.mekomsolutions.ha.migration.model.BaseEntity;
import tools.jackson.databind.MappingIterator;

@Component
@Slf4j
public class Processor {
	
	@Value("${" + Constants.PROP_DATA_FILE_DIR + "}")
	private String directory;
	
	@Value("${" + Constants.ARG_ENTITY_TYPE + "}")
	private EntityType type;
	
	@Value("${" + Constants.ARG_FILE + "}")
	private String file;
	
	@Value("${" + Constants.PROP_BATCH_SIZE + "}")
	private int batchSize;
	
	@Value("${" + Constants.PROP_SYNC_SOURCE_ID + "}")
	private String sourceId;
	
	@Value("${" + Constants.PROP_SYNC_QUEUE_NAME + "}")
	private String queueName;
	
	private CsvParser parser;
	
	private ConnectionFactory connectionFactory;
	
	public Processor(CsvParser parser, ConnectionFactory connectionFactory) {
		this.parser = parser;
		this.connectionFactory = connectionFactory;
	}
	
	public void process() {
		Path path = FileUtils.get(directory, file);
		try {
			OffsetUtils.init(path, type);
		}
		catch (IOException e) {
			throw new RuntimeException("Failed to initialize offset", e);
		}
		
		Integer lastProcessedRow = OffsetUtils.getLastProcessedRowNumber(type);
		final MappingIterator<?> it = parseFile();
		final BaseRowItemizer itemizer = ProcessorUtils.getItemizer(type.getClazz());
		int rowNumber = 0;
		int processedCount = 0;
		final List<Object> batch = new ArrayList<>(batchSize);
		while (it.hasNext()) {
			final Object row = it.next();
			rowNumber++;
			if (lastProcessedRow != null && rowNumber <= lastProcessedRow) {
				log.debug("Skipping already processed row number {}", rowNumber);
				continue;
			}
			
			batch.add(row);
			if (batch.size() == batchSize) {
				sendBatch(batch, rowNumber, type.getItemCountPerRow(), itemizer);
				batch.clear();
			}
			
			processedCount++;
		}
		
		if (!batch.isEmpty()) {
			sendBatch(batch, rowNumber, type.getItemCountPerRow(), itemizer);
		}
		
		log.info("Processed {} rows", processedCount);
		OffsetUtils.deleteOffset(type);
	}
	
	protected MappingIterator<?> parseFile() {
		Path path = FileUtils.get(directory, file);
		log.info("Processing {} records in file {}", type.getClazz().getSimpleName(), path);
		return parser.parse(path, type.getClazz());
	}
	
	protected void sendBatch(List<Object> rows, int rowNumber, int entityCountPerRow, BaseRowItemizer itemizer) {
		List<BaseEntity> entities = new ArrayList<>(rows.size() * entityCountPerRow);
		for (Object row : rows) {
			List<BaseEntity> items = itemizer.itemize(row);
			entities.addAll(items);
		}
		
		MessageUtils.sendBatch(connectionFactory, entities, sourceId, queueName);
		OffsetUtils.saveOffset(rowNumber, type);
	}
	
}
