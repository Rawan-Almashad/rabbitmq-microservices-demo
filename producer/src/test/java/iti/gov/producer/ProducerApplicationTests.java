package iti.gov.producer;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE,
		properties = {
				"eureka.client.enabled=false",
				"spring.cloud.discovery.enabled=false",
		})
class ProducerApplicationTests {

	@Test
	void contextLoads() {
	}

}
