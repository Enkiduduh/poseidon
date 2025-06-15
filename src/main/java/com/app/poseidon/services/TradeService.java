package com.app.poseidon.services;

import com.app.poseidon.domain.Trade;
import com.app.poseidon.repositories.TradeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TradeService {
    private final TradeRepository tradeRepository;

    public TradeService(TradeRepository tradeRepository) {
        this.tradeRepository = tradeRepository;
    }

    public List<Trade> getAllTrades() {
        return tradeRepository.findAll();
    }

    public Trade findById(Integer id) {
        return tradeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Trade not found with this id;" + id));
    }

    public void save(Trade trade) {
        tradeRepository.save(trade);
    }


    public void update(Integer id, Trade data) {
        Trade existing = TradeService.this.tradeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Trade not found with this id:" + id));
        existing.setAccount(data.getAccount());
        existing.setType(data.getType());
        existing.setBuyQuantity(data.getBuyQuantity());
        TradeService.this.tradeRepository.save(existing);
    }


    public void delete(Integer id) {
        Trade existing = tradeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Trade not found with this id:" + id));
        tradeRepository.delete(existing);
    }
}
