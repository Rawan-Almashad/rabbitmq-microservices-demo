package iti.gov.consumer;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE,
		properties = {
				"eureka.client.enabled=false",
				"spring.cloud.discovery.enabled=false",
		})
class ConsumerApplicationTests {

	@Test
	void contextLoads() {
	}

}
