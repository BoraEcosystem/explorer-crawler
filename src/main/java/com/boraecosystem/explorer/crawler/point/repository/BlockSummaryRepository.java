package com.boraecosystem.explorer.crawler.point.repository;

import com.boraecosystem.explorer.crawler.point.domain.BlockSummary;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface BlockSummaryRepository extends CrudRepository<BlockSummary, BigInteger> {
}
