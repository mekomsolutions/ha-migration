package net.mekomsolutions.ha.migration;

import java.lang.reflect.ParameterizedType;
import java.util.Collection;

import net.mekomsolutions.ha.migration.converter.BaseRowConverter;

public class ProcessorUtils {
	
	public static BaseRowConverter getConverter(Class<?> clazz) {
		BaseRowConverter converter = null;
		Collection<BaseRowConverter> converters = Context.getBeans(BaseRowConverter.class);
		for (BaseRowConverter c : converters) {
			Class<?> beanClass = c.getClass();
			ParameterizedType parameterizedType = (ParameterizedType) beanClass.getGenericSuperclass();
			if (parameterizedType != null) {
				Class<?> firstTypeArgument = (Class<?>) parameterizedType.getActualTypeArguments()[0];
				if (clazz.equals(firstTypeArgument)) {
					converter = c;
					break;
				}
			}
		}
		
		return converter;
	}
}
