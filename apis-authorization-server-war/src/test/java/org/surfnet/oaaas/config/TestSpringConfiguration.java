/*
 * Copyright 2012 SURFnet bv, The Netherlands
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.surfnet.oaaas.config;

import javax.inject.Inject;

import org.apache.openjpa.persistence.PersistenceProviderImpl;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.surfnet.oaaas.repository.ExceptionTranslator;
import org.surfnet.oaaas.repository.OpenJPAExceptionTranslator;

@Configuration
@PropertySource("classpath:apis.application.test.properties")
/*
 * The component scan can be used to add packages and exclusions to the default
 * package
 */
@ImportResource("classpath:spring-repositories.xml")
@EnableTransactionManagement
public class TestSpringConfiguration {

  private static final String PERSISTENCE_UNIT_NAME = "oaaas";
  private static final Class<PersistenceProviderImpl> PERSISTENCE_PROVIDER_CLASS = PersistenceProviderImpl.class;

  @Inject
  Environment env;

  @Bean
  public javax.sql.DataSource dataSource() {
    DataSource dataSource = new DataSource();
    dataSource.setDriverClassName(env.getProperty("jdbc.driverClassName"));
    dataSource.setUrl(env.getProperty("jdbc.url"));
    dataSource.setUsername(env.getProperty("jdbc.username"));
    dataSource.setPassword(env.getProperty("jdbc.password"));
    return dataSource;
  }

  @Bean
  public JpaTransactionManager transactionManager() {
    return new JpaTransactionManager();
  }

  @Bean
  public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
    final LocalContainerEntityManagerFactoryBean emfBean = new LocalContainerEntityManagerFactoryBean();
    emfBean.setDataSource(dataSource());
    emfBean.setPersistenceUnitName(PERSISTENCE_UNIT_NAME);
    emfBean.setPersistenceProviderClass(PERSISTENCE_PROVIDER_CLASS);
    return emfBean;
  }

  @Bean
  public ExceptionTranslator exceptionTranslator() {
    return new OpenJPAExceptionTranslator();
  }

}
