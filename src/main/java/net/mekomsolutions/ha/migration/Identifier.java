package net.mekomsolutions.ha.migration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used to mark a field as an identifier in the context of deserialization. This
 * annotation is typically used in conjunction with {@link IdentifierDeserializer}
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Identifier {
	
	/**
	 * Specifies the application `property name to be used to look up the identifier type uuid.
	 *
	 * @return the property name as a String.
	 */
	String typePropertyName();
	
	/**
	 * Specifies whether the deserialized PatientIdentifier object should be marked as preferred or not.
	 *
	 * @return {@code true} if this identifier is the preferred one; {@code false} otherwise.
	 */
	boolean preferred() default false;
	
}
