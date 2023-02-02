package com.vaadin.bugrap;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.vaadin.bugrap.domain.spring.DBTools;

import com.vaadin.flow.component.charts.themes.LumoLightTheme;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

@SpringBootApplication
@Theme(value = "bugrap")
@NpmPackage(value = "line-awesome", version = "1.3.0")
@NpmPackage(value = "@vaadin-component-factory/vcf-nav", version = "1.0.6")
@ComponentScan({"org.vaadin.bugrap.domain.spring","com.vaadin.bugrap"})
@EnableJpaRepositories(basePackages = "com.vaadin.bugrap.repository")
public class BugrapApplication extends SpringBootServletInitializer implements AppShellConfigurator{

	@Autowired
	private DBTools dbTools;

	@PostConstruct
	protected void onInit() {
		dbTools.clear();
		dbTools.create();
	}
	
	public static void main(String[] args) {
		SpringApplication.run(BugrapApplication.class, args);
	}

}
