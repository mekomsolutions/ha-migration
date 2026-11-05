package net.mekomsolutions.ha.migration;

import static net.mekomsolutions.ha.migration.Constants.SYNC_CLASS_ATTRIB_TYPE_LIGHT;
import static net.mekomsolutions.ha.migration.Constants.SYNC_CLASS_USER_LIGHT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

import net.mekomsolutions.ha.migration.model.PersonAttribute;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.BeanProperty;

public class AttributeDeserializerTest extends BaseContextTest {
	
	@Test
	public void deserialize_shouldDeserializeTextWhenIsNumberIsFalse() {
		final String typeUuid = "some-type-uuid";
		AttributeDeserializer deserializer = new AttributeDeserializer(typeUuid, false);
		JsonParser parser = mock(JsonParser.class);
		when(parser.getText()).thenReturn("non-numeric-value");
		
		PersonAttribute attribute = deserializer.deserialize(parser, null);
		
		assertNotNull(attribute);
		assertEquals("non-numeric-value", attribute.getValue());
		assertEquals(typeUuid, attribute.getAttributeTypeUuid());
		assertEquals(SYNC_CLASS_ATTRIB_TYPE_LIGHT + "(" + typeUuid + ")", attribute.getPersonAttributeTypeUuid());
		assertEquals(SYNC_CLASS_USER_LIGHT + "(" + Constants.DAEMON_USER_UUID + ")", attribute.getCreatorUuid());
	}
	
	@Test
	public void deserialize_shouldDeserializeAValidNumberWhenIsNumberIsTrue() {
		final String typeUuid = "some-type-uuid";
		final String phoneNo = "1112223333";
		AttributeDeserializer deserializer = new AttributeDeserializer(typeUuid, true);
		JsonParser parser = mock(JsonParser.class);
		when(parser.getText()).thenReturn(phoneNo);
		
		PersonAttribute attribute = deserializer.deserialize(parser, null);
		
		assertNotNull(attribute);
		assertEquals(phoneNo, attribute.getValue());
		assertEquals(typeUuid, attribute.getAttributeTypeUuid());
	}
	
	@Test
	public void deserialize_shouldReturnNullWhenIsNumberIsTrueAndValueIsInvalidNumber() {
		final String typeUuid = "some-type-uuid";
		final String invalidNo = "invalid-number";
		AttributeDeserializer deserializer = new AttributeDeserializer(typeUuid, true);
		JsonParser parser = mock(JsonParser.class);
		when(parser.getText()).thenReturn(invalidNo);
		
		PersonAttribute attribute = deserializer.deserialize(parser, null);
		
		assertNull(attribute);
	}
	
	@Test
	public void createContextual_shouldConfigureDeserializerFromAnnotation() {
		AttributeDeserializer deserializer = new AttributeDeserializer();
		BeanProperty property = mock(BeanProperty.class);
		Attribute annotation = mock(Attribute.class);
		when(annotation.typePropertyName()).thenReturn(Constants.PROP_ID_TYPE_UUID_ID);
		when(annotation.isNumber()).thenReturn(true);
		when(property.getAnnotation(Attribute.class)).thenReturn(annotation);
		
		AttributeDeserializer contextual = (AttributeDeserializer) deserializer.createContextual(null, property);
		
		JsonParser parser = mock(JsonParser.class);
		when(parser.getText()).thenReturn("invalid");
		assertNull(contextual.deserialize(parser, null));
		
		when(parser.getText()).thenReturn("123");
		PersonAttribute attribute = contextual.deserialize(parser, null);
		assertEquals("123", attribute.getValue());
		assertEquals("type-uuid-1", attribute.getAttributeTypeUuid());
	}
	
}
