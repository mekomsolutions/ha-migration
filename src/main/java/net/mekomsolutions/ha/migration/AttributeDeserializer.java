package net.mekomsolutions.ha.migration;

import net.mekomsolutions.ha.migration.model.PersonAttribute;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.BeanProperty;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.ValueDeserializer;
import tools.jackson.databind.deser.std.StdDeserializer;

/**
 * Custom deserializer for converting string identifiers into {@link PersonAttribute} objects. The
 * deserialization behavior can be customized through the {@link Identifier} annotation on the
 * field. The deserialization process supports contextual initialization based on the field
 * properties annotated with {@link Identifier}.
 */
public class AttributeDeserializer extends StdDeserializer<PersonAttribute> {
	
	private final String attributeType;
	
	public AttributeDeserializer() {
		this(null);
	}
	
	public AttributeDeserializer(String attributeType) {
		super(PersonAttribute.class);
		this.attributeType = attributeType;
	}
	
	@Override
	public ValueDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) {
		Attribute annotation = property.getAnnotation(Attribute.class);
		final String type = annotation.typePropertyName();
		return new AttributeDeserializer(type);
	}
	
	@Override
	public PersonAttribute deserialize(JsonParser p, DeserializationContext ctxt) {
		PersonAttribute attribute = new PersonAttribute();
		attribute.setValue(p.getText());
		attribute.setPersonAttributeType(attributeType);
		return attribute;
	}
	
}
