package net.mekomsolutions.ha.migration.config;

import org.springframework.context.annotation.Bean;

import net.mekomsolutions.ha.migration.CsvParser;

public class AppConfig {
	
	@Bean
	public CsvParser csvParser() {
		return new CsvParser();
	}
	
}
