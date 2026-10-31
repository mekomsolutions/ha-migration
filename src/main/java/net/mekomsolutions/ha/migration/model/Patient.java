package net.mekomsolutions.ha.migration.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import net.mekomsolutions.ha.migration.LocalDateTimeSerializer;
import tools.jackson.databind.annotation.JsonSerialize;

public class Patient extends BaseEntity {
	
	@Getter
	@Setter
	private String gender;
	
	@Getter
	@Setter
	private LocalDate birthdate;
	
	@Getter
	@Setter
	private String patientCreator;
	
	@Getter
	@Setter
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	private LocalDateTime patientDateCreated;
	
	@Override
	public String getUuidTextPrefix() {
		return null;
	}
	
}
