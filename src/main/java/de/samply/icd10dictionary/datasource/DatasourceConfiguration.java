package de.samply.icd10dictionary.datasource;

import javax.sql.DataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class DatasourceConfiguration {

  @Bean(name = "icdDataSource")
  @ConfigurationProperties("app.datasource")
  public DataSource icdDataSource() {
    return DataSourceBuilder.create().build();
  }
}
