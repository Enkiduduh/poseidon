package com.app.poseidon.services;

import com.app.poseidon.domain.Rating;
import com.app.poseidon.repositories.RatingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RatingService {
    private final RatingRepository ratingRepository;

    public RatingService(RatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }

    public List<Rating> getAllRatings() {
        return ratingRepository.findAll();
    }

    public Rating findById(Integer id) {
        return ratingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Rating non found with this id:" + id));
    }

    public void save(Rating rating) {
        ratingRepository.save(rating);
    }

    public void update(Integer id, Rating data) {
        Rating existing = ratingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Rating not found with this is:" + id));
        existing.setMoodysRating(data.getMoodysRating());
        existing.setSandPRating(data.getSandPRating());
        existing.setFitchRating(data.getFitchRating());
        existing.setOrderNumber(data.getOrderNumber());
        ratingRepository.save(existing);
    }

    public void delete(Integer id) {
        Rating existing = ratingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Rating not found with this is:" + id));
        ratingRepository.delete(existing);
    }
}
