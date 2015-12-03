/*
 * Copyright 2012-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.ahyc.yjz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import java.util.Properties;

/**
 * Application 程序运行入口.
 */
@SpringBootApplication
public class Application{

		private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

		public static void main(String[] args) throws Exception {
				LOGGER.info("Application start to running.");
				SpringApplication springApplication = new SpringApplicationBuilder()
							.sources(Application.class)
							.showBanner(true)
							.registerShutdownHook(true)
							.web(true)
							.build();

				//set spring.profiles.active
				Resource resource = new ClassPathResource("config/application.properties");
				Properties properties = new Properties();
				properties.load(resource.getInputStream());
				String profile = properties.getProperty("spring.profiles.active");
				LOGGER.info("Read spring.profiles.active={} from application.properties.", profile);
				profile = "@profiles.active@".equals(profile) ? "development" : profile;
				LOGGER.info("Set default value for spring.profiles.active={}", profile);
				springApplication.setAdditionalProfiles(profile);

				//run
				springApplication.run(args);
		}
}
