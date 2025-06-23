package com.app.poseidon.services;

import com.app.poseidon.domain.BidList;
import com.app.poseidon.repositories.BidListRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BidListService {
    private final BidListRepository bidListRepository;

    public BidListService(BidListRepository bidListRepository) {
        this.bidListRepository = bidListRepository;
    }

    public List<BidList> getAllBids() {
        return bidListRepository.findAll();
    }

    public BidList findById(Integer id) {
        return bidListRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid bid Id:" + id));
    }

    @Transactional
    public void save(@Valid BidList bid) {
        bidListRepository.save(bid);
    }

    @Transactional
    public void update(Integer id, BidList data) {
        BidList bid = bidListRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid id:" + id));
        bid.setAccount(data.getAccount());
        bid.setType(data.getType());
        bid.setBidQuantity(data.getBidQuantity());
        bidListRepository.save(bid);
    }

    @Transactional
    public void delete(Integer id) {
        BidList bid = bidListRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Bid not found with id:" + id));
        bidListRepository.delete(bid);
    }
}
