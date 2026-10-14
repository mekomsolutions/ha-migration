package net.mekomsolutions.ha.migration;

import lombok.Getter;
import net.mekomsolutions.ha.migration.data.PatientData;

public enum EntityType {
	
	PATIENT(PatientData.class);
	
	@Getter
	private Class<?> clazz;
	
	EntityType(Class<?> clazz) {
		this.clazz = clazz;
	}
	
}
