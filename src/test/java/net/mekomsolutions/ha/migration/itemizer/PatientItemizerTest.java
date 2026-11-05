package net.mekomsolutions.ha.migration.itemizer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import net.mekomsolutions.ha.migration.data.PatientData;
import net.mekomsolutions.ha.migration.model.Address;
import net.mekomsolutions.ha.migration.model.BaseEntity;
import net.mekomsolutions.ha.migration.model.PatientIdentifier;
import net.mekomsolutions.ha.migration.model.PersonAttribute;
import net.mekomsolutions.ha.migration.model.PersonName;

public class PatientItemizerTest {
	
	private PatientItemizer itemizer;
	
	@BeforeEach
	public void setup() {
		itemizer = new PatientItemizer();
	}
	
	private PatientData createPatientData(PersonAttribute emergencyPhone) {
		PatientData data = new PatientData();
		
		PatientIdentifier patientId = new PatientIdentifier();
		patientId.setIdentifier("PAT-123");
		data.setPatientId(patientId);
		
		PatientIdentifier govtId = new PatientIdentifier();
		govtId.setIdentifier("DPI-456");
		data.setGovtId(govtId);
		
		PersonName name = new PersonName();
		name.setGivenName("John");
		name.setFamilyName("Doe");
		data.setName(name);
		
		data.setGender("M");
		data.setBirthdate(LocalDate.of(1990, 1, 1));
		
		Address address = new Address();
		address.setAddress1("123 Main St");
		data.setAddress(address);
		
		data.setEthnicity(new PersonAttribute());
		data.setMaritalStatus(new PersonAttribute());
		data.setPhone(new PersonAttribute());
		data.setBirthPlace(new PersonAttribute());
		data.setSpouse(new PersonAttribute());
		data.setFather(new PersonAttribute());
		data.setMother(new PersonAttribute());
		data.setIgss(new PersonAttribute());
		data.setEmergencyContact(new PersonAttribute());
		data.setEmergencyPhone(emergencyPhone);
		data.setOccupation(new PersonAttribute());
		
		return data;
	}
	
	@Test
	public void itemize_shouldIncludeEmergencyPhoneWhenNotNull() {
		PersonAttribute emergencyPhone = new PersonAttribute();
		emergencyPhone.setValue("12345678");
		PatientData data = createPatientData(emergencyPhone);
		
		List<BaseEntity> entities = itemizer.itemize(data);
		
		assertEquals(16, entities.size());
		assertTrue(entities.contains(emergencyPhone));
	}
	
	@Test
	public void itemize_shouldSkipEmergencyPhoneWhenNull() {
		PatientData data = createPatientData(null);
		
		List<BaseEntity> entities = itemizer.itemize(data);
		
		assertEquals(15, entities.size());
		assertFalse(entities.stream().anyMatch(e -> e == null));
	}
	
}
