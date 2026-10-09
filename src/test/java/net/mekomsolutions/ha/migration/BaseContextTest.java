package net.mekomsolutions.ha.migration;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

/**
 * Base class for all tests that require a spring application context
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = { TestConfig.class })
@TestExecutionListeners(DependencyInjectionTestExecutionListener.class)
@TestPropertySource(properties = Constants.PROP_USER_UUID + "=test-user-uuid")
@TestPropertySource(properties = Constants.PROP_ID_TYPE_UUID_ID + "=type-uuid-1")
@TestPropertySource(properties = Constants.PROP_ID_TYPE_UUID_GOVT + "=type-uuid-2")
public abstract class BaseContextTest {
	
	@Autowired
	protected ApplicationContext appContext;
	
}
