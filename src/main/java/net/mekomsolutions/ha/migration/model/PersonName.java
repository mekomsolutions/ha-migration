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
