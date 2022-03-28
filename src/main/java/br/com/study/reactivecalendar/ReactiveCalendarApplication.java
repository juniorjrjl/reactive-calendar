package br.com.study.reactivecalendar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableReactiveMongoAuditing;

@SpringBootApplication
@EnableReactiveMongoAuditing(dateTimeProviderRef = "dateTimeProvider")
public class ReactiveCalendarApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReactiveCalendarApplication.class, args);
	}

}
