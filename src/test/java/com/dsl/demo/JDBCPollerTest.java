package com.dsl.demo;

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.model.TestTimedOutException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.messaging.MessageHandler;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { JDBCPoller.class, MessagePrinter.class, JDBCPollerTest.TestConf.class })
@DirtiesContext
//(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class JDBCPollerTest {
		
	private static CountDownLatch latch = new CountDownLatch(1);	
	JDBCPoller poller = new JDBCPoller();
	
	@Test
	public void test() throws IOException, TestTimedOutException, InterruptedException, SQLException {
		
		if(!latch.await(30, TimeUnit.SECONDS)) {
		throw new TestTimedOutException(30, TimeUnit.SECONDS);
		}			
	}	
	
	@Configuration
	@EnableIntegration
	public static class TestConf{
		@Bean
		public MessageHandler finished() {
			return m -> latch.countDown();
		}
	}
}