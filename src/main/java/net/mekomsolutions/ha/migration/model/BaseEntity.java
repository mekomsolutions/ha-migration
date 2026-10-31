package net.mekomsolutions.ha.migration.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import net.mekomsolutions.ha.migration.LocalDateTimeSerializer;
import tools.jackson.databind.annotation.JsonSerialize;

public abstract class BaseEntity {
	
	@Getter
	@Setter
	private String uuid;
	
	@Getter
	@Setter
	private String creator;
	
	@Getter
	@Setter
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	private LocalDateTime dateCreated;
	
	@JsonIgnore
	public abstract String getUuidTextPrefix();
	
}
