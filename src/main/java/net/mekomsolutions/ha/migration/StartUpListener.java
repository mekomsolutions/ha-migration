package net.mekomsolutions.ha.migration;

import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StartUpListener {
	
	@Value("${" + Constants.PROP_DATA_FILE_DIR + "}")
	private String directory;
	
	@Value("${" + Constants.ARG_ENTITY_TYPE + "}")
	private EntityType type;
	
	@Value("${" + Constants.ARG_FILE + "}")
	private String file;
	
	private CsvParser parser;
	
	public StartUpListener(CsvParser parser) {
		this.parser = parser;
	}
	
	@EventListener(classes = { ContextRefreshedEvent.class })
	public void contextRefreshed() {
		Path path = FileUtils.get(directory, file);
		log.info("Migrating {} records in file {}", type.getClazz().getSimpleName(), path);
		parser.parse(path, type.getClazz());
	}
	
}
