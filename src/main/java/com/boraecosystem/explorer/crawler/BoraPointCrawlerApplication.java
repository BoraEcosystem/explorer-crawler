package com.boraecosystem.explorer.crawler;

import com.boraecosystem.explorer.crawler.config.ApplicationProperties;
import com.boraecosystem.explorer.crawler.point.domain.LastBlock;
import com.boraecosystem.explorer.crawler.service.Crawler;
import com.boraecosystem.explorer.crawler.service.RepositoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.web3j.protocol.Web3j;

import java.math.BigInteger;
import java.util.Optional;

@SpringBootApplication
@Slf4j
public class BoraPointCrawlerApplication implements ApplicationRunner {

    private static final String ARG_START = "start";
    private static final String ARG_END = "end";
    private static final String ARG_STREAM = "stream";
    private static final String ARG_CATCH_UP = "catch-up";

    private final Web3j web3j;
    private final Crawler crawler;
    private final RepositoryService repositoryService;
    private final ApplicationProperties applicationProperties;

    public BoraPointCrawlerApplication(Web3j web3j, Crawler crawler, ApplicationProperties applicationProperties, RepositoryService repositoryService) {
        this.web3j = web3j;
        this.crawler = crawler;
        this.repositoryService = repositoryService;
        this.applicationProperties = applicationProperties;
    }

    public static void main(String[] args) {
        SpringApplication.run(BoraPointCrawlerApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        initialize();

        if (args.getSourceArgs().length == 0 || args.containsOption(ARG_STREAM)) {
            log.info("Application is starting from the current block number: {} in STREAM mode.", web3j.ethBlockNumber().send().getBlockNumber());
            crawler.startStream();
        } else if (args.containsOption(ARG_CATCH_UP)) {
            final BigInteger startBlock = Optional.ofNullable(repositoryService.getLatestBlockNo())
                .map(block -> block.getBlockNo().add(BigInteger.ONE))
                .orElse(applicationProperties.getDefaultStartBlock());
            log.info("Application is starting from the block number: {} in CATCH-UP mode.", startBlock);
            crawler.startFrom(startBlock);
        } else if (args.containsOption(ARG_START) && !args.containsOption(ARG_END)) {
            final BigInteger startBlock = args.containsOption(ARG_START)
                ? new BigInteger(args.getOptionValues(ARG_START).get(0))
                : applicationProperties.getDefaultStartBlock();
            log.info("Application is starting from the block number: {} in RANGE-FROM mode.", startBlock);
            crawler.startFrom(startBlock);
        } else if (args.containsOption(ARG_START) && args.containsOption(ARG_END)) {
            final BigInteger endBlock = args.containsOption(ARG_END)
                ? new BigInteger(args.getOptionValues(ARG_END).get(0))
                : web3j.ethBlockNumber().send().getBlockNumber();
            final BigInteger startBlock = args.containsOption(ARG_START)
                ? new BigInteger(args.getOptionValues(ARG_START).get(0))
                : BigInteger.ZERO.max(endBlock.subtract(BigInteger.valueOf(5L)));
            if (startBlock.compareTo(endBlock) > 0) // startBlock > endBlock
                throw new IllegalArgumentException("start-block must be less than or equal end-block");
            log.info("Application is starting from block number: {} to the block number: {} in RANGE mode.", startBlock, endBlock);
            crawler.startRange(startBlock, endBlock);
        } else {
            throw new IllegalArgumentException("Please, input arguments properly.");
        }
    }

    private void initialize() {
        repositoryService.initializeLastBlock(new LastBlock(applicationProperties.getDefaultStartBlock()));
    }
}

