package com.boraecosystem.explorer.crawler.service;

import com.boraecosystem.explorer.crawler.point.domain.BlockSummary;
import com.boraecosystem.explorer.crawler.point.domain.LastBlock;
import com.boraecosystem.explorer.crawler.point.domain.TransactionSummary;
import com.boraecosystem.explorer.crawler.point.repository.BlockSummaryRepository;
import com.boraecosystem.explorer.crawler.point.repository.LastBlockRepository;
import com.boraecosystem.explorer.crawler.point.repository.TransactionSummaryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Slf4j
public class RepositoryServiceImpl implements RepositoryService {

    private final LastBlockRepository lastBlockRepository;
    private final BlockSummaryRepository blockSummaryRepository;
    private final TransactionSummaryRepository transactionSummaryRepository;

    public RepositoryServiceImpl(LastBlockRepository lastBlockRepository, BlockSummaryRepository blockSummaryRepository, TransactionSummaryRepository transactionSummaryRepository) {
        this.lastBlockRepository = lastBlockRepository;
        this.blockSummaryRepository = blockSummaryRepository;
        this.transactionSummaryRepository = transactionSummaryRepository;
    }

    @Override
    @Transactional
    public TransactionSummary saveTransaction(TransactionSummary transactionSummary) {
        log.debug("{}", transactionSummary);
        return transactionSummaryRepository.save(transactionSummary);
    }

    @Override
    @Transactional
    public void saveBlock(BlockSummary blockSummary) {
        log.debug("{}", blockSummary);
        blockSummaryRepository.save(blockSummary);
    }

    @Override
    @Transactional
    public void saveLastBlock(LastBlock lastBlock) {
        try {
            lastBlockRepository.updateLastBlock(lastBlock.getBlockNo(), LastBlock.ID);
        } catch (InvalidDataAccessApiUsageException e) {
            lastBlockRepository.save(lastBlock);
        }
    }

    @Override
    public LastBlock getLatestBlockNo() {
        return lastBlockRepository.findTopByOrderByBlockNoDesc();
    }

    @Override
    public void initializeLastBlock(LastBlock lastBlock) {
        Optional<LastBlock> exist = lastBlockRepository.findById(lastBlock.getId());
        if (!exist.isPresent()) {
            lastBlockRepository.save(lastBlock);
        }
    }
}
