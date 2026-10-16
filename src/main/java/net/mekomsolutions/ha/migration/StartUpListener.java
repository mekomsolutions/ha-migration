package net.mekomsolutions.ha.migration;

import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

public class StartUpListener {
	
	private Processor processor;
	
	public StartUpListener(Processor processor) {
		this.processor = processor;
	}
	
	@EventListener(classes = { ContextRefreshedEvent.class })
	public void contextRefreshed() {
		processor.process();
	}
	
}
