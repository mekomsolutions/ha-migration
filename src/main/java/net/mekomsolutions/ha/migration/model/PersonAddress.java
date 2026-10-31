package net.mekomsolutions.ha.migration.model;

import lombok.Getter;
import lombok.Setter;

public class PersonAddress extends BaseEntity {
	
	@Getter
	@Setter
	private Address address;
	
	@Getter
	@Setter
	private String personUuid;
	
	@Getter
	private final boolean preferred = true;
	
	@Override
	public String getUuidTextPrefix() {
		return address.getUuidTextPrefix();
	}
	
}
