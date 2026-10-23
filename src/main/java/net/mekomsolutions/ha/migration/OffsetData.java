package net.mekomsolutions.ha.migration;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents metadata related to an offset in a file, storing a reference to the row number and the
 * file's hash.
 */
public class OffsetData {
	
	@Getter
	@Setter
	private Integer rowNumber;
	
	@Getter
	@Setter
	private String fileHash;
	
	public OffsetData() {
		this(null, null);
	}
	
	public OffsetData(Integer rowNumber, String fileHash) {
		this.rowNumber = rowNumber;
		this.fileHash = fileHash;
	}
	
}
