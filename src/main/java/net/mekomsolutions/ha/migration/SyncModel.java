package net.mekomsolutions.ha.migration;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import net.mekomsolutions.ha.migration.model.BaseEntity;

@Builder
public class SyncModel {
	
	@Getter
	@Setter
	private Class<? extends BaseEntity> tableToSyncModelClass;
	
	@Getter
	@Setter
	private BaseEntity model;
	
	@Getter
	@Setter
	private SyncMetadata metadata;
	
	@Override
	public String toString() {
		return "{tableToSyncModelClass=" + tableToSyncModelClass + ", identifier=" + model.getUuid() + ", metadata="
		        + metadata + "}";
	}
	
}
