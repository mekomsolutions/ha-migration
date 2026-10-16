/*
 * Copyright © 2024, Ozone HIS <info@ozone-his.com>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package net.mekomsolutions.ha.migration.bootstrap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import net.mekomsolutions.ha.migration.AppConfig;
import net.mekomsolutions.ha.migration.Processor;
import net.mekomsolutions.ha.migration.StartUpListener;

@SpringBootApplication
@Import(AppConfig.class)
public class Launcher {
	
	public static void main(String[] args) {
		SpringApplication.run(Launcher.class, args);
	}
	
	@Bean
	public StartUpListener startupListener(Processor processor) {
		return new StartUpListener(processor);
	}
	
}
