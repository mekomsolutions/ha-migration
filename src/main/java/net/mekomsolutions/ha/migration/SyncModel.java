package net.mekomsolutions.ha.migration;

import lombok.Builder;
import lombok.Getter;
import net.mekomsolutions.ha.migration.model.BaseEntity;

@Builder
public class SyncModel {
	
	@Getter
	private String tableToSyncModelClass;
	
	@Getter
	private BaseEntity model;
	
	@Getter
	private SyncMetadata metadata;
	
	@Override
	public String toString() {
		return "{tableToSyncModelClass=" + tableToSyncModelClass + ", identifier=" + model.getUuid() + ", metadata="
		        + metadata + "}";
	}
	
}
