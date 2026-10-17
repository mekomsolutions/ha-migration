package net.mekomsolutions.ha.migration;

import java.lang.reflect.ParameterizedType;
import java.util.Collection;

import net.mekomsolutions.ha.migration.itemizer.BaseRowItemizer;

public class ProcessorUtils {
	
	public static BaseRowItemizer getItemizer(Class<?> clazz) {
		BaseRowItemizer itemizer = null;
		Collection<BaseRowItemizer> itemizers = Context.getBeans(BaseRowItemizer.class);
		for (BaseRowItemizer i : itemizers) {
			Class<?> beanClass = i.getClass();
			ParameterizedType parameterizedType = (ParameterizedType) beanClass.getGenericSuperclass();
			if (parameterizedType != null) {
				Class<?> firstTypeArgument = (Class<?>) parameterizedType.getActualTypeArguments()[0];
				if (clazz.equals(firstTypeArgument)) {
					itemizer = i;
					break;
				}
			}
		}
		
		return itemizer;
	}
}
