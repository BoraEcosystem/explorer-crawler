package com.boraecosystem.explorer.crawler.point.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigInteger;
import java.time.ZonedDateTime;

@Data
@Entity
@Table(name = "block_summary")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BlockSummary {
    @Id
    @Column(name = "block_no")
    private BigInteger blockNo;
    @Column(name = "block_hash")
    private String blockHash;
    @Column(name = "gas_limit")
    private BigInteger gasLimit;
    @Column(name = "gas_used")
    private BigInteger gasUsed;
    @Column(name = "nonce")
    private BigInteger nonce;
    @Column(name = "transaction_cnt")
    private Integer transactionCount;
    @Column(name = "uncle_cnt")
    private Integer uncleCount;
    @Column(name = "block_date")
    private ZonedDateTime blockDate;
}
