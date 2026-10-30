package net.mekomsolutions.ha.migration.itemizer;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import net.mekomsolutions.ha.migration.Constants;
import net.mekomsolutions.ha.migration.EntityType;
import net.mekomsolutions.ha.migration.Processor;
import net.mekomsolutions.ha.migration.data.PatientData;
import net.mekomsolutions.ha.migration.model.BaseEntity;
import net.mekomsolutions.ha.migration.model.Patient;
import net.mekomsolutions.ha.migration.model.PersonAddress;
import net.mekomsolutions.ha.migration.model.PersonName;

/**
 * Itemizes a {@link PatientData} row.
 */
@Component
public class PatientItemizer extends BaseRowItemizer<PatientData> {
	
	@Override
	public List<BaseEntity> itemize(PatientData data) {
		final String creator = Constants.SYNC_CLASS_USER_LIGHT + "(" + Constants.DAEMON_USER_UUID + ")";
		List<BaseEntity> entities = new ArrayList<>(EntityType.PATIENT.getItemCountPerRow());
		Patient patient = new Patient();
		patient.setGender(data.getGender());
		patient.setBirthdate(data.getBirthdate());
		patient.setCreator(creator);
		patient.setDateCreated(Processor.date);
		patient.setPatientCreator(creator);
		patient.setPatientDateCreated(Processor.date);
		entities.add(patient);
		PersonName name = data.getName();
		name.setCreator(creator);
		name.setDateCreated(Processor.date);
		entities.add(name);
		entities.add(data.getPatientId());
		entities.add(data.getGovtId());
		PersonAddress address = data.getAddress();
		address.setCreator(creator);
		address.setDateCreated(Processor.date);
		entities.add(address);
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
