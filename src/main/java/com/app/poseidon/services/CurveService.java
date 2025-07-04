package com.app.poseidon.services;

import com.app.poseidon.domain.CurvePoint;
import com.app.poseidon.repositories.CurvePointRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CurveService {
    private final CurvePointRepository curvePointRepository;

    public CurveService(CurvePointRepository curvePointRepository) {
        this.curvePointRepository = curvePointRepository;
    }

    public List<CurvePoint> getAllCurvePoints() {
        return curvePointRepository.findAll();
    }

    public CurvePoint findById(Integer id) {
        return curvePointRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("CurvePoint not found with this id:" + id));
    }

    @Transactional
    public void save(CurvePoint curvePoint) {
        curvePointRepository.save(curvePoint);
    }

    @Transactional
    public void update(Integer id, CurvePoint data) {
        CurvePoint existing = curvePointRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("CurvePoint not found with this id:" + id));
        existing.setTerm(data.getTerm());
        existing.setValue(data.getValue());
        curvePointRepository.save(existing);
    }

    @Transactional
    public void delete(Integer id) {
        CurvePoint existing = curvePointRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("CurvePoint not found with this id:" + id));
        curvePointRepository.delete(existing);
    }
}
