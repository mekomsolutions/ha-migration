package net.mekomsolutions.ha.migration;

import java.util.Collection;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class Context implements ApplicationContextAware {
	
	private static ApplicationContext appContext;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		appContext = applicationContext;
	}
	
	/**
	 * Gets the bean matching the specified type from the application context
	 *
	 * @return an instance of the bean matching the specified type
	 */
	public static <T> T getBean(Class<T> clazz) {
		return appContext.getBean(clazz);
	}
	
	/**
	 * Gets all beans matching the specified type from the application context
	 *
	 * @return list of beans matching the specified type
	 */
	public static <T> Collection<T> getBeans(Class<T> clazz) {
		return appContext.getBeansOfType(clazz).values();
	}
	
}
