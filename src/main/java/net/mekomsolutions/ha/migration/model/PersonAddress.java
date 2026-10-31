package net.mekomsolutions.ha.migration.model;

import com.fasterxml.jackson.annotation.JsonSetter;

import lombok.Getter;
import lombok.Setter;

public class PersonAddress extends BaseEntity {
	
	@Getter
	@Setter
	@JsonSetter("direccion")
	private String address1;
	
	@Getter
	@Setter
	@JsonSetter("canton")
	private String address2;
	
	@Getter
	@Setter
	@JsonSetter("municipio")
	private String cityVillage;
	
	@Getter
	@Setter
	@JsonSetter("departamento")
	private String stateProvince;
	
	@Getter
	@Setter
	@JsonSetter("pais")
	private String country;
	
	@Getter
	@Setter
	private String personUuid;
	
	@Override
	public String getUuidTextPrefix() {
		return address1 + address2 + cityVillage + stateProvince + country;
	}
	
}
