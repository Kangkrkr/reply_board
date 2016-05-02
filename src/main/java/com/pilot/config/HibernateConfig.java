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
	public LocalSessionFactoryBean  sessionFactory() {
		LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();

		Properties hibernateProperties = new Properties();
		hibernateProperties.setProperty("use_outer_join", "true");
		hibernateProperties.setProperty("dialect", "org.hibernate.dialect.MySQLDialect");
		
		sessionFactory.setDataSource(dataSource());
		sessionFactory.setPackagesToScan("com.pilot.domain");
		sessionFactory.setHibernateProperties(hibernateProperties);
		
		return sessionFactory;
	}
	
	@Bean
	public DataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("org.mariadb.jdbc.Driver");
		dataSource.setUrl("jdbc:mysql://localhost:3306/reply_board?autoReconnect=true&useUnicode=false&characterEncoding=utf8&useSSL=false");
		dataSource.setUsername("root");
		dataSource.setPassword("tmddbs0723!@1!");
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