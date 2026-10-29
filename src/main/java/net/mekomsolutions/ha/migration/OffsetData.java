package net.mekomsolutions.ha.migration;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents metadata related to an offset in a file, storing a reference to the row index and the
 * file's hash.
 */
public class OffsetData {
	
	@Getter
	@Setter
	private Integer rowIndex;
	
	@Getter
	@Setter
	private String fileHash;
	
	public OffsetData() {
		this(null, null);
	}
	
	public OffsetData(Integer rowIndex, String fileHash) {
		this.rowIndex = rowIndex;
		this.fileHash = fileHash;
	}
	
}
