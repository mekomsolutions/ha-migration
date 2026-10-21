package net.mekomsolutions.ha.migration;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

/**
 * Encapsulates descriptive data about a sync payload i.e. the unique ID of the site sending the.
 * payload and the date it was sent.
 */
@Builder
public class SyncMetadata {
	
	@Getter
	private String sourceIdentifier;
	
	@Getter
	private String operation;
	
	@Getter
	private LocalDateTime dateSent;
	
	@Getter
	private String dbSyncVersion;
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + "{sourceIdentifier=" + sourceIdentifier + ", operation=" + operation
		        + ", dateSent=" + dateSent + ", dbSyncVersion=" + dbSyncVersion + "}";
	}
	
}
