package com.dsl.demo;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.Pollers;
//import org.springframework.integration.dsl.core.Pollers;
import org.springframework.integration.jdbc.JdbcPollingChannelAdapter;
import org.springframework.messaging.MessageHandler;

import oracle.jdbc.pool.OracleDataSource;

@Configuration
@EnableIntegration
@IntegrationComponentScan
public class JDBCPoller {

	
	@Bean
	public DirectChannel starterChannel() {
		return new DirectChannel();
	}
	
	@Autowired
	MessagePrinter printer;
	
	@Primary
	@Bean(name= {"odsDataSource"})
	public DataSource odsDataSource() throws SQLException{
		OracleDataSource ds = new OracleDataSource();
		ds.setDriverType("thin");
		ds.setServerName("localhost");
		ds.setPortNumber(1521);
		ds.setDatabaseName("ORCLCDB");
		ds.setUser("demo");
		ds.setPassword("demo");
		return ds;
	}
	
	@Bean
	public MessageSource<Object> jdbcMessageSource() throws SQLException {
	   JdbcPollingChannelAdapter a = new JdbcPollingChannelAdapter(odsDataSource(), "select msg from sintegration");
	   return a;
	}
		
	@Bean
	public IntegrationFlow dbpollerFlow() throws Exception{		
		return IntegrationFlows.from(jdbcMessageSource()
						, c -> c.poller(Pollers.fixedRate(10000).maxMessagesPerPoll(1)))
					.split()
					.handle(printer)
					.handle(finished())
					.get();	
	}
	
	@Bean
	public MessageHandler finished() {
		return m ->{System.out.println("?????"+m.getPayload());};
	}
	
}



