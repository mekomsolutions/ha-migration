package net.mekomsolutions.ha.migration.model;

import lombok.Getter;
import lombok.Setter;

public class PersonAttribute extends BaseData {
	
	@Getter
	@Setter
	private String personAttributeType;
	
	@Getter
	@Setter
	private String value;
	
	@Getter
	@Setter
	private String person;
	
}
