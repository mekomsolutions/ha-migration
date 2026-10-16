package net.mekomsolutions.ha.migration;

import java.lang.reflect.ParameterizedType;
import java.util.Collection;

import net.mekomsolutions.ha.migration.generator.BaseGenerator;

public class ProcessorUtils {
	
	public static BaseGenerator getGenerator(Class<?> clazz) {
		BaseGenerator generator = null;
		Collection<BaseGenerator> generators = Context.getBeans(BaseGenerator.class);
		for (BaseGenerator g : generators) {
			Class<?> beanClass = g.getClass();
			ParameterizedType parameterizedType = (ParameterizedType) beanClass.getGenericSuperclass();
			if (parameterizedType != null) {
				Class<?> firstTypeArgument = (Class<?>) parameterizedType.getActualTypeArguments()[0];
				if (clazz.equals(firstTypeArgument)) {
					generator = g;
					break;
				}
			}
		}
		
		return generator;
	}
}
