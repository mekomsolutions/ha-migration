package net.mekomsolutions.ha.migration;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

import org.springframework.util.DigestUtils;

import lombok.extern.slf4j.Slf4j;
import tools.jackson.databind.json.JsonMapper;

@Slf4j
public class OffsetUtils {
	
	private static Path offsetPath;
	
	private static final JsonMapper MAPPER = JsonMapper.builder().build();
	
	public static void init(Path csvFilePath, String type) throws IOException {
		byte[] csvFileData = FileUtils.readAllBytes(csvFilePath);
		String hash = DigestUtils.md5DigestAsHex(csvFileData);
		OffsetData offsetData = loadOffsetData(type);
		if (offsetData == null) {
			offsetData = new OffsetData();
			offsetData.setFileHash(hash);
		} else {
			if (!hash.equals(offsetData.getFileHash())) {
				throw new IllegalStateException(
				        "The contents of the import file have changed while it was still being processed");
			}
		}
		
	}
	
	/**
	 * Gets the file used to store the offset details.
	 *
	 * @return File object
	 */
	private static Path getOffsetPath(String fileName) {
		if (offsetPath == null) {
			offsetPath = FileUtils.get(fileName + Constants.OFFSET_FILE_SUFFIX);
			log.info("Off set file -> {}", offsetPath);
		}
		
		return offsetPath;
	}
	
	/**
	 * Saves the offset data
	 *
	 * @param type the logical type of the data
	 */
	public static OffsetData loadOffsetData(String type) {
		try {
			Path path = getOffsetPath(type);
			if (FileUtils.exists(path)) {
				byte[] data = FileUtils.readAllBytes(path);
				if (data.length == 0) {
					log.info("No offset data found");
				} else {
					return MAPPER.readValue(data, OffsetData.class);
				}
			}
			
			return null;
		}
		catch (IOException e) {
			throw new RuntimeException("Failed to load offset data for " + type, e);
		}
	}
	
	/**
	 * Gets the number of the last processed row.
	 *
	 * @return row number
	 */
	public static Integer getLastProcessedRowNumber(String type) {
		log.info("Loading row number that was last processed");
		
		OffsetData data = loadOffsetData(type);
		Integer rowNumber = data.getRowNumber();
		if (rowNumber == null) {
			log.info("No last processed row number was found");
		} else {
			log.info("Found last processed row number: {}", rowNumber);
		}
		
		return rowNumber;
	}
	
	/**
	 * Saves the offset data
	 *
	 * @param rowNumber row number
	 */
	public static void saveOffset(Integer rowNumber, String type) {
		OffsetData data = loadOffsetData(type);
		data.setRowNumber(rowNumber);
		final String json = MAPPER.writeValueAsString(data);
		log.info("Saving offset details as: {}", json);
		
		try {
			FileUtils.write(getOffsetPath(type), json.getBytes(StandardCharsets.UTF_8));
			
			log.info("Successfully saved the offset file");
		}
		catch (IOException e) {
			log.error("Failed to save the offset file", e);
		}
	}
	
	/**
	 * Deletes the offset file.
	 */
	public static void deleteOffset(String type) {
		log.info("Deleting the offset file");
		
		Path path = getOffsetPath(type);
		if (!FileUtils.exists(path)) {
			log.info("No offset file found to delete");
			return;
		}
		
		try {
			FileUtils.deleteIfExists(path);
			
			log.info("Successfully deleted the offset file");
		}
		catch (IOException e) {
			log.error("Failed to delete the offset file", e);
		}
	}
	
}
