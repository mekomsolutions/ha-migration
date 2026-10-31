package net.mekomsolutions.ha.migration.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
	
	@JsonIgnore
	public abstract String getUuidTextPrefix();
	
}
