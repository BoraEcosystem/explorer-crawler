package com.boraecosystem.explorer.crawler.point.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigInteger;

@Data
@Entity
@Table(name = "last_block")
@NoArgsConstructor
public class LastBlock {
    @Transient
    public static int ID = 1;

    @Id
    private int id;
    @Column(name = "block_no")
    private BigInteger blockNo;

    public LastBlock(BigInteger blockNo) {
        this.id = ID;
        this.blockNo = blockNo;
    }
}
