package com.boraecosystem.explorer.crawler.service;

import io.reactivex.Flowable;
import org.web3j.protocol.core.methods.response.EthBlock;

import java.math.BigInteger;

public interface Crawler {

    void startStream();

    void startRange(BigInteger startBlock, BigInteger endBlock);

    void startFrom(BigInteger startBlock);

    void start(Flowable<EthBlock> ethBlockObservable);
}
