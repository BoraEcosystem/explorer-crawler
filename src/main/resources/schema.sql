CREATE TABLE IF NOT EXISTS block_summary (
  block_no  bigint(20) unsigned COMMENT 'the block number',
  block_hash varchar(66) NOT NULL COMMENT '32 Bytes - hash of the transactions this log was created from. null when its pending log',
  gas_limit bigint(20) DEFAULT NULL COMMENT 'Gas Limit',
  gas_used  bigint(20) DEFAULT NULL COMMENT 'Gas Used',
  nonce varchar(20) DEFAULT NULL,
  transaction_cnt int(10) DEFAULT 0,
  uncle_cnt int(10) DEFAULT 0,
  block_date datetime NOT NULL COMMENT 'block.timestamp',
  PRIMARY KEY (block_no),
  KEY idx_block_summary_block_date (block_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS transaction_summary (
  transaction_hash varchar(66) NOT NULL COMMENT '32 Bytes - hash of the transactions this log was created from. null when its pending log',
  transaction_index int(10) DEFAULT 0,
  block_no bigint(20) COMMENT 'the block number',
  nonce varchar(20) DEFAULT NULL,
  from_address varchar(42) DEFAULT NULL COMMENT 'Sender',
  to_address varchar(42) DEFAULT NULL COMMENT 'Receiver',
  contract_address varchar(42) DEFAULT NULL COMMENT 'contract',
  method varchar(30) DEFAULT NULL COMMENT 'method name',
  status varchar(10) DEFAULT NULL COMMENT 'SUCCESS/FAIL',
  amount varchar(30) DEFAULT NULL COMMENT 'amount',
  tx_id varchar(100) DEFAULT NULL COMMENT 'tx id',
  json_data varchar(1000) DEFAULT NULL COMMENT 'data',
  block_date datetime NOT NULL COMMENT 'block.timestamp',
  PRIMARY KEY (transaction_hash),
  KEY idx_tx_summary_from (from_address),
  KEY idx_tx_summary_to (to_address)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE IF NOT EXISTS last_block (
  id int(10) unsigned COMMENT 'id',
  block_no bigint(20) unsigned COMMENT 'the block number',
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
