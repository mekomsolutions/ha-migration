package net.mekomsolutions.ha.migration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

/**
 * Base class for all tests that require a spring application context
 */
@SpringBootTest(classes = { TestConfig.class })
@TestExecutionListeners(DependencyInjectionTestExecutionListener.class)
public abstract class BaseContextTest {
	
	@Autowired
	protected ApplicationContext appContext;
	
}
