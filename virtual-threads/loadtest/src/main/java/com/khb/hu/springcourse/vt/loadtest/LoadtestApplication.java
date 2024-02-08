package com.khb.hu.springcourse.vt.loadtest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.client.RestClient;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootApplication
public class LoadtestApplication implements CommandLineRunner {

	@Autowired
	RestClient.Builder restClientBuilder;

	RestClient restClient;

	private CountDownLatch latch;

	public static void main(String[] args) {
		SpringApplication.run(LoadtestApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		int numThreads = Integer.parseInt(args[0]);
		int numRequests = Integer.parseInt(args[1]);
		String uri = args[2];

		latch = new CountDownLatch(numRequests);

		restClient = restClientBuilder.baseUrl(uri).build();

		long now = System.nanoTime();

		ExecutorService exec = Executors.newVirtualThreadPerTaskExecutor ();
		for(int i=0; i< numThreads; i++) {
			exec.submit(() -> {
				while(latch.getCount() > 0) {
					restClient.get().retrieve().toBodilessEntity();
					latch.countDown();
				}
			});
		}

		latch.await();
		long duration = System.nanoTime() - now;
		exec.shutdownNow();
		System.out.format("Sending %d requests with %d threads took %d ms%n", numRequests, numThreads, duration/1_000_000);
	}
}
