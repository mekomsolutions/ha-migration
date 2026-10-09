package net.mekomsolutions.ha.migration.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

public class PersonAddress extends BaseEntity {
	
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
