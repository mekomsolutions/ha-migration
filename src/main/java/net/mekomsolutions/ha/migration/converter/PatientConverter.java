package net.mekomsolutions.ha.migration.converter;

import java.time.LocalDateTime;

import net.mekomsolutions.ha.migration.Constants;
import net.mekomsolutions.ha.migration.PatientData;
import net.mekomsolutions.ha.migration.model.Patient;

public class PatientConverter extends BaseConverter<PatientData, Patient> {
	
	@Override
	public Patient convert(PatientData data, LocalDateTime date) {
		Patient patient = new Patient();
		patient.setGender(data.getGender());
		patient.setBirthdate(data.getBirthdate());
		patient.setCreator(Constants.DAEMON_USER_UUID);
		patient.setDateCreated(date);
		patient.setPatientCreator(Constants.DAEMON_USER_UUID);
		patient.setPatientDateCreated(date);
		return patient;
	}
	
}
