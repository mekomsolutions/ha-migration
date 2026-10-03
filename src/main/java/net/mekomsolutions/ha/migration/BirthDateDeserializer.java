package net.mekomsolutions.ha.migration;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.util.StringUtils;

import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.deser.std.StdDeserializer;

public class BirthDateDeserializer extends StdDeserializer<LocalDate> {
	
	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	
	public BirthDateDeserializer() {
		super(LocalDate.class);
	}
	
	@Override
	public LocalDate deserialize(JsonParser p, DeserializationContext ctxt) {
		final String birthDate = p.getText();
		if (StringUtils.hasText(birthDate)) {
			return LocalDate.parse(birthDate, FORMATTER);
		}
		
		return null;
	}
	
}
