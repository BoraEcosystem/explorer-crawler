package com.boraecosystem.explorer.crawler.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.math.BigInteger;

@Data
@Configuration
@ConfigurationProperties(prefix = "application")
public class ApplicationProperties {

    private String boraContractHash;
    private BigInteger defaultStartBlock;

    public BigInteger getDefaultStartBlock() {
        if (defaultStartBlock == null) return BigInteger.ONE;
        return defaultStartBlock;
    }
}
