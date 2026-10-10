package net.mekomsolutions.ha.migration.converter;

import java.time.LocalDateTime;

import net.mekomsolutions.ha.migration.model.BaseEntity;

/**
 * Base class for classes that convert data from one type to another.
 *
 * @param <S> the source type to be converted
 * @param <T> the target type to which the source will be converted; must extend {@link BaseEntity}
 */
public abstract class BaseConverter<S, T extends BaseEntity> {
	
	/**
	 * Convert the source object of type {@code S} to target type {@code T}.
	 * 
	 * @param source the source object to convert, which must be an instance of {@code S} (never
	 *            {@code null})
	 * @param date the date to be used as date created for the converted object
	 * @return the converted object, which must be an instance of {@code T} (potentially {@code null})
	 */
	public abstract T convert(S source, LocalDateTime date);
	
}
