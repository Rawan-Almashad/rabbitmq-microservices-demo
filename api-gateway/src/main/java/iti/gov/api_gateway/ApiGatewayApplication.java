package iti.gov.api_gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ApiGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiGatewayApplication.class, args);
	}
	@Bean
	public RouteLocator routes(RouteLocatorBuilder builder) {
		return builder.routes()
				.route("producer", r -> r
						.path("/producer/**")
						.uri("lb://producer"))
				.route("consumer", r -> r
						.path("/consumer/**")
						.uri("lb://consumer"))
				.route("frontend", r -> r
						.path("/frontend/**")
						.uri("lb://frontend"))
				.build();
	}
}
