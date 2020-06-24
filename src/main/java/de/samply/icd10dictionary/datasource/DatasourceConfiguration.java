package de.samply.icd10dictionary.datasource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DatasourceConfiguration {

    @Bean(name = "icdDataSource")
    @ConfigurationProperties("app.datasource")
    public DataSource icdDataSource() {
        return DataSourceBuilder.create().build();
    }
}
