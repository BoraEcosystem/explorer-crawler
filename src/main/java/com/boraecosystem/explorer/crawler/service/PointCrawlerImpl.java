package com.boraecosystem.explorer.crawler.service;

import com.boraecosystem.explorer.crawler.config.ApplicationProperties;
import com.boraecosystem.explorer.crawler.constant.Method;
import com.boraecosystem.explorer.crawler.point.domain.BlockSummary;
import com.boraecosystem.explorer.crawler.point.domain.LastBlock;
import com.boraecosystem.explorer.crawler.point.domain.TransactionSummary;
import com.boraecosystem.explorer.crawler.utils.InputParser;
import io.reactivex.Flowable;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.utils.Flowables;

import java.math.BigInteger;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class PointCrawlerImpl implements Crawler {

    private static final String SUCCESS = "0x1";
    private final ApplicationProperties applicationProperties;
    private final Web3j web3j;
    private final RepositoryService repositoryService;

    public PointCrawlerImpl(ApplicationProperties applicationProperties,
                            Web3j web3j,
                            RepositoryService repositoryService) {
        this.applicationProperties = applicationProperties;
        this.web3j = web3j;
        this.repositoryService = repositoryService;
    }

    @Override
    public void startStream() {
        start(web3j.blockFlowable(true));
    }

    @Override
    public void startFrom(BigInteger startBlock) {
        start(web3j.replayPastAndFutureBlocksFlowable(new DefaultBlockParameterNumber(startBlock), true));
    }

    @Override
    public void startRange(BigInteger startBlock, BigInteger endBlock) {
        log.debug("startBlock:{}, endBlock:{}", startBlock, endBlock);
        start(Flowables.range(startBlock, endBlock)
            .map(DefaultBlockParameterNumber::new)
            .map(p -> web3j.ethGetBlockByNumber(p, true))
            .concatMap(Request::flowable));
    }

    @Override
    public void start(Flowable<EthBlock> ethBlockObservable) {
        ethBlockObservable
            .map(EthBlock::getBlock)
            .doOnNext(block -> log.debug("BLOCK INFO::{}", block))
            .filter(Objects::nonNull)
            .doOnNext(this::setBlock)
            .doOnNext(block -> repositoryService.saveLastBlock(new LastBlock(block.getNumber())))
            .filter(block -> block.getTransactions().size() > 0)
            .concatMap(this::handleTransactions)
            .doOnError(exception -> {
                log.error("Exception Raised: {}, {}", exception.getMessage(), exception.getStackTrace());
                final LastBlock latestBlockNo = repositoryService.getLatestBlockNo();
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ie) {
                    log.error("Exception Raised while sleep: {}", ie.getMessage());
                }
                log.warn("Trying to get data from Blockchain after Exception.");
                startFrom(latestBlockNo.getBlockNo().subtract(BigInteger.TEN));
            })
            .subscribe();

    }

    private Flowable<?> handleTransactions(EthBlock.Block block) {
        return Flowable.fromIterable(block.getTransactions())
            .map(EthBlock.TransactionObject.class::cast)
            .doOnNext(transaction -> log.debug("TransactionHash: {}", transaction.get().getHash()))
            .filter(transaction -> isBoraContract(transaction.getTo()))
            .doOnNext(transaction -> setTransaction(block, transaction))
            .doOnComplete(() -> repositoryService.saveLastBlock(new LastBlock(block.getNumber())));
    }

    @SneakyThrows
    private void setTransaction(EthBlock.Block block, final EthBlock.TransactionObject transaction) {
        log.debug("## transaction: {} / {}", transaction.getHash(), transaction.getBlockNumber());
        final String input = transaction.getInput();
        final Method method = Method.fromHash(input);
        log.debug("## Method: {}", method.toString());

        if (method == Method.UNDEFINED) return;

        TransactionSummary tx = TransactionSummary.builder()
            .transactionHash(transaction.getHash())
            .transactionIndex(transaction.getTransactionIndex())
            .blockNo(transaction.getBlockNumber())
            .fromAddress(transaction.getFrom())
            .contractAddress(transaction.getTo())
            .nonce(transaction.getNonce())
            .blockDate(timestampToDateTime(block.getTimestamp()))
            .build();

        final List<String> params = InputParser.chunk64(input.substring(10));

        switch (method) {
            case TRANSFER:
                tx.setToAddress(getAddressValue(params, 0));
                tx.setAmount(getUint256Value(params, 1));
                tx.setTxId(getUint256Value(params, 2));
                tx.setData(getByte32Value(params, 3));
                tx.setMethod(method.toString());
                break;
            case TRANSFER_FROM:
                tx.setToAddress(getAddressValue(params, 1));
                tx.setAmount(getUint256Value(params, 2));
                tx.setTxId(getUint256Value(params, 3));
                tx.setData(getByte32Value(params, 4));
                tx.setMethod(method.toString());
                break;
            case BURN:
            case MINT:
            case APPROVE:
            case INCREASE_APPROVAL:
            case DECREASE_APPROVAL:
                tx.setToAddress(getAddressValue(params, 0));
                tx.setAmount(getUint256Value(params, 1));
                tx.setTxId(getUint256Value(params, 2));
                tx.setMethod(method.toString());
                break;
        }

        Optional<TransactionReceipt> transactionReceipt = web3j.ethGetTransactionReceipt(transaction.getHash()).send().getTransactionReceipt();
        transactionReceipt.map(receipt -> {
            tx.setStatus(statusCode(receipt.getStatus()));
            return tx;
        }).orElseThrow(() -> new RuntimeException("EXCEPTION!!!!!!"));

        log.debug("## tx : {}", tx);

        repositoryService.saveTransaction(tx);

    }

    private void setBlock(EthBlock.Block block) {
        BlockSummary blockSummary = BlockSummary.builder()
            .blockNo(block.getNumber())
            .blockHash(block.getHash())
            .gasLimit(block.getGasLimit())
            .gasUsed(block.getGasUsed())
            .nonce(block.getNonce())
            .transactionCount(block.getTransactions().size())
            .uncleCount(block.getUncles().size())
            .blockDate(timestampToDateTime(block.getTimestamp()))
            .build();
        repositoryService.saveBlock(blockSummary);
    }

    private String statusCode(String status) {
        return (SUCCESS.equals(status)) ? "SUCCESS" : "FAIL";
    }

    private boolean isBoraContract(String address) {
        return applicationProperties.getBoraContractHash().equalsIgnoreCase(address);
    }

    private ZonedDateTime timestampToDateTime(BigInteger timestamp) {
        return ZonedDateTime.ofInstant(Instant.ofEpochSecond(timestamp.longValue()), ZoneId.of("UTC"));
    }

    private String getAddressValue(List<String> params, int index) {
        if (index < params.size()) {
            return InputParser.decode(params.get(index), Address.class).getValue();
        }
        return null;
    }

    private BigInteger getUint256Value(List<String> params, int index) {
        if (index < params.size()) {
            return InputParser.decode(params.get(index), Uint256.class).getValue();
        }
        return null;
    }

    private String getByte32Value(List<String> params, int index) {
        if (index < params.size()) {
            return new String(InputParser.decode(params.get(index), Bytes32.class).getValue());
        }
        return null;

    }

}
