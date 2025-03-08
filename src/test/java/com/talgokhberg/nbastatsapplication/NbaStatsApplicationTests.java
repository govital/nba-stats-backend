package com.talgokhberg.nbastatsapplication;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;


@SpringBootTest(properties = {"spring.config.location=classpath:application-test.properties"})
public class NbaStatsApplicationTests {

	@Test
	void contextLoads() {
	}
}

