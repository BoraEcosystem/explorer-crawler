package com.boraecosystem.explorer.crawler.service;

import com.boraecosystem.explorer.crawler.point.domain.BlockSummary;
import com.boraecosystem.explorer.crawler.point.domain.LastBlock;
import com.boraecosystem.explorer.crawler.point.domain.TransactionSummary;

public interface RepositoryService {

    TransactionSummary saveTransaction(TransactionSummary transactionSummary);


    LastBlock getLatestBlockNo();

    void saveLastBlock(LastBlock lastBlock);

    void initializeLastBlock(LastBlock lastBlock);

    void saveBlock(BlockSummary blockSummary);
}
