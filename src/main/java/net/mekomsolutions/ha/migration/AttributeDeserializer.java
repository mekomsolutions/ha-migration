package net.mekomsolutions.ha.migration;

import org.springframework.core.env.Environment;

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
	
	private static Environment env;
	
	private final String attributeTypeUuid;
	
	private final boolean isNumber;
	
	public AttributeDeserializer() {
		this(null, false);
	}
	
	public AttributeDeserializer(String attributeTypeUuid) {
		this(attributeTypeUuid, false);
	}
	
	public AttributeDeserializer(String attributeTypeUuid, boolean isNumber) {
		super(PersonAttribute.class);
		this.attributeTypeUuid = attributeTypeUuid;
		this.isNumber = isNumber;
	}
	
	@Override
	public ValueDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) {
		Attribute annotation = property.getAnnotation(Attribute.class);
		final String propName = annotation.typePropertyName();
		if (env == null) {
			env = Context.getBean(Environment.class);
		}
		
		return new AttributeDeserializer(env.getProperty(propName), annotation.isNumber());
	}
	
	@Override
	public PersonAttribute deserialize(JsonParser p, DeserializationContext ctxt) {
		final String value = p.getText();
		if (isNumber) {
			try {
				Double.parseDouble(value);
			}
			catch (NumberFormatException e) {
				return null;
			}
		}
		
		PersonAttribute attribute = new PersonAttribute();
		attribute.setValue(value);
		attribute.setAttributeTypeUuid(attributeTypeUuid);
		attribute.setPersonAttributeTypeUuid(Constants.SYNC_CLASS_ATTRIB_TYPE_LIGHT + "(" + attributeTypeUuid + ")");
		attribute.setCreatorUuid(Constants.SYNC_CLASS_USER_LIGHT + "(" + Constants.DAEMON_USER_UUID + ")");
		attribute.setDateCreated(Processor.date);
		return attribute;
	}
	
}
