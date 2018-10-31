package com.boraecosystem.explorer.crawler.point.repository;

import com.boraecosystem.explorer.crawler.point.domain.TransactionSummary;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionSummaryRepository extends CrudRepository<TransactionSummary, String> {
}
