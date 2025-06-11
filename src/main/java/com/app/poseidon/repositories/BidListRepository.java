package com.app.poseidon.repositories;

import com.app.poseidon.domain.BidList;
import com.app.poseidon.services.BidListService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface BidListRepository extends JpaRepository<BidList, Integer> {
    List<BidList> findAll();

}
