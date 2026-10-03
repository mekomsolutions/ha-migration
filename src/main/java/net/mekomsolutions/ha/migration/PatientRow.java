/*
 * Copyright (C) Amiyul LLC - All Rights Reserved
 *
 * This source code is protected under international copyright law. All rights
 * reserved and protected by the copyright holder.
 *
 * This file is confidential and only available to authorized individuals with the
 * permission of the copyright holder. If you encounter this file and do not have
 * permission, please contact the copyright holder and delete this file.
 */
package net.mekomsolutions.ha.migration;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

import lombok.Getter;
import lombok.Setter;
import net.mekomsolutions.ha.migration.model.GovtIdentifier;
import net.mekomsolutions.ha.migration.model.PatientIdentifier;
import net.mekomsolutions.ha.migration.model.PersonAddress;
import net.mekomsolutions.ha.migration.model.PersonAttribute;

public class PatientRow {
	
	@Getter
	@Setter
	@JsonUnwrapped
	private PatientIdentifier patientId;
	
	@Getter
	@Setter
	@JsonUnwrapped
	private GovtIdentifier govtId;
	
	@Getter
	@Setter
	@JsonProperty("nombre")
	private String firstName;
	
	@Getter
	@Setter
	@JsonProperty("nombre_secundario")
	private String middleName;
	
	@Getter
	@Setter
	@JsonProperty("apellidos")
	private String familyName;
	
	@Getter
	@Setter
	@JsonProperty("sexo")
	private String gender;
	
	@Getter
	@Setter
	@JsonProperty("fechanacimiento")
	private LocalDate birthDate;
	
	@Getter
	@Setter
	@JsonProperty("etnia")
	private PersonAttribute ethnicity;
	
	@Getter
	@Setter
	@JsonProperty("estadocivil")
	private PersonAttribute maritalStatus;
	
	@Getter
	@Setter
	@JsonProperty("telefono")
	private PersonAttribute phone;
	
	@Getter
	@Setter
	@JsonProperty("lugarnacimiento")
	private PersonAttribute birthPlace;
	
	@Getter
	@Setter
	@JsonProperty("nombreconyugue")
	private PersonAttribute spouse;
	
	@Getter
	@Setter
	@JsonProperty("nombrepadre")
	private PersonAttribute father;
	
	@Getter
	@Setter
	@JsonProperty("nombremadre")
	private PersonAttribute mother;
	
	@Getter
	@Setter
	@JsonProperty("igss")
	private PersonAttribute igss;
	
	@Getter
	@Setter
	@JsonProperty("emergenciaavisara")
	private PersonAttribute emergencyContact;
	
	@Getter
	@Setter
	@JsonProperty("telefonoavisar")
	private PersonAttribute emergencyPhone;
	
	@Getter
	@Setter
	@JsonProperty("ocupacion")
	private PersonAttribute occupation;
	
	@Getter
	@Setter
	@JsonUnwrapped
	private PersonAddress address;
	
}
