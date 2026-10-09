package net.mekomsolutions.ha.migration;

import org.springframework.context.annotation.Bean;

public class TestConfig {
	
	@Bean
	public Context Context() {
		return new Context();
	}
	
}
