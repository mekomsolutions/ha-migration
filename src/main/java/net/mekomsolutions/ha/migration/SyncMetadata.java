package net.mekomsolutions.ha.migration;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

/**
 * Encapsulates descriptive data about a sync payload i.e. the unique ID of the site sending the.
 * payload and the date it was sent.
 */
public class SyncMetadata {
	
	@Getter
	@Setter
	private String sourceIdentifier;
	
	@Getter
	@Setter
	private String operation;
	
	@Getter
	@Setter
	private LocalDateTime dateSent;
	
	@Getter
	@Setter
	private String dbSyncVersion;
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + "{sourceIdentifier=" + sourceIdentifier + ", operation=" + operation
		        + ", dateSent=" + dateSent + ", dbSyncVersion=" + dbSyncVersion + "}";
	}
	
}
