package net.mekomsolutions.ha.migration.model;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

public abstract class BaseEntity {
	
	@Getter
	@Setter
	private String uuid;
	
	@Getter
	@Setter
	private String creator;
	
	@Getter
	@Setter
	private LocalDateTime dateCreated;
	
	public abstract String getUuidTextPrefix();
	
}
