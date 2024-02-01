package com.khb.hu.gateway;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

import java.time.Duration;

@EnableDiscoveryClient
@SpringBootApplication
public class GatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
	}
	@Bean
	public Customizer<ReactiveResilience4JCircuitBreakerFactory> defaultCustomizer() {
		return factory -> factory.configureDefault( id ->
				new Resilience4JConfigBuilder(id)
						.circuitBreakerConfig(CircuitBreakerConfig.custom()
								.slidingWindowSize(5)
								.slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
								.minimumNumberOfCalls(1)
								.failureRateThreshold(20.0f)
								.waitDurationInOpenState(Duration.ofSeconds(5))
								.permittedNumberOfCallsInHalfOpenState(2)
								.build())
						.timeLimiterConfig(
								TimeLimiterConfig.custom()
										.timeoutDuration(Duration.ofSeconds(10))
								.build()
						)
						.build()
		);
	}

}
