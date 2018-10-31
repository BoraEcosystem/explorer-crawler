package com.boraecosystem.explorer.crawler.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.boraecosystem.explorer.crawler")
@EntityScan(basePackages = {"com.boraecosystem.explorer.crawler"})
public class RepositoryConfiguration {
}
