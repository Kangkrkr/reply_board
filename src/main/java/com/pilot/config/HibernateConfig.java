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

	// 스프링 인액션 - 372P
	// setPackagesToScan 메소드는 패키지들을 검사하여 퍼시스턴스 어노테이션이 적용된 도메인 클래스를 찾는다.(다수의 도메인
	// 클래스가 존재)
	// 여기에는 JPA의 @Entity나 @MappedSuperclass 그리고 하이버네이트의 자체적인 @Entity 어노테이션이 적용된
	// 클래스가 포함된다.

	// 소수의 도메인 클래스가 존재할 때는 다음처럼 지정할 수도 있다.
	// sessionFactory.setAnnotatedClasses(new Class<?>[]{User.class, Post.class,
	// Reply.class});
	@Bean(name = "sessionFactory")
	public LocalSessionFactoryBean sessionFactory() {
		LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();

		// 다음과 같이 Properties클래스를 사용하여 sessionFactory의 설정도 가능하다.
		Properties hibernateProperties = new Properties();
		hibernateProperties.setProperty("use_outer_join", "true");
		hibernateProperties.setProperty("dialect", "org.hibernate.dialect.MySQLDialect");

		sessionFactory.setDataSource(dataSource());
		sessionFactory.setPackagesToScan("com.pilot.entity");	// 2016-05-03 도메인들의 패키지명을 com.pilot.entity로 변경했었음.
		sessionFactory.setHibernateProperties(hibernateProperties);

		return sessionFactory;
	}

	// 데이터소스 설정은 스프링 인액션 - 350P
	@Bean
	public DataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("org.mariadb.jdbc.Driver");
		dataSource.setUrl(
				"jdbc:mysql://localhost:3306/reply_board?autoReconnect=true&useUnicode=false&characterEncoding=utf8&useSSL=false");
		dataSource.setUsername("root");
		dataSource.setPassword("tmddbs0723!@1!");
		return dataSource;
	}

	// 현재 클래스에 있는 sessionFactory라는 이름의 빈이 파라미터로 자동 주입된다.
	@Bean
	@Autowired
	public HibernateTransactionManager transactionManager(SessionFactory s) {
		HibernateTransactionManager txManager = new HibernateTransactionManager();
		txManager.setSessionFactory(s);
		
		return txManager;
	}
}