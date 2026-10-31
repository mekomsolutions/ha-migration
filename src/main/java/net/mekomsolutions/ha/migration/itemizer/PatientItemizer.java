package net.mekomsolutions.ha.migration.itemizer;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

import net.mekomsolutions.ha.migration.Constants;
import net.mekomsolutions.ha.migration.EntityType;
import net.mekomsolutions.ha.migration.Processor;
import net.mekomsolutions.ha.migration.data.PatientData;
import net.mekomsolutions.ha.migration.model.BaseEntity;
import net.mekomsolutions.ha.migration.model.Patient;
import net.mekomsolutions.ha.migration.model.PatientIdentifier;
import net.mekomsolutions.ha.migration.model.PersonAddress;
import net.mekomsolutions.ha.migration.model.PersonAttribute;
import net.mekomsolutions.ha.migration.model.PersonName;

/**
 * Itemizes a {@link PatientData} row.
 */
@Component
public class PatientItemizer extends BaseRowItemizer<PatientData> {
	
	@Override
	public List<BaseEntity> itemize(PatientData data) {
		final String creator = Constants.SYNC_CLASS_USER_LIGHT + "(" + Constants.DAEMON_USER_UUID + ")";
		final String patUuid = UUID.nameUUIDFromBytes(data.getPatientId().getIdentifier().getBytes(UTF_8)).toString();
		final String patientLight = Constants.SYNC_CLASS_PATIENT_LIGHT + "(" + patUuid + ")";
		List<BaseEntity> entities = new ArrayList<>(EntityType.PATIENT.getItemCountPerRow());
		Patient patient = new Patient();
		patient.setUuid(patUuid);
		patient.setGender(data.getGender());
		patient.setBirthdate(data.getBirthdate());
		patient.setCreator(creator);
		patient.setDateCreated(Processor.date);
		patient.setPatientCreator(creator);
		patient.setPatientDateCreated(Processor.date);
		entities.add(patient);
		PersonName name = data.getName();
		name.setUuid(UUID.nameUUIDFromBytes((name.getUuidTextPrefix() + patUuid).getBytes(UTF_8)).toString());
		name.setPerson(patientLight);
		name.setCreator(creator);
		name.setDateCreated(Processor.date);
		entities.add(name);
		PatientIdentifier patientId = data.getPatientId();
		patientId.setUuid(UUID.nameUUIDFromBytes((patientId.getUuidTextPrefix() + patUuid).getBytes(UTF_8)).toString());
		patientId.setPatient(patientLight);
		entities.add(patientId);
		PatientIdentifier govtId = data.getGovtId();
		govtId.setUuid(UUID.nameUUIDFromBytes((govtId.getUuidTextPrefix() + patUuid).getBytes(UTF_8)).toString());
		govtId.setPatient(patientLight);
		entities.add(govtId);
		PersonAddress address = data.getAddress();
		address.setUuid(UUID.nameUUIDFromBytes((address.getUuidTextPrefix() + patUuid).getBytes(UTF_8)).toString());
		address.setPerson(patientLight);
		address.setCreator(creator);
		address.setDateCreated(Processor.date);
		entities.add(address);
		addAttribute(data.getEthnicity(), entities, patUuid);
		addAttribute(data.getMaritalStatus(), entities, patUuid);
		addAttribute(data.getPhone(), entities, patUuid);
		addAttribute(data.getBirthPlace(), entities, patUuid);
		addAttribute(data.getSpouse(), entities, patUuid);
		addAttribute(data.getFather(), entities, patUuid);
		addAttribute(data.getMother(), entities, patUuid);
		addAttribute(data.getIgss(), entities, patUuid);
		addAttribute(data.getEmergencyContact(), entities, patUuid);
		addAttribute(data.getEmergencyPhone(), entities, patUuid);
		addAttribute(data.getOccupation(), entities, patUuid);
		return entities;
	}
	
	private void addAttribute(PersonAttribute attribute, List<BaseEntity> entities, String patUuid) {
		attribute.setUuid(UUID.nameUUIDFromBytes((attribute.getUuidTextPrefix() + patUuid).getBytes(UTF_8)).toString());
		entities.add(attribute);
	}
	
}
