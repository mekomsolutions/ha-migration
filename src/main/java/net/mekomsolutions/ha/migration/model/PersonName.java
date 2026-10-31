package net.mekomsolutions.ha.migration.model;

import com.fasterxml.jackson.annotation.JsonSetter;

import lombok.Getter;
import lombok.Setter;

public class PersonName extends BaseEntity {
	
	@Getter
	@Setter
	@JsonSetter("nombre")
	private String givenName;
	
	@Getter
	@Setter
	@JsonSetter("nombre_secundario")
	private String middleName;
	
	@Getter
	@Setter
	@JsonSetter("apellidos")
	private String familyName;
	
	@Getter
	private final boolean preferred = true;
	
	@Getter
	@Setter
	private String personUuid;
	
	@Override
	public String getUuidTextPrefix() {
		return givenName + middleName + familyName;
	}
	
}
