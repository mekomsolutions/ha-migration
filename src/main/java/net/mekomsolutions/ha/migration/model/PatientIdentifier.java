package net.mekomsolutions.ha.migration.model;

import lombok.Getter;
import lombok.Setter;

public class PatientIdentifier extends BaseEntity {
	
	@Getter
	@Setter
	private String identifier;
	
	@Getter
	@Setter
	private String identifierType;
	
	@Getter
	@Setter
	private String patient;
	
	@Getter
	@Setter
	private boolean preferred;
	
}
