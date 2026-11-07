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
		verifyPatient(jsonNodes.get(0), pat1Uuid, "M", "1985-08-15");
		verifyName(jsonNodes.get(1), pat1Uuid, "CARLOS", "EDUARDO", "LOPEZ PEREZ");
		verifyIdentifier(jsonNodes.get(2), pat1Uuid, "type-uuid-1", "12", true);
		verifyIdentifier(jsonNodes.get(3), pat1Uuid, "type-uuid-2", "2589631470101", false);
		verifyAddress(jsonNodes.get(4), pat1Uuid, "GUATEMALA", "GUATEMALA", "CIUDAD DE GUATEMALA", "CALLE REAL 1-23",
		    "ZONA 1");
		verifyAttribute(jsonNodes.get(5), pat1Uuid, "attr-type-ethnicity", "LADINO");
		verifyAttribute(jsonNodes.get(6), pat1Uuid, "attr-type-marital-status", "SOLTERO");
		verifyAttribute(jsonNodes.get(7), pat1Uuid, "attr-type-phone", "55512345");
		verifyAttribute(jsonNodes.get(8), pat1Uuid, "attr-type-birth-place", "GUATEMALA");
		verifyAttribute(jsonNodes.get(9), pat1Uuid, "attr-type-spouse", "PEDRO MORALES");
		verifyAttribute(jsonNodes.get(10), pat1Uuid, "attr-type-father", "JUAN LOPEZ");
		verifyAttribute(jsonNodes.get(11), pat1Uuid, "attr-type-mother", "MARIA PEREZ");
		verifyAttribute(jsonNodes.get(12), pat1Uuid, "attr-type-igss", "Si");
		verifyAttribute(jsonNodes.get(13), pat1Uuid, "attr-type-emergency-contact", "JUAN LOPEZ");
		verifyAttribute(jsonNodes.get(14), pat1Uuid, "attr-type-occupation", "COMERCIANTE");
		verifyAttribute(jsonNodes.get(15), pat1Uuid, "attr-type-emergency-phone", "55598765");
		
		// Verify patient row 2 entities
		final String pat2Uuid = java.util.UUID.nameUUIDFromBytes("85".getBytes(UTF_8)).toString();
		verifyPatient(jsonNodes.get(16), pat2Uuid, "F", "1992-12-03");
		verifyName(jsonNodes.get(17), pat2Uuid, "MARIA", "", "GOMEZ SANTOS");
		verifyIdentifier(jsonNodes.get(18), pat2Uuid, "type-uuid-1", "85", true);
		verifyIdentifier(jsonNodes.get(19), pat2Uuid, "type-uuid-2", "1478523690202", false);
		verifyAddress(jsonNodes.get(20), pat2Uuid, "GUATEMALA", "QUETZALTENANGO", "QUETZALTENANGO", "", "CANTON CENTRO");
		verifyAttribute(jsonNodes.get(21), pat2Uuid, "attr-type-ethnicity", "MAYA");
		verifyAttribute(jsonNodes.get(22), pat2Uuid, "attr-type-marital-status", "CASADO");
		verifyAttribute(jsonNodes.get(23), pat2Uuid, "attr-type-phone", "44456789");
		verifyAttribute(jsonNodes.get(24), pat2Uuid, "attr-type-birth-place", "QUETZALTENANGO");
		verifyAttribute(jsonNodes.get(25), pat2Uuid, "attr-type-spouse", "");
		verifyAttribute(jsonNodes.get(26), pat2Uuid, "attr-type-father", "JOSE GOMEZ");
		verifyAttribute(jsonNodes.get(27), pat2Uuid, "attr-type-mother", "ANA SANTOS");
		verifyAttribute(jsonNodes.get(28), pat2Uuid, "attr-type-igss", "No");
		verifyAttribute(jsonNodes.get(29), pat2Uuid, "attr-type-emergency-contact", "PEDRO MORALES");
		verifyAttribute(jsonNodes.get(30), pat2Uuid, "attr-type-occupation", "MAESTRA");
		verifyAttribute(jsonNodes.get(31), pat2Uuid, "attr-type-emergency-phone", "SE IGNORA");
	}
	
	private void verifyPatient(JsonNode node, String uuid, String gender, String birthdate) {
		assertEquals(Constants.SYNC_CLASS_PATIENT, node.get("tableToSyncModelClass").asText());
		assertEquals(uuid, node.get("model").get("uuid").asText());
		assertEquals(gender, node.get("model").get("gender").asText());
		assertEquals(birthdate, node.get("model").get("birthdate").asText());
		assertEquals("Unknown", node.get("model").get("allergyStatus").asText());
		assertEquals(Constants.SYNC_CLASS_USER_LIGHT + "(" + Constants.DAEMON_USER_UUID + ")",
		    node.get("model").get("creatorUuid").asText());
		assertEquals(Constants.SYNC_CLASS_USER_LIGHT + "(" + Constants.DAEMON_USER_UUID + ")",
		    node.get("model").get("patientCreatorUuid").asText());
		assertNotNull(node.get("model").get("patientDateCreated").asText());
	}
	
	private void verifyName(JsonNode node, String personUuid, String givenName, String middleName, String familyName) {
		assertEquals(Constants.SYNC_CLASS_NAME, node.get("tableToSyncModelClass").asText());
		assertEquals(givenName, node.get("model").get("givenName").asText());
		assertEquals(middleName, node.get("model").get("middleName").asText());
		assertEquals(familyName, node.get("model").get("familyName").asText());
		assertTrue(node.get("model").get("preferred").asBoolean());
		assertEquals(Constants.SYNC_CLASS_PATIENT_LIGHT + "(" + personUuid + ")",
		    node.get("model").get("personUuid").asText());
		assertNotNull(node.get("model").get("dateCreated").asText());
	}
	
	private void verifyIdentifier(JsonNode node, String patientUuid, String idTypeUuid, String identifier,
	                              boolean preferred) {
		assertEquals(Constants.SYNC_CLASS_ID, node.get("tableToSyncModelClass").asText());
		assertEquals(identifier, node.get("model").get("identifier").asText());
		assertEquals(preferred, node.get("model").get("preferred").asBoolean());
		assertEquals(Constants.SYNC_CLASS_ID_LIGHT + "(" + idTypeUuid + ")",
		    node.get("model").get("patientIdentifierTypeUuid").asText());
		assertEquals(Constants.SYNC_CLASS_PATIENT_LIGHT + "(" + patientUuid + ")",
		    node.get("model").get("patientUuid").asText());
		assertNotNull(node.get("model").get("dateCreated").asText());
	}
	
	private void verifyAddress(JsonNode node, String personUuid, String country, String stateProvince, String cityVillage,
	                           String address1, String address2) {
		assertEquals(Constants.SYNC_CLASS_ADDRESS, node.get("tableToSyncModelClass").asText());
		assertEquals(country, node.get("model").get("address").get("country").asText());
		assertEquals(stateProvince, node.get("model").get("address").get("stateProvince").asText());
		assertEquals(cityVillage, node.get("model").get("address").get("cityVillage").asText());
		assertEquals(address1, node.get("model").get("address").get("address1").asText());
		assertEquals(address2, node.get("model").get("address").get("address2").asText());
		assertTrue(node.get("model").get("preferred").asBoolean());
		assertEquals(Constants.SYNC_CLASS_PATIENT_LIGHT + "(" + personUuid + ")",
		    node.get("model").get("personUuid").asText());
		assertNotNull(node.get("model").get("dateCreated").asText());
	}
	
	private void verifyAttribute(JsonNode node, String personUuid, String attributeTypeUuid, String value) {
		assertEquals(Constants.SYNC_CLASS_ATTRIBUTE, node.get("tableToSyncModelClass").asText());
		assertEquals(value, node.get("model").get("value").asText());
		assertEquals(Constants.SYNC_CLASS_ATTRIB_TYPE_LIGHT + "(" + attributeTypeUuid + ")",
		    node.get("model").get("personAttributeTypeUuid").asText());
		assertEquals(Constants.SYNC_CLASS_PATIENT_LIGHT + "(" + personUuid + ")",
		    node.get("model").get("personUuid").asText());
		assertNotNull(node.get("model").get("dateCreated").asText());
	}
}
