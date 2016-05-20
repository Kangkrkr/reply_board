package com.pilot.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@ComponentScan
public class HibernateConfig {

	@Bean(name = "sessionFactory")
	public LocalSessionFactoryBean sessionFactory() {
		LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();

		// 다음과 같이 Properties클래스를 사용하여 sessionFactory의 설정도 가능하다.
		Properties hibernateProperties = new Properties();
		hibernateProperties.setProperty("dialect", "org.hibernate.dialect.MySQLDialect");
		hibernateProperties.setProperty("hibernate.hbm2ddl.auto", "update");
		hibernateProperties.setProperty("show_sql", "true");

		sessionFactory.setDataSource(dataSource());
		sessionFactory.setPackagesToScan("com.pilot.entity");	// 2016-05-03 도메인들의 패키지명을 com.pilot.entity로 변경했었음.
		sessionFactory.setHibernateProperties(hibernateProperties);

		return sessionFactory;
	}

	@Bean
	public DataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("org.mariadb.jdbc.Driver");
		dataSource.setUrl(
				"jdbc:mysql://localhost:3306/" +
				"reply_board?autoReconnect=true&" +
				"useUnicode=false&characterEncoding=utf8&useSSL=false");
		dataSource.setUsername("root");
		dataSource.setPassword("***********");
		return dataSource;
	}

	@Bean
	@Autowired
	public HibernateTransactionManager transactionManager(SessionFactory s) {
		HibernateTransactionManager txManager = new HibernateTransactionManager();
		txManager.setSessionFactory(s);
		
		return txManager;
	}
}