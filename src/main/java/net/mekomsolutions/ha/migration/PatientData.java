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
import net.mekomsolutions.ha.migration.model.PatientIdentifier;
import net.mekomsolutions.ha.migration.model.PersonAddress;
import net.mekomsolutions.ha.migration.model.PersonAttribute;
import tools.jackson.databind.annotation.JsonDeserialize;

public class PatientData {
	
	@Getter
	@Setter
	@JsonProperty("codigousuario")
	@JsonDeserialize(using = IdentifierDeserializer.class)
	@Identifier(typePropertyName = Constants.PROP_ID_TYPE_ID, preferred = true)
	private PatientIdentifier patientId;
	
	@Getter
	@Setter
	@JsonProperty("dpi")
	@JsonDeserialize(using = IdentifierDeserializer.class)
	@Identifier(typePropertyName = Constants.PROP_ID_TYPE_GOVT)
	private PatientIdentifier govtId;
	
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
	@JsonDeserialize(using = BirthDateDeserializer.class)
	private LocalDate birthDate;
	
	@Getter
	@Setter
	@JsonProperty("etnia")
	@JsonDeserialize(using = AttributeDeserializer.class)
	@Attribute(typePropertyName = "attribute.type.uuid.ethnicity")
	private PersonAttribute ethnicity;
	
	@Getter
	@Setter
	@JsonProperty("estadocivil")
	@JsonDeserialize(using = AttributeDeserializer.class)
	@Attribute(typePropertyName = "attribute.type.uuid.marital.status")
	private PersonAttribute maritalStatus;
	
	@Getter
	@Setter
	@JsonProperty("telefono")
	@JsonDeserialize(using = AttributeDeserializer.class)
	@Attribute(typePropertyName = "attribute.type.uuid.phone")
	private PersonAttribute phone;
	
	@Getter
	@Setter
	@JsonProperty("lugarnacimiento")
	@JsonDeserialize(using = AttributeDeserializer.class)
	@Attribute(typePropertyName = "attribute.type.uuid.birth.place")
	private PersonAttribute birthPlace;
	
	@Getter
	@Setter
	@JsonProperty("nombreconyugue")
	@JsonDeserialize(using = AttributeDeserializer.class)
	@Attribute(typePropertyName = "attribute.type.uuid.spouse")
	private PersonAttribute spouse;
	
	@Getter
	@Setter
	@JsonProperty("nombrepadre")
	@JsonDeserialize(using = AttributeDeserializer.class)
	@Attribute(typePropertyName = "attribute.type.uuid.father")
	private PersonAttribute father;
	
	@Getter
	@Setter
	@JsonProperty("nombremadre")
	@JsonDeserialize(using = AttributeDeserializer.class)
	@Attribute(typePropertyName = "attribute.type.uuid.mother")
	private PersonAttribute mother;
	
	@Getter
	@Setter
	@JsonProperty("igss")
	@JsonDeserialize(using = AttributeDeserializer.class)
	@Attribute(typePropertyName = "attribute.type.uuid.igss")
	private PersonAttribute igss;
	
	@Getter
	@Setter
	@JsonProperty("emergenciaavisara")
	@JsonDeserialize(using = AttributeDeserializer.class)
	@Attribute(typePropertyName = "attribute.type.uuid.emergency.contact")
	private PersonAttribute emergencyContact;
	
	@Getter
	@Setter
	@JsonProperty("telefonoavisar")
	@JsonDeserialize(using = AttributeDeserializer.class)
	@Attribute(typePropertyName = "attribute.type.uuid.emergency.phone")
	private PersonAttribute emergencyPhone;
	
	@Getter
	@Setter
	@JsonProperty("ocupacion")
	@JsonDeserialize(using = AttributeDeserializer.class)
	@Attribute(typePropertyName = "attribute.type.uuid.occupation")
	private PersonAttribute occupation;
	
	@Getter
	@Setter
	@JsonUnwrapped
	private PersonAddress address;
	
}
