package net.mekomsolutions.ha.migration;

import lombok.Getter;
import net.mekomsolutions.ha.migration.data.PatientData;

public enum EntityType {
	
	PATIENT(PatientData.class, 16);
	
	@Getter
	private Class<?> clazz;
	
	@Getter
	private int itemCountPerRow;
	
	EntityType(Class<?> clazz, int itemCountPerRow) {
		this.clazz = clazz;
		this.itemCountPerRow = itemCountPerRow;
	}
	
}
