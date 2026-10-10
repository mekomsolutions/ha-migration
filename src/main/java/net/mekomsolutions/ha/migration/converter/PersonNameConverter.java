package net.mekomsolutions.ha.migration.converter;

import java.time.LocalDateTime;

import net.mekomsolutions.ha.migration.data.PatientData;
import net.mekomsolutions.ha.migration.model.PersonName;

/**
 * Converts a {@link PersonName} object from a {@link PatientData} object.
 */
public class PersonNameConverter extends BaseConverter<PatientData, PersonName> {
	
	@Override
	public PersonName convert(PatientData data, LocalDateTime date) {
		PersonName name = new PersonName();
		name.setGivenName(data.getGivenName());
		name.setMiddleName(data.getMiddleName());
		name.setFamilyName(data.getFamilyName());
		return name;
	}
	
}
