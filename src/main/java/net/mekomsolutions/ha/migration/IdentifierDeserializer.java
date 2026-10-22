package net.mekomsolutions.ha.migration;

import java.time.LocalDateTime;

import org.springframework.core.env.Environment;

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
public class IdentifierDeserializer extends StdDeserializer<PatientIdentifier> {
	
	private static Environment env;
	
	private final String idTypeUuid;
	
	private final boolean preferred;
	
	public IdentifierDeserializer() {
		this(null, false);
	}
	
	public IdentifierDeserializer(String idTypeUuid, boolean preferred) {
		super(PatientIdentifier.class);
		this.idTypeUuid = idTypeUuid;
		this.preferred = preferred;
	}
	
	@Override
	public ValueDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) {
		Identifier annotation = property.getAnnotation(Identifier.class);
		final String propName = annotation.typePropertyName();
		if (env == null) {
			env = Context.getBean(Environment.class);
		}
		
		return new IdentifierDeserializer(env.getProperty(propName), annotation.preferred());
	}
	
	@Override
	public PatientIdentifier deserialize(JsonParser p, DeserializationContext ctxt) {
		PatientIdentifier id = new PatientIdentifier();
		id.setIdentifier(p.getText());
		id.setIdentifierType(Constants.SYNC_CLASS_ID_LIGHT + "(" + idTypeUuid + ")");
		id.setPreferred(preferred);
		id.setCreator(Constants.SYNC_CLASS_USER_LIGHT + "(" + Constants.DAEMON_USER_UUID + ")");
		id.setDateCreated((LocalDateTime) ctxt.getAttribute("date"));
		return id;
	}
	
}
