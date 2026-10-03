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

public class PersonAddress extends BaseData {
	
	@Getter
	@Setter
	@JsonProperty("direccion")
	private String address1;
	
	@Getter
	@Setter
	@JsonProperty("canton")
	private String address2;
	
	@Getter
	@Setter
	@JsonProperty("municipio")
	private String cityVillage;
	
	@Getter
	@Setter
	@JsonProperty("departamento")
	private String stateProvince;
	
	@Getter
	@Setter
	@JsonProperty("pais")
	private String country;
	
}
