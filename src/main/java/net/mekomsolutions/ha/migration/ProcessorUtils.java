package net.mekomsolutions.ha.migration;

import java.lang.reflect.ParameterizedType;
import java.util.Collection;

import net.mekomsolutions.ha.migration.itemizer.BaseRowItemizer;

public class ProcessorUtils {
	
	/**
	 * Looks up the {@link BaseRowItemizer} for the specified type by searching through all available
	 * {@link BaseRowItemizer} beans in the application context to find one whose parameterized type
	 * matches the given class type.
	 *
	 * @param clazz the class type for which an appropriate {@link BaseRowItemizer} is to be retrieved
	 * @return a {@link BaseRowItemizer} if found otherwise null.
	 */
	public static BaseRowItemizer getItemizer(Class<?> clazz) {
		BaseRowItemizer itemizer = null;
		Collection<BaseRowItemizer> itemizers = Context.getBeans(BaseRowItemizer.class);
		for (BaseRowItemizer i : itemizers) {
			Class<?> beanClass = i.getClass();
			ParameterizedType pType = (ParameterizedType) beanClass.getGenericSuperclass();
			if (pType != null) {
				Class<?> firstTypeArgument = (Class<?>) pType.getActualTypeArguments()[0];
				if (clazz.equals(firstTypeArgument)) {
					itemizer = i;
					break;
				}
			}
		}
		
		return itemizer;
	}
}
