/**
 * (c) 2003-2013 MuleSoft, Inc. This software is protected under international
 * copyright law. All use of this software is subject to MuleSoft's Master
 * Subscription Agreement (or other Terms of Service) separately entered
 * into between you and MuleSoft. If such an agreement is not in
 * place, you may not use the software.
 */

package org.mule.modules.tests;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.mule.api.MuleEvent;
import org.mule.api.MuleMessage;
import org.mule.construct.Flow;
import org.mule.tck.junit4.FunctionalTestCase;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


/**
 * Connector Tests Framework Parent Class
 *
 * @author Mulesoft, Inc
 */
@SuppressWarnings("unchecked")
public abstract class ConnectorTestCase extends FunctionalTestCase {

    private static final Logger LOGGER = Logger.getLogger(ConnectorTestCase.class);

    protected static final String DEFAULT_SPRING_CONFIG_FILE = "AutomationSpringBeans.xml";
    protected static final String[] SPRING_CONFIG_FILES = new String[] { DEFAULT_SPRING_CONFIG_FILE };

	private Map<String, Object> testData = new HashMap<String, Object>();
	private static ApplicationContext context;

    @BeforeClass
    public static void beforeClass(){  	
    	try {
    		context = new ClassPathXmlApplicationContext(getConfigSpringFiles());
        } catch (Exception e) {
            LOGGER.warn("Spring beans file was not found. Couldn't create the context for ConnectorTestParent.");
        }
    }
    
    protected static String[] getConfigSpringFiles() {
    	return SPRING_CONFIG_FILES;
    }
    
    @Override
    protected String getConfigResources() {
        return getConfigXmlFile();
    }

    protected String getConfigXmlFile() {
        return "automation-test-flows.xml";
    }
    
    protected Flow lookupFlowConstruct(String name) {
        return (Flow) muleContext.getRegistry().lookupFlowConstruct(name);
    }

    protected MuleMessage runFlowAndGetMessage(String flowName) throws Exception {
        MuleEvent response = lookupFlowConstruct(flowName).process(getTestEvent(testData));
        return response.getMessage();
    }

    protected MuleMessage runFlowAndGetMessage(String flowName, String beanId) throws Exception {
        MuleEvent response = lookupFlowConstruct(flowName).process(getTestEvent(context.getBean(beanId)));
        return response.getMessage();
    }
    
    protected <T> T runFlowAndGetPayload(String flowName) throws Exception {
        MuleEvent response = lookupFlowConstruct(flowName).process(getTestEvent(testData));
        return (T) response.getMessage().getPayload();
    }

    protected <T> T runFlowAndGetPayload(String flowName, String beanId) throws Exception {
        MuleEvent response = lookupFlowConstruct(flowName).process(getTestEvent(context.getBean(beanId)));
        return (T) response.getMessage().getPayload();
    }
    
    /**
     * Use initializeTestRunMessage instead
     * @param data
     */
    @Deprecated
    protected void loadTestRunMessage(String beanId) {
		Object bean = context.getBean(beanId);
		testData.clear();
		if (bean instanceof Map) {
			testData.putAll((Map<String, Object>) context.getBean(beanId));
		} else {
			testData.put(beanId, bean);
		}
    }
 
    /**
     * Use initializeTestRunMessage instead
     * @param data
     */
    @Deprecated
	public void loadTestRunMessage(Map<String,Object> data) {
		testData.clear();
		testData.putAll(data);
	}
	
    protected void initializeTestRunMessage(String beanId) {
		Object bean = context.getBean(beanId);
		testData.clear();
		if (bean instanceof Map) {
			testData.putAll((Map<String, Object>) context.getBean(beanId));
		} else {
			testData.put(beanId, bean);
		}
    }
 
	public void initializeTestRunMessage(String key, Object value) {
		initializeTestRunMessage(new HashMap<String,Object>());
		upsertOnTestRunMessage(key,value);
	}
    
	public void initializeTestRunMessage(Map<String,Object> data) {
		testData.clear();
		testData.putAll(data);
	}
   
    public <T> T getBeanFromContext(String beanId) throws BeansException {
		return (T) context.getBean(beanId);
	}
    
    public void upsertBeanFromContextOnTestRunMessage(String beanId) {
		Object bean = getBeanFromContext(beanId);
		testData.put(beanId, bean);
    }
    
    public void upsertBeanFromContextOnTestRunMessage(String key,String beanId) {
		Object bean = getBeanFromContext(beanId);
		testData.put(key, bean);
    }
    
	public void upsertOnTestRunMessage(String key, Object value) {
		testData.put(key, value);
	}

	public void upsertOnTestRunMessage(Map<String,Object> data) {
		testData.putAll(data);
	}

	public <T> T getTestRunMessageValue(String key) {
		return (T) testData.get(key);
	}

	public void removeFromTestRunMessage(String key) {
		testData.remove(key);
	}

	public boolean keyContainedInTestRunMessage(Object key) {
		return testData.containsKey(key);
	}
	
	public Set<String> getTestRunMessageKeySet() {
		return testData.keySet();
	}    
}
