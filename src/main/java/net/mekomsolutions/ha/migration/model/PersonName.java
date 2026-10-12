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
package net.mekomsolutions.ha.migration.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

public class PersonName extends BaseEntity {
	
	@Getter
	@Setter
	@JsonProperty("nombre")
	private String givenName;
	
	@Getter
	@Setter
	@JsonProperty("nombre_secundario")
	private String middleName;
	
	@Getter
	@Setter
	@JsonProperty("apellidos")
	private String familyName;
	
}
