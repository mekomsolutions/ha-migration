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
	@Identifier(typePropertyName = Constants.PROP_ID_TYPE_UUID_ID, preferred = true)
	private PatientIdentifier patientId;
	
	@Getter
	@Setter
	@JsonProperty("dpi")
	@JsonDeserialize(using = IdentifierDeserializer.class)
	@Identifier(typePropertyName = Constants.PROP_ID_TYPE_UUID_GOVT)
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
	@Attribute(typePropertyName = Constants.PROP_ATTR_TYPE_UUID_ETHNICITY)
	private PersonAttribute ethnicity;
	
	@Getter
	@Setter
	@JsonProperty("estadocivil")
	@JsonDeserialize(using = AttributeDeserializer.class)
	@Attribute(typePropertyName = Constants.PROP_ATTR_TYPE_UUID_MARITAL_STATUS)
	private PersonAttribute maritalStatus;
	
	@Getter
	@Setter
	@JsonProperty("telefono")
	@JsonDeserialize(using = AttributeDeserializer.class)
	@Attribute(typePropertyName = Constants.PROP_ATTR_TYPE_UUID_PHONE)
	private PersonAttribute phone;
	
	@Getter
	@Setter
	@JsonProperty("lugarnacimiento")
	@JsonDeserialize(using = AttributeDeserializer.class)
	@Attribute(typePropertyName = Constants.PROP_ATTR_TYPE_UUID_BIRTH_PLACE)
	private PersonAttribute birthPlace;
	
	@Getter
	@Setter
	@JsonProperty("nombreconyugue")
	@JsonDeserialize(using = AttributeDeserializer.class)
	@Attribute(typePropertyName = Constants.PROP_ATTR_TYPE_UUID_SPOUSE)
	private PersonAttribute spouse;
	
	@Getter
	@Setter
	@JsonProperty("nombrepadre")
	@JsonDeserialize(using = AttributeDeserializer.class)
	@Attribute(typePropertyName = Constants.PROP_ATTR_TYPE_UUID_FATHER)
	private PersonAttribute father;
	
	@Getter
	@Setter
	@JsonProperty("nombremadre")
	@JsonDeserialize(using = AttributeDeserializer.class)
	@Attribute(typePropertyName = Constants.PROP_ATTR_TYPE_UUID_MOTHER)
	private PersonAttribute mother;
	
	@Getter
	@Setter
	@JsonProperty("igss")
	@JsonDeserialize(using = AttributeDeserializer.class)
	@Attribute(typePropertyName = Constants.PROP_ATTR_TYPE_UUID_IGSS)
	private PersonAttribute igss;
	
	@Getter
	@Setter
	@JsonProperty("emergenciaavisara")
	@JsonDeserialize(using = AttributeDeserializer.class)
	@Attribute(typePropertyName = Constants.PROP_ATTR_TYPE_UUID_EMERGENCY_CONTACT)
	private PersonAttribute emergencyContact;
	
	@Getter
	@Setter
	@JsonProperty("telefonoavisar")
	@JsonDeserialize(using = AttributeDeserializer.class)
	@Attribute(typePropertyName = Constants.PROP_ATTR_TYPE_UUID_EMERGENCY_PHONE)
	private PersonAttribute emergencyPhone;
	
	@Getter
	@Setter
	@JsonProperty("ocupacion")
	@JsonDeserialize(using = AttributeDeserializer.class)
	@Attribute(typePropertyName = Constants.PROP_ATTR_TYPE_UUID_OCCUPATION)
	private PersonAttribute occupation;
	
	@Getter
	@Setter
	@JsonUnwrapped
	private PersonAddress address;
	
}
