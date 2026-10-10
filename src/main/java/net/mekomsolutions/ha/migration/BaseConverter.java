package net.mekomsolutions.ha.migration;

import org.springframework.core.convert.converter.Converter;

import net.mekomsolutions.ha.migration.model.BaseEntity;

/**
 * Base class for classes that convert data from one type to another. This class implements the
 * {@link Converter} interface and provides a skeletal structure for specific converter
 * implementations.
 *
 * @param <S> the source type to be converted
 * @param <T> the target type to which the source will be converted; must extend {@link BaseEntity}
 */
public abstract class BaseConverter<S, T extends BaseEntity> implements Converter<S, T> {
	
	@Override
	public T convert(S data) {
		return null;
	}
	
}
