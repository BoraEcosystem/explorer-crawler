package com.boraecosystem.explorer.crawler.point.repository;

import com.boraecosystem.explorer.crawler.point.domain.LastBlock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface LastBlockRepository extends CrudRepository<LastBlock, Integer> {
    @Modifying
    @Query("UPDATE LastBlock b SET b.blockNo = :blockNo WHERE b.id = :id AND b.blockNo < :blockNo")
    void updateLastBlock(@Param("blockNo") BigInteger blockNo, @Param("id") int id);

    LastBlock findTopByOrderByBlockNoDesc();
}
