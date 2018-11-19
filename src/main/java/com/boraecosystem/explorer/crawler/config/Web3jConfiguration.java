package com.boraecosystem.explorer.crawler.config;

import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jService;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Async;

import java.util.concurrent.TimeUnit;

@Configuration
@ConfigurationProperties(prefix = "web3j")
@Slf4j
public class Web3jConfiguration {

    @Bean
    public Web3j web3j() {
        Web3jService web3jService = buildService(getClientAddress());
        log.info("Building Web3J Instance for endpoint: " + getClientAddress());
        return Web3j.build(web3jService, 1 * 1000L, Async.defaultExecutorService());
    }

    private Web3jService buildService(String clientAddress) {
        return new HttpService(clientAddress, createOkHttpClient(), false);
    }

    private OkHttpClient createOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        configureLogging(builder);
        configureTimeouts(builder);
        return builder.build();
    }

    private void configureTimeouts(OkHttpClient.Builder builder) {
        Long tos = getHttpTimeoutSeconds();
        log.info("Web3J Timeout Configuration: {} seconds" + tos);
        if (tos != null) {
            builder.connectTimeout(tos, TimeUnit.SECONDS);
            builder.readTimeout(tos, TimeUnit.SECONDS);  // Sets the socket timeout too
            builder.writeTimeout(tos, TimeUnit.SECONDS);
        }
    }

    private static void configureLogging(OkHttpClient.Builder builder) {
        if (log.isDebugEnabled()) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor(log::debug);
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(logging);
        }
    }

    private String clientAddress;
    private Long httpTimeoutSeconds;

    public String getClientAddress() {
        return clientAddress;
    }

    public void setClientAddress(String clientAddress) {
        this.clientAddress = clientAddress;
    }

    public Long getHttpTimeoutSeconds() {
        return httpTimeoutSeconds;
    }

    public void setHttpTimeoutSeconds(Long httpTimeoutSeconds) {
        this.httpTimeoutSeconds = httpTimeoutSeconds;
    }
}
