package code;

// Copyright (c) 2024, NoCodeNoLife-cloud. All rights reserved.
// Author: NoCodeNoLife-cloud
// stay hungry, stay foolish
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
// @EnableEurekaServer
// @EnableDiscoveryClient
// @EnableFeignClients
// @EnableDubbo
@SpringBootApplication
public class SpringBootApplicationLauncher {
	private static final String LOG_DIRECTORY_PATH = "src/log";
	private static final int MAXIMUM_SAVED_LOGS = 3;

	static {
		printBanner();
	}

	/**
	 * Entry point of the application.
	 *
	 * @param args The command line arguments.
	 */
	@SneakyThrows
	public static void main(String[] args) {
		SpringApplication.run(SpringBootApplicationLauncher.class, args);

		SimpleClientHttpRequestFactory clientFactory = new SimpleClientHttpRequestFactory();
		clientFactory.setConnectTimeout(10);
		clientFactory.setReadTimeout(10);
		RestTemplate restTemplate = new RestTemplate(clientFactory);

		int clientSize = 200;
		ExecutorService fixedThreadPool = Executors.newFixedThreadPool(clientSize);

		Random random = new Random();
		CountDownLatch downLatch = new CountDownLatch(clientSize);

		for (int i = 0; i < clientSize; i++) {
			try {
				TimeUnit.MILLISECONDS.sleep(random.nextInt(0, 100));
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
			fixedThreadPool.submit(() -> {
				System.out.println(restTemplate.getForObject("http://localhost:8080/limit1", String.class));
				downLatch.countDown();
			});
		}
		System.out.println(downLatch.getCount());
		downLatch.await();
		fixedThreadPool.shutdown();
		log.info("end");
	}

	/**
	 * Prints the application banner.
	 */
	private static void printBanner() {
		// Print the banner
		log.info("""
				       __                __                                       __                ___            __ _       __\s
				  ___ / /_ ___ _ __ __  / /  __ __ ___  ___ _ ____ __ __     ___ / /_ ___ _ __ __  / _/___  ___   / /(_)___  / /\s
				 (_-</ __// _ `// // / / _ \\/ // // _ \\/ _ `// __// // /_   (_-</ __// _ `// // / / _// _ \\/ _ \\ / // /(_-< / _ \\
				/___/\\__\
				/ \\_,_/ \\_, / /_//_/\\_,_//_//_/\\_, //_/   \\_, /( ) /___/\\__/ \\_,_/ \\_, / /_/  \\___/\\___//_//_//___//_//_/
				               /___/                  /___/      /___/ |/                 /___/                                 \s""");
	}
}