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
	
	public AttributeDeserializer() {
		this(null);
	}
	
	public AttributeDeserializer(String attributeTypeUuid) {
		super(PersonAttribute.class);
		this.attributeTypeUuid = attributeTypeUuid;
	}
	
	@Override
	public ValueDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) {
		Attribute annotation = property.getAnnotation(Attribute.class);
		final String propName = annotation.typePropertyName();
		if (env == null) {
			env = Context.getBean(Environment.class);
		}
		
		return new AttributeDeserializer(env.getProperty(propName));
	}
	
	@Override
	public PersonAttribute deserialize(JsonParser p, DeserializationContext ctxt) {
		PersonAttribute attribute = new PersonAttribute();
		attribute.setValue(p.getText());
		attribute.setPersonAttributeType(Constants.SYNC_CLASS_ATTRIB_TYPE_LIGHT + "(" + attributeTypeUuid + ")");
		attribute.setCreator(Constants.SYNC_CLASS_USER_LIGHT + "(" + Constants.DAEMON_USER_UUID + ")");
		attribute.setDateCreated(Processor.date);
		return attribute;
	}
	
}
