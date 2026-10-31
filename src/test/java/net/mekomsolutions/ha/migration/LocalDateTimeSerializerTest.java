package net.mekomsolutions.ha.migration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import tools.jackson.databind.json.JsonMapper;

public class LocalDateTimeSerializerTest extends BaseContextTest {
	
	private static final JsonMapper MAPPER = JsonMapper.builder().build();
	
	@Test
	public void serialize_shouldSerializeLocalDateTimeWithZoneOffset() {
		LocalDateTime date = LocalDateTime.of(2021, 6, 10, 10, 15, 30);
		SyncMetadata metadata = SyncMetadata.builder().dateSent(date).build();
		
		String json = MAPPER.writeValueAsString(metadata);
		
		assertEquals("{\"dateSent\":\"2021-06-10T10:15:30-05:00\"}", json);
	}
	
}
