package net.mekomsolutions.ha.migration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import com.jayway.jsonpath.JsonPath;

public class LocalDateTimeSerializerTest extends BaseContextTest {
	
	@Test
	public void serialize_shouldSerializeLocalDateTimeWithZoneOffset() {
		LocalDateTime date = LocalDateTime.of(2021, 6, 10, 10, 15, 30);
		SyncMetadata metadata = SyncMetadata.builder().dateSent(date).build();
		
		String json = MessageUtils.getMapper().writeValueAsString(metadata);
		
		assertEquals("2021-06-10T10:15:30+03:00", JsonPath.parse(json).read("$.dateSent"));
	}
	
}
