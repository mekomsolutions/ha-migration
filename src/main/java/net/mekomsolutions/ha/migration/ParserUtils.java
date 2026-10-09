package net.mekomsolutions.ha.migration;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import tools.jackson.databind.json.JsonMapper;

@Slf4j
public class ParserUtils {
	
	private static Path offsetPath;
	
	private static final JsonMapper MAPPER = JsonMapper.builder().build();
	
	/**
	 * Gets the file used to store the offset details.
	 *
	 * @return File object
	 */
	private static Path getOffsetPath() {
		if (offsetPath == null) {
			offsetPath = FileUtils.get(Constants.OFFSET_FILE);
			log.info("Off set file -> {}", offsetPath);
		}
		
		return offsetPath;
	}
	
	/**
	 * Gets the number of the last processed row.
	 *
	 * @return row number
	 */
	public static Integer getLastProcessedRowNumber() {
		log.info("Loading row number that was last processed");
		
		try {
			Path path = getOffsetPath();
			Integer rowNumber = null;
			if (FileUtils.exists(path)) {
				byte[] data = FileUtils.readAllBytes(path);
				if (data.length == 0) {
					log.info("No offset data found");
					return null;
				}
				
				//TODO Verify hash
				Map<String, Object> map = MAPPER.readValue(data, Map.class);
				rowNumber = (Integer) map.get("rowNumber");
			}
			
			log.info("Found last processed row number: {}", rowNumber);
			
			return rowNumber;
		}
		catch (IOException e) {
			throw new RuntimeException("Failed to get the number of the row that was last processed", e);
		}
	}
	
	/**
	 * Saves the offset data
	 *
	 * @param data offset details
	 */
	public static void saveOffset(Map<String, Object> data) {
		final String json = MAPPER.writeValueAsString(data);
		log.info("Saving offset details as: {}", json);
		
		try {
			FileUtils.write(getOffsetPath(), json.getBytes(StandardCharsets.UTF_8));
			
			log.info("Successfully saved the offset file");
		}
		catch (IOException e) {
			log.error("Failed to save the offset file", e);
		}
	}
	
	/**
	 * Deletes the offset file.
	 */
	public static void deleteOffset() {
		log.info("Deleting the offset file");
		
		Path path = getOffsetPath();
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
