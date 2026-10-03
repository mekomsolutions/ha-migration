package net.mekomsolutions.ha.migration;

import net.mekomsolutions.ha.migration.model.PatientIdentifier;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.BeanProperty;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.ValueDeserializer;
import tools.jackson.databind.deser.std.StdDeserializer;

/**
 * Custom deserializer for converting string identifiers into {@link PatientIdentifier} objects. The
 * deserialization behavior can be customized through the {@link Identifier} annotation on the
 * field. The deserialization process supports contextual initialization based on the field
 * properties annotated with {@link Identifier}.
 */
public class PatientIdentifierDeserializer extends StdDeserializer<PatientIdentifier> {
	
	private final String identifierType;
	
	private final boolean preferred;
	
	public PatientIdentifierDeserializer() {
		this(null, false);
	}
	
	public PatientIdentifierDeserializer(String identifierType, boolean preferred) {
		super(PatientIdentifier.class);
		this.identifierType = identifierType;
		this.preferred = preferred;
	}
	
	@Override
	public ValueDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) {
		Identifier annotation = property.getAnnotation(Identifier.class);
		final String type = annotation.typePropertyName();
		return new PatientIdentifierDeserializer(type, annotation.preferred());
	}
	
	@Override
	public PatientIdentifier deserialize(JsonParser p, DeserializationContext ctxt) {
		PatientIdentifier id = new PatientIdentifier();
		id.setIdentifier(p.getText());
		id.setIdentifierType(identifierType);
		id.setPreferred(preferred);
		return id;
	}
	
}
