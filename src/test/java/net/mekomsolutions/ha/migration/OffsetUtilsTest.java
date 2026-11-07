package net.mekomsolutions.ha.migration;

import static java.nio.charset.StandardCharsets.UTF_8;
import static net.mekomsolutions.ha.migration.Constants.OFFSET_FILE_SUFFIX;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.util.DigestUtils;

public class OffsetUtilsTest extends BaseContextTest {
	
	@TempDir
	static File tempDir;
	
	@DynamicPropertySource
	static void registerProperties(DynamicPropertyRegistry registry) {
		registry.add(Constants.PROP_OFFSET_FILE_DIR, () -> tempDir.getAbsolutePath());
	}
	
	private Path testCsvPath;
	
	@BeforeEach
	public void setup() throws Exception {
		testCsvPath = Files.createTempFile("test-patients", ".csv");
		Files.write(testCsvPath, "header1,header2\nval1,val2\n".getBytes(UTF_8));
	}
	
	@Test
	public void shouldManageOffsetLifecycleInConfiguredDirectory() throws Exception {
		EntityType type = EntityType.PATIENT;
		Path expectedOffsetFile = tempDir.toPath().resolve(type.name() + OFFSET_FILE_SUFFIX);
		byte[] csvFileData = FileUtils.readAllBytes(testCsvPath);
		
		// 1. Init should create the offset file in the configured directory
		OffsetUtils.init(testCsvPath, type);
		assertTrue(Files.exists(expectedOffsetFile));
		
		// 2. Load offset data should return initial state
		OffsetData offsetData = OffsetUtils.loadOffsetData(type);
		assertNotNull(offsetData);
		assertEquals(-1, offsetData.getRowIndex());
		assertEquals(DigestUtils.md5DigestAsHex(csvFileData), offsetData.getFileHash());
		assertEquals(-1, OffsetUtils.getLastProcessedRowIndex(type));
		
		// 3. Save row index should update the file
		OffsetUtils.saveRowIndex(42, type);
		assertEquals(42, OffsetUtils.getLastProcessedRowIndex(type));
		offsetData = OffsetUtils.loadOffsetData(type);
		assertEquals(42, offsetData.getRowIndex());
		
		// 4. Delete offset should remove the file
		OffsetUtils.deleteOffset(type);
		assertFalse(Files.exists(expectedOffsetFile));
		assertNull(OffsetUtils.loadOffsetData(type));
		assertNull(OffsetUtils.getLastProcessedRowIndex(type));
	}
	
	@Test
	public void init_shouldThrowExceptionWhenFileContentChanges() throws Exception {
		EntityType type = EntityType.PATIENT;
		final Path expectedOffsetFile = tempDir.toPath().resolve(type.name() + OFFSET_FILE_SUFFIX);
		try {
			OffsetUtils.init(testCsvPath, type);
			assertTrue(Files.exists(expectedOffsetFile));
			
			// Modify CSV content
			Files.write(testCsvPath, "header1,header2\nchanged1,changed2\n".getBytes(UTF_8));
			
			IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
				OffsetUtils.init(testCsvPath, type);
			});
			assertEquals("The contents of the import file have changed while it was still being processed",
			    exception.getMessage());
		}
		finally {
			OffsetUtils.deleteOffset(type);
		}
	}
	
}
