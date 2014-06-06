/**
 * (c) 2003-2013 MuleSoft, Inc. This software is protected under international
 * copyright law. All use of this software is subject to MuleSoft's Master
 * Subscription Agreement (or other Terms of Service) separately entered
 * into between you and MuleSoft. If such an agreement is not in
 * place, you may not use the software.
 */

package org.mule.modules.tests;

import org.junit.Test;
import org.junit.runners.model.InitializationError;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestFlowTests extends AutomationTestCaseTestParent {

    @Test
    public void runFlowManualTestData() {
        TestData data = new TestData().withFlowVar("foo", "bar").withPayload(1000);
        TestFlowResult result = getFlow("cat-foo-and-payload").run(data);
        assertEquals("bar1000", result.getPayload());
    }

    @Test
    public void runFlowWithTestDataBean() {
        TestFlowResult result = getFlow("cat-foo-and-payload").runWithBean("fooBar1000TestData");
        assertEquals("bar1000", result.getPayload());
    }

    @Test
    public void runFlowWithRegularMapBean() {
        TestFlowResult result = getFlow("echo-payload").runWithBean("fooBar1000");
        assertEquals(getBeanFromContext("fooBar1000"), result.getPayload());
    }

    @Test
    public void runFlowWithBean() {
        Integer expected = getBeanFromContext("integerPOJO");
        TestFlowResult result = getFlow("echo-payload").runWithBean("integerPOJO");
        assertEquals(expected, result.getPayload());
    }

    @Test
    public void runFlowWithoutData() {
        TestFlowResult result = getFlow("echo-foo").run();
        assertEquals("foo", result.getPayload());
    }

    @Test(expected = IllegalArgumentException.class)
    public void nonExistentFlow() throws Exception {
        getFlow("fooFlow");
    }

    @Test
    public void nullPayload() {
        TestFlowResult result = getFlow("null-payload").run();
        assertTrue(result.isNullPayload());
    }

}
