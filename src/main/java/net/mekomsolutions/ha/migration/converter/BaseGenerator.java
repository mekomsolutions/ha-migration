package net.mekomsolutions.ha.migration.converter;

import java.time.LocalDateTime;

import net.mekomsolutions.ha.migration.model.BaseEntity;

/**
 * Base class for classes that generate objects of a specific type from another object of a specific
 * type.
 *
 * @param <S> the type of the source object
 * @param <T> the target type of the target object to generate.
 */
public abstract class BaseGenerator<S, T extends BaseEntity> {
	
	/**
	 * Generates an instance of the target type using the provided source object and date.
	 *
	 * @param source the source object used to generate the target object
	 * @param date the date to be used as the creation date for the generated object
	 * @return an instance of the generated object
	 */
	public abstract T generate(S source, LocalDateTime date);
	
}
