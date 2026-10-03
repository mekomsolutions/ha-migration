package net.mekomsolutions.ha.migration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used to mark a field as an identifier in the context of deserialization. This
 * annotation is typically used in conjunction with {@link PatientIdentifierDeserializer}
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Identifier {
	
	/**
	 * Retrieves the type property name associated with the identifier. This property is typically used
	 * to categorize or provide additional context about the identifier type in scenarios such as
	 * deserialization or metadata-based processing.
	 *
	 * @return the type property name as a String.
	 */
	String typePropertyName();
	
	/**
	 * Indicates whether this identifier is the preferred or primary identifier among multiple
	 * identifiers. This property is used in scenarios where there are multiple identifiers associated
	 * with the same context, and one needs to be marked as the preferred one for prioritization or
	 * selection purposes. The default value is {@code false}.
	 *
	 * @return {@code true} if this identifier is the preferred one; {@code false} otherwise.
	 */
	boolean preferred() default false;
	
}
