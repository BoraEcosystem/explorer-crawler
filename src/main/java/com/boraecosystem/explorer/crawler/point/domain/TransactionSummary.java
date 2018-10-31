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
@Table(name = "transaction_summary")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionSummary {
    @Id
    @Column(name = "transaction_hash")
    private String transactionHash;
    @Column(name = "transaction_index")
    private BigInteger transactionIndex;
    @Column(name = "block_no")
    private BigInteger blockNo;
    @Column(name = "from_address")
    private String fromAddress;
    @Column(name = "to_address")
    private String toAddress;
    @Column(name = "contract_address")
    private String contractAddress;
    private String method;
    private String status;
    private BigInteger nonce;
    private BigInteger amount;
    @Column(name = "tx_id")
    private BigInteger txId;
    @Column(name = "json_data")
    private String data;
    @Column(name = "block_date")
    private ZonedDateTime blockDate;
}
