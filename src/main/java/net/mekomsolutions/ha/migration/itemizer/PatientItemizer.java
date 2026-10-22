package net.mekomsolutions.ha.migration.itemizer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import net.mekomsolutions.ha.migration.Constants;
import net.mekomsolutions.ha.migration.EntityType;
import net.mekomsolutions.ha.migration.data.PatientData;
import net.mekomsolutions.ha.migration.model.BaseEntity;
import net.mekomsolutions.ha.migration.model.Patient;

/**
 * Itemizes a {@link PatientData} row.
 */
@Component
public class PatientItemizer extends BaseRowItemizer<PatientData> {
	
	@Override
	public List<BaseEntity> itemize(PatientData data) {
		LocalDateTime date = LocalDateTime.now();
		List<BaseEntity> entities = new ArrayList<>(EntityType.PATIENT.getItemCountPerRow());
		Patient patient = new Patient();
		patient.setGender(data.getGender());
		patient.setBirthdate(data.getBirthdate());
		patient.setCreator(Constants.SYNC_CLASS_USER_LIGHT + "(" + Constants.DAEMON_USER_UUID + ")");
		patient.setDateCreated(date);
		patient.setPatientCreator(Constants.SYNC_CLASS_USER_LIGHT + "(" + Constants.DAEMON_USER_UUID + ")");
		patient.setPatientDateCreated(date);
		entities.add(patient);
		entities.add(data.getName());
		entities.add(data.getPatientId());
		entities.add(data.getGovtId());
		entities.add(data.getAddress());
		entities.add(data.getEthnicity());
		entities.add(data.getMaritalStatus());
		entities.add(data.getPhone());
		entities.add(data.getBirthPlace());
		entities.add(data.getSpouse());
		entities.add(data.getFather());
		entities.add(data.getMother());
		entities.add(data.getIgss());
		entities.add(data.getEmergencyContact());
		entities.add(data.getEmergencyPhone());
		entities.add(data.getOccupation());
		return entities;
	}
	
}
