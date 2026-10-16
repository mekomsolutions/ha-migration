package net.mekomsolutions.ha.migration.converter;

import java.time.LocalDateTime;

import net.mekomsolutions.ha.migration.model.BaseEntity;

/**
 * Base class for classes that convert an object of a specific type to another object of a specific
 * type.
 *
 * @param <S> the type to convert from
 * @param <T> the type to convert to
 */
public abstract class BaseRowConverter<S, T extends BaseEntity> {
	
	/**
	 * Converts an instance of the target type using the provided source object and date.
	 *
	 * @param source the object to convert
	 * @param date the date to be used as the creation date for the generated object
	 * @return an instance of the generated object
	 */
	public abstract T convert(S source, LocalDateTime date);
	
}
