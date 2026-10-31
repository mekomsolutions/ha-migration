package net.mekomsolutions.ha.migration.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

public class PatientIdentifier extends BaseEntity {
	
	@Getter
	@Setter
	private String identifier;
	
	@Getter
	@Setter
	@JsonIgnore
	private String identifierTypeUuid;
	
	@Getter
	@Setter
	private String patientIdentifierTypeUuid;
	
	@Getter
	@Setter
	private String patientUuid;
	
	@Getter
	@Setter
	private boolean preferred;
	
	@Override
	public String getUuidTextPrefix() {
		return identifierTypeUuid + identifier;
	}
	
}
