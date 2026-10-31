package net.mekomsolutions.ha.migration;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import org.springframework.core.env.Environment;

import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ser.std.StdSerializer;

public class LocalDateTimeSerializer extends StdSerializer<LocalDateTime> {
	
	private static ZoneId zoneId;
	
	public LocalDateTimeSerializer() {
		super(LocalDateTime.class);
	}
	
	@Override
	public void serialize(LocalDateTime date, JsonGenerator gen, SerializationContext context) {
		if (zoneId == null) {
			zoneId = ZoneId.of(Context.getBean(Environment.class).getProperty(Constants.PROP_TIME_ZONE));
		}
		
		gen.writeString(date.atZone(zoneId).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
	}
	
}
