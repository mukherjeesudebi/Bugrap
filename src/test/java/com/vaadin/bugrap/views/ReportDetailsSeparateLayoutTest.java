package com.vaadin.bugrap.views;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

import com.vaadin.bugrap.TestViewSecurityConfig;
import com.vaadin.testbench.unit.SpringUIUnit4Test;

@ContextConfiguration(classes = TestViewSecurityConfig.class)
public class ReportDetailsSeparateLayoutTest extends SpringUIUnit4Test{

    @Test
    public void createReportsView() {
        navigate(MainLayout.class);
    }
}
