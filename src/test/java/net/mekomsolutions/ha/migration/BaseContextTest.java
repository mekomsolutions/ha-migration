package net.mekomsolutions.ha.migration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

/**
 * Base class for all tests that require a spring application context
 */
@SpringBootTest(classes = { TestConfig.class })
@TestExecutionListeners(DependencyInjectionTestExecutionListener.class)
@TestPropertySource(properties = Constants.PROP_DATA_FILE_DIR + "=")
@TestPropertySource(properties = Constants.PROP_BATCH_SIZE + "=")
@TestPropertySource(properties = Constants.PROP_SYNC_SOURCE_ID + "=")
@TestPropertySource(properties = Constants.PROP_TIME_ZONE + "=GMT+3")
@TestPropertySource(properties = Constants.PROP_ID_TYPE_UUID_ID + "=type-uuid-1")
@TestPropertySource(properties = Constants.PROP_ID_TYPE_UUID_GOVT + "=type-uuid-2")
@TestPropertySource(properties = Constants.PROP_ATTR_TYPE_UUID_ETHNICITY + "=")
@TestPropertySource(properties = Constants.PROP_ATTR_TYPE_UUID_MARITAL_STATUS + "=")
@TestPropertySource(properties = Constants.PROP_ATTR_TYPE_UUID_PHONE + "=")
@TestPropertySource(properties = Constants.PROP_ATTR_TYPE_UUID_BIRTH_PLACE + "=")
@TestPropertySource(properties = Constants.PROP_ATTR_TYPE_UUID_SPOUSE + "=")
@TestPropertySource(properties = Constants.PROP_ATTR_TYPE_UUID_FATHER + "=")
@TestPropertySource(properties = Constants.PROP_ATTR_TYPE_UUID_MOTHER + "=")
@TestPropertySource(properties = Constants.PROP_ATTR_TYPE_UUID_IGSS + "=")
@TestPropertySource(properties = Constants.PROP_ATTR_TYPE_UUID_EMERGENCY_CONTACT + "=")
@TestPropertySource(properties = Constants.PROP_ATTR_TYPE_UUID_EMERGENCY_PHONE + "=")
@TestPropertySource(properties = Constants.PROP_ATTR_TYPE_UUID_OCCUPATION + "=")
public abstract class BaseContextTest {
	
	@Autowired
	protected ApplicationContext appContext;
	
}
