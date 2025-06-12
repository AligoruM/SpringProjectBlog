package org.practice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfiguration {

    private final Environment env;

    @Autowired
    public DataSourceConfiguration(Environment env) {
        this.env = env;
    }

    @EventListener
    public void populate(ContextRefreshedEvent event) {
        DataSource dataSource = event.getApplicationContext().getBean(DataSource.class);

        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("schema.sql"));
        Boolean prepopulationRequired = env.getProperty("testdata.prepopulation", Boolean.class, false);
        if (prepopulationRequired) {
            populator.addScript(new ClassPathResource("prepopulation.sql"));
        }
        populator.execute(dataSource);
    }

}
