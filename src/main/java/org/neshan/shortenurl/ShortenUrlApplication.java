package org.neshan.shortenurl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan("org.neshan.shortenurl.configurationproperties")
public class ShortenUrlApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShortenUrlApplication.class, args);
	}

}
