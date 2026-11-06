package net.mekomsolutions.ha.migration;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

import net.mekomsolutions.ha.migration.bootstrap.Launcher;

@Configuration
@EnableAutoConfiguration
@ComponentScan(excludeFilters = @Filter(type = FilterType.ASSIGNABLE_TYPE, classes = { Launcher.class,
        StartUpListener.class }))
public class TestProcessorConfig {}
