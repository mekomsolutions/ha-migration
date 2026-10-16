package net.mekomsolutions.ha.migration;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import net.mekomsolutions.ha.migration.generator.BaseGenerator;
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
		BaseGenerator generator = ProcessorUtils.getGenerator(type.getClazz());
		int count = 0;
		while (it.hasNext()) {
			BaseEntity e = convert(it.next(), LocalDateTime.now(), generator);
			sendBatch(List.of(e));
			count++;
		}
		
		log.info("Processed {} items", count);
	}
	
	protected MappingIterator<?> parseFile() {
		Path path = FileUtils.get(directory, file);
		log.info("Processing {} records in file {}", type.getClazz().getSimpleName(), path);
		return parser.parse(path, type.getClazz());
	}
	
	protected BaseEntity convert(Object object, LocalDateTime date, BaseGenerator generator) {
		return generator.generate(object, date);
	}
	
	protected void sendBatch(List<BaseEntity> entities) {
	}
	
}
