package com.boraecosystem.explorer.crawler.service;

import org.web3j.protocol.core.methods.response.EthBlock;
import rx.Observable;

import java.math.BigInteger;

public interface Crawler {

    void startStream();

    void startRange(BigInteger startBlock, BigInteger endBlock);

    void startFrom(BigInteger startBlock);

    void start(Observable<EthBlock> ethBlockObservable);
}
