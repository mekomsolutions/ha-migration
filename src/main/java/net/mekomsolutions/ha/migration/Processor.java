package net.mekomsolutions.ha.migration;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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
	
	private CsvParser parser;
	
	public Processor(CsvParser parser) {
		this.parser = parser;
	}
	
	public void process() {
		MappingIterator<?> it = parseFile();
		BaseRowItemizer itemizer = ProcessorUtils.getItemizer(type.getClazz());
		int count = 0;
		while (it.hasNext()) {
			sendBatch(null, type.getItemCountPerRow(), itemizer);
			count++;
		}
		
		log.info("Processed {} items", count);
	}
	
	protected MappingIterator<?> parseFile() {
		Path path = FileUtils.get(directory, file);
		log.info("Processing {} records in file {}", type.getClazz().getSimpleName(), path);
		return parser.parse(path, type.getClazz());
	}
	
	protected void sendBatch(List<Object> rows, int entityCountPerRow, BaseRowItemizer itemizer) {
		List<BaseEntity> entities = new ArrayList<>(rows.size() * entityCountPerRow);
		for (Object row : rows) {
			List<BaseEntity> items = itemizer.itemize(row);
			entities.addAll(items);
		}
		
		//TODO Send entities to activeQM
	}
	
}
