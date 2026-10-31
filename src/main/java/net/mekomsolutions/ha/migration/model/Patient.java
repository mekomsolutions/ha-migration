package net.mekomsolutions.ha.migration.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

public class Patient extends BaseEntity {
	
	@Getter
	@Setter
	private String gender;
	
	@Getter
	@Setter
	private LocalDate birthdate;
	
	@Getter
	@Setter
	private String patientCreatorUuid;
	
	@Getter
	@Setter
	private LocalDateTime patientDateCreated;
	
	@Getter
	private final String allergyStatus = "Unknown";
	
	@Override
	public String getUuidTextPrefix() {
		return null;
	}
	
}
