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
	
	/**
	 * Initializes the processing of the specified CSV file based on the provided entity type. This
	 * method validates the integrity of the file by checking its hash against an existing offset record
	 * to ensure that the file has not been modified during processing. If no offset data exists for the
	 * type, a new offset record is created and associated with the file's hash.
	 *
	 * @param csvFilePath the path to the CSV file being processed
	 * @param type the type of entity that the CSV file data pertains to
	 * @throws IOException
	 */
	public static void init(Path csvFilePath, EntityType type) throws IOException {
		byte[] csvFileData = FileUtils.readAllBytes(csvFilePath);
		String hash = DigestUtils.md5DigestAsHex(csvFileData);
		OffsetData offsetData = loadOffsetData(type);
		if (offsetData == null) {
			offsetData = new OffsetData();
			offsetData.setFileHash(hash);
			offsetData.setRowIndex(-1);
			saveOffsetData(offsetData, type);
		} else if (!hash.equals(offsetData.getFileHash())) {
			throw new IllegalStateException(
			        "The contents of the import file have changed while it was still being processed");
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
			log.info("{} offset file -> {}", fileName, offsetPath);
		}
		
		return offsetPath;
	}
	
	/**
	 * Saves the offset data
	 *
	 * @param type the type of the data
	 */
	public static OffsetData loadOffsetData(EntityType type) {
		try {
			Path path = getOffsetPath(type.name());
			if (FileUtils.exists(path)) {
				byte[] data = FileUtils.readAllBytes(path);
				if (data.length == 0) {
					log.info("No {} offset data found", type);
				} else {
					return MAPPER.readValue(data, OffsetData.class);
				}
			}
			
			return null;
		}
		catch (IOException e) {
			throw new RuntimeException("Failed to load " + type + " offset data", e);
		}
	}
	
	/**
	 * Gets the index of the last processed row.
	 *
	 * @param type the type of the data
	 * @return row index
	 */
	public static Integer getLastProcessedRowIndex(EntityType type) {
		log.info("Loading {} row index that was last processed", type);
		
		OffsetData data = loadOffsetData(type);
		Integer rowIndex = data != null ? data.getRowIndex() : null;
		if (rowIndex == null) {
			log.info("No last processed {} row index was found", type);
		} else if (rowIndex > -1) {
			log.info("Found last processed {} row index: {}", type, rowIndex);
		}
		
		return rowIndex;
	}
	
	/**
	 * Saves the offset row index.
	 *
	 * @param rowIndex row index
	 * @param type the type of the data
	 */
	public static void saveRowIndex(Integer rowIndex, EntityType type) {
		OffsetData data = loadOffsetData(type);
		data.setRowIndex(rowIndex);
		saveOffsetData(data, type);
	}
	
	/**
	 * Saves the offset data to disk
	 *
	 * @param data the offset data object
	 * @param type the type of the data
	 */
	private static void saveOffsetData(OffsetData data, EntityType type) {
		final String json = MAPPER.writeValueAsString(data);
		log.info("Saving {} offset data as: {}", type, json);
		
		try {
			FileUtils.write(getOffsetPath(type.name()), json.getBytes(StandardCharsets.UTF_8));
			
			log.info("Successfully saved {} offset data", type);
		}
		catch (IOException e) {
			log.error("Failed to save " + type + " offset data", e);
		}
	}
	
	/**
	 * Deletes the offset file.
	 *
	 * @param type the type of the data
	 */
	public static void deleteOffset(EntityType type) {
		log.info("Deleting the offset file for {}", type);
		
		Path path = getOffsetPath(type.name());
		if (!FileUtils.exists(path)) {
			log.info("No {} offset file found to delete", type);
			return;
		}
		
		try {
			FileUtils.deleteIfExists(path);
			
			log.info("Successfully deleted {} offset file", type);
		}
		catch (IOException e) {
			log.error("Failed to delete " + type + " offset file", e);
		}
	}
	
}
