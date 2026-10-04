package net.mekomsolutions.ha.migration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used to mark a field as a person attribute in the context of deserialization. This
 * annotation is typically used in conjunction with {@link AttributeDeserializer}
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Attribute {
	
	/**
	 * Specifies the application `property name to be used to look up the attribute type uuid.
	 *
	 * @return the property name as a String.
	 */
	String typePropertyName();
	
}
