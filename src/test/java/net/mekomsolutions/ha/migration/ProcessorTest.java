package net.mekomsolutions.ha.migration;

import static jakarta.jms.Session.AUTO_ACKNOWLEDGE;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import jakarta.jms.Connection;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.Message;
import jakarta.jms.MessageConsumer;
import jakarta.jms.Queue;
import jakarta.jms.Session;
import jakarta.jms.TextMessage;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

@Testcontainers
@SpringBootTest(classes = TestProcessorConfig.class)
public class ProcessorTest {
	
	private static final String QUEUE_NAME = "openmrs.sync.test";
	
	private static final String SOURCE_ID = "test-source-id";
	
	private static final String USER = "admin";
	
	private static final String PASSWORD = "admin";
	
	private static final int PORT = 61616;
	
	@TempDir
	static File tempDir;
	
	@Container
	public static GenericContainer<?> artemisContainer = new GenericContainer<>(
	        DockerImageName.parse("apache/activemq-artemis:2.37.0-alpine")).withEnv("ARTEMIS_USER", USER)
	                .withEnv("ARTEMIS_PASSWORD", "admin").withExposedPorts(PORT).waitingFor(Wait.forListeningPort());
	
	@DynamicPropertySource
	static void registerProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.artemis.broker-url",
		    () -> "tcp://" + artemisContainer.getHost() + ":" + artemisContainer.getMappedPort(PORT));
		registry.add("spring.artemis.user", () -> USER);
		registry.add("spring.artemis.password", () -> PASSWORD);
		registry.add("spring.artemis.mode", () -> "native");
		
		registry.add(Constants.PROP_DATA_FILE_DIR, () -> "src/test/resources");
		registry.add(Constants.PROP_OFFSET_FILE_DIR, () -> tempDir.getAbsolutePath());
		registry.add(Constants.ARG_ENTITY_TYPE, () -> "PATIENT");
		registry.add(Constants.ARG_FILE, () -> "patients.csv");
		registry.add(Constants.PROP_BATCH_SIZE, () -> 100);
		registry.add(Constants.PROP_SYNC_SOURCE_ID, () -> SOURCE_ID);
		registry.add(Constants.PROP_SYNC_QUEUE_NAME, () -> QUEUE_NAME);
		registry.add(Constants.PROP_TIME_ZONE, () -> "GMT+3");
		
		registry.add(Constants.PROP_ID_TYPE_UUID_ID, () -> "type-uuid-1");
		registry.add(Constants.PROP_ID_TYPE_UUID_GOVT, () -> "type-uuid-2");
		registry.add(Constants.PROP_ATTR_TYPE_UUID_ETHNICITY, () -> "attr-type-ethnicity");
		registry.add(Constants.PROP_ATTR_TYPE_UUID_MARITAL_STATUS, () -> "attr-type-marital-status");
		registry.add(Constants.PROP_ATTR_TYPE_UUID_PHONE, () -> "attr-type-phone");
		registry.add(Constants.PROP_ATTR_TYPE_UUID_BIRTH_PLACE, () -> "attr-type-birth-place");
		registry.add(Constants.PROP_ATTR_TYPE_UUID_SPOUSE, () -> "attr-type-spouse");
		registry.add(Constants.PROP_ATTR_TYPE_UUID_FATHER, () -> "attr-type-father");
		registry.add(Constants.PROP_ATTR_TYPE_UUID_MOTHER, () -> "attr-type-mother");
		registry.add(Constants.PROP_ATTR_TYPE_UUID_IGSS, () -> "attr-type-igss");
		registry.add(Constants.PROP_ATTR_TYPE_UUID_EMERGENCY_CONTACT, () -> "attr-type-emergency-contact");
		registry.add(Constants.PROP_ATTR_TYPE_UUID_EMERGENCY_PHONE, () -> "attr-type-emergency-phone");
		registry.add(Constants.PROP_ATTR_TYPE_UUID_OCCUPATION, () -> "attr-type-occupation");
	}
	
	@Autowired
	private Processor processor;
	
	@Autowired
	private ConnectionFactory connFactory;
	
	@Test
	public void process_shouldProcessCsvAndSendMessagesToActiveMQ() throws Exception {
		// Each of the 2 rows in patients.csv produces 16 itemized entities = 32 messages
		final int expectedMessageCount = 32;
		// Verify Artemis MQ is running
		assertTrue(artemisContainer.isRunning());
		
		processor.process();
		
		List<String> rawMessages = new ArrayList<>(expectedMessageCount);
		try (Connection c = connFactory.createConnection(); Session s = c.createSession(false, AUTO_ACKNOWLEDGE)) {
			c.start();
			Queue queue = s.createQueue(QUEUE_NAME);
			try (MessageConsumer consumer = s.createConsumer(queue)) {
				while (true) {
					Message message = consumer.receive(2000);
					if (message == null) {
						break;
					}
					if (message instanceof TextMessage textMessage) {
						rawMessages.add(textMessage.getText());
					}
				}
			}
		}
		
		assertEquals(expectedMessageCount, rawMessages.size());
		
		JsonMapper mapper = JsonMapper.builder().build();
		List<JsonNode> jsonNodes = new ArrayList<>();
		for (String rawJson : rawMessages) {
			JsonNode node = mapper.readTree(rawJson);
			jsonNodes.add(node);
			
			// Verify common metadata structure
			assertNotNull(node.get("tableToSyncModelClass"));
			assertTrue(node.get("tableToSyncModelClass").asText().startsWith("org.openmrs.eip.dbsync.model."));
			
			JsonNode metadata = node.get("metadata");
			assertNotNull(metadata);
			assertEquals(SOURCE_ID, metadata.get("sourceIdentifier").asText());
			assertEquals(Constants.DB_SYNC_OPERATION, metadata.get("operation").asText());
			assertEquals(Constants.DB_SYNC_VERSION, metadata.get("dbSyncVersion").asText());
			assertNotNull(metadata.get("dateSent"));
			
			JsonNode model = node.get("model");
			assertNotNull(model);
			assertNotNull(model.get("uuid"));
			assertFalse(model.get("uuid").asText().isEmpty());
		}
		
		// Detailed verification of patient row 1 itemized entities
		final String pat1Uuid = java.util.UUID.nameUUIDFromBytes("12".getBytes(UTF_8)).toString();
		JsonNode patient1 = jsonNodes.get(0);
		assertEquals(Constants.SYNC_CLASS_PATIENT, patient1.get("tableToSyncModelClass").asText());
		assertEquals(pat1Uuid, patient1.get("model").get("uuid").asText());
		assertEquals("M", patient1.get("model").get("gender").asText());
		assertEquals("1985-08-15", patient1.get("model").get("birthdate").asText());
		assertEquals("Unknown", patient1.get("model").get("allergyStatus").asText());
		assertEquals(Constants.SYNC_CLASS_USER_LIGHT + "(" + Constants.DAEMON_USER_UUID + ")",
		    patient1.get("model").get("creatorUuid").asText());
		assertEquals(Constants.SYNC_CLASS_USER_LIGHT + "(" + Constants.DAEMON_USER_UUID + ")",
		    patient1.get("model").get("patientCreatorUuid").asText());
		assertNotNull(patient1.get("model").get("patientDateCreated").asText());
		
		JsonNode name1 = jsonNodes.get(1);
		assertEquals(Constants.SYNC_CLASS_NAME, name1.get("tableToSyncModelClass").asText());
		assertEquals("CARLOS", name1.get("model").get("givenName").asText());
		assertEquals("EDUARDO", name1.get("model").get("middleName").asText());
		assertEquals("LOPEZ PEREZ", name1.get("model").get("familyName").asText());
		assertTrue(name1.get("model").get("preferred").asBoolean());
		assertEquals(Constants.SYNC_CLASS_PATIENT_LIGHT + "(" + pat1Uuid + ")",
		    name1.get("model").get("personUuid").asText());
		assertNotNull(name1.get("model").get("dateCreated").asText());
		
		JsonNode patientId1 = jsonNodes.get(2);
		assertEquals(Constants.SYNC_CLASS_ID, patientId1.get("tableToSyncModelClass").asText());
		assertEquals("12", patientId1.get("model").get("identifier").asText());
		assertTrue(patientId1.get("model").get("preferred").asBoolean());
		assertEquals(Constants.SYNC_CLASS_ID_LIGHT + "(type-uuid-1)",
		    patientId1.get("model").get("patientIdentifierTypeUuid").asText());
		assertEquals(Constants.SYNC_CLASS_PATIENT_LIGHT + "(" + pat1Uuid + ")",
		    patientId1.get("model").get("patientUuid").asText());
		assertNotNull(patientId1.get("model").get("dateCreated").asText());
		
		JsonNode govtId1 = jsonNodes.get(3);
		assertEquals(Constants.SYNC_CLASS_ID, govtId1.get("tableToSyncModelClass").asText());
		assertEquals("2589631470101", govtId1.get("model").get("identifier").asText());
		assertFalse(govtId1.get("model").get("preferred").asBoolean());
		assertEquals(Constants.SYNC_CLASS_ID_LIGHT + "(type-uuid-2)",
		    govtId1.get("model").get("patientIdentifierTypeUuid").asText());
		assertEquals(Constants.SYNC_CLASS_PATIENT_LIGHT + "(" + pat1Uuid + ")",
		    govtId1.get("model").get("patientUuid").asText());
		assertNotNull(govtId1.get("model").get("dateCreated").asText());
		
		JsonNode address1 = jsonNodes.get(4);
		assertEquals(Constants.SYNC_CLASS_ADDRESS, address1.get("tableToSyncModelClass").asText());
		assertEquals("GUATEMALA", address1.get("model").get("address").get("country").asText());
		assertEquals("GUATEMALA", address1.get("model").get("address").get("stateProvince").asText());
		assertEquals("CIUDAD DE GUATEMALA", address1.get("model").get("address").get("cityVillage").asText());
		assertEquals("CALLE REAL 1-23", address1.get("model").get("address").get("address1").asText());
		assertEquals("ZONA 1", address1.get("model").get("address").get("address2").asText());
		assertTrue(address1.get("model").get("preferred").asBoolean());
		assertEquals(Constants.SYNC_CLASS_PATIENT_LIGHT + "(" + pat1Uuid + ")",
		    address1.get("model").get("personUuid").asText());
		assertNotNull(address1.get("model").get("dateCreated").asText());
		
		JsonNode ethnicity1 = jsonNodes.get(5);
		assertEquals(Constants.SYNC_CLASS_ATTRIBUTE, ethnicity1.get("tableToSyncModelClass").asText());
		assertEquals("LADINO", ethnicity1.get("model").get("value").asText());
		assertEquals(Constants.SYNC_CLASS_ATTRIB_TYPE_LIGHT + "(attr-type-ethnicity)",
		    ethnicity1.get("model").get("personAttributeTypeUuid").asText());
		assertEquals(Constants.SYNC_CLASS_PATIENT_LIGHT + "(" + pat1Uuid + ")",
		    ethnicity1.get("model").get("personUuid").asText());
		assertNotNull(ethnicity1.get("model").get("dateCreated").asText());
		
		// Verify patient row 2 entities
		final String pat2Uuid = java.util.UUID.nameUUIDFromBytes("85".getBytes(UTF_8)).toString();
		JsonNode patient2 = jsonNodes.get(16);
		assertEquals(Constants.SYNC_CLASS_PATIENT, patient2.get("tableToSyncModelClass").asText());
		assertEquals(pat2Uuid, patient2.get("model").get("uuid").asText());
		assertEquals("F", patient2.get("model").get("gender").asText());
		assertEquals("1992-12-03", patient2.get("model").get("birthdate").asText());
		assertEquals("Unknown", patient2.get("model").get("allergyStatus").asText());
		assertNotNull(patient2.get("model").get("patientDateCreated").asText());
		
		JsonNode name2 = jsonNodes.get(17);
		assertEquals(Constants.SYNC_CLASS_NAME, name2.get("tableToSyncModelClass").asText());
		assertEquals("MARIA", name2.get("model").get("givenName").asText());
		assertEquals("", name2.get("model").get("middleName").asText());
		assertEquals("GOMEZ SANTOS", name2.get("model").get("familyName").asText());
		assertEquals(Constants.SYNC_CLASS_PATIENT_LIGHT + "(" + pat2Uuid + ")",
		    name2.get("model").get("personUuid").asText());
	}
	
}
