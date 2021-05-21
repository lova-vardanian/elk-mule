package com.test.db;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@PropertySource("classpath:${env}.properties")
public class DatabaseConfiguration {
	
	@Value("${DATABASE_URL}")
	String propertyConectionURL; 
	
	@Value("${env}")
	String env;
	
	@PostConstruct
	private void postConstruct() {
		propertyConectionURL = System.getenv("DATABASE_URL") == null ? propertyConectionURL : System.getenv("DATABASE_URL");
		env = System.getenv("env") == null ? env : System.getenv("env");
	}
	
	@Bean("postgreDataSource")
    public HikariDataSource dataSource(){

        String conURL = propertyConectionURL;
        
        String[] connectionProperty = conURL.split("@");
        
        String url = env.equals("dev") ? "jdbc:postgresql://" + connectionProperty[1] : "jdbc:postgresql://" + connectionProperty[1]; // + "?sslmode=require";
        String[] userAndPassword = connectionProperty[0].replace("postgres://","").split(":");

        HikariConfig config = new HikariConfig();

        
        config.setJdbcUrl(url);
        config.setUsername(userAndPassword[0]);
        config.setPassword(userAndPassword[1]);
        config.setMaximumPoolSize(18);
        config.setMaximumPoolSize(10);
        config.setDriverClassName("org.postgresql.Driver");
        //config.setDataSourceClassName("org.postgresql.ds.PGSimpleDataSource");
        
        return new HikariDataSource(config);
    }
}