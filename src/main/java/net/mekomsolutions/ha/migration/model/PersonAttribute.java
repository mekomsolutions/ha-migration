package net.mekomsolutions.ha.migration.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

public class PersonAttribute extends BaseEntity {
	
	@Getter
	@Setter
	@JsonIgnore
	private String attributeTypeUuid;
	
	@Getter
	@Setter
	private String personAttributeTypeUuid;
	
	@Getter
	@Setter
	private String value;
	
	@Getter
	@Setter
	private String personUuid;
	
	@Override
	public String getUuidTextPrefix() {
		return attributeTypeUuid + value;
	}
	
}
