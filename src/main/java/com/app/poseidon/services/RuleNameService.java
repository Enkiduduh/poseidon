package com.app.poseidon.services;

import com.app.poseidon.domain.RuleName;
import com.app.poseidon.repositories.RuleNameRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RuleNameService {
    private final RuleNameRepository ruleNameRepository;

    public RuleNameService(RuleNameRepository ruleNameRepository) {
        this.ruleNameRepository = ruleNameRepository;
    }

    public List<RuleName> getAllRuleName() {
        return ruleNameRepository.findAll();
    }

    public RuleName findById(Integer id) {
        return ruleNameRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid ruleName Id:" + id));
    }

    @Transactional
    public void save(@Valid RuleName ruleName) {
        ruleNameRepository.save(ruleName);
    }

    @Transactional
    public void update(Integer id, RuleName data) {
        RuleName existing = ruleNameRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid ruleName id:" + id));
        existing.setName(data.getName());
        existing.setDescription(data.getDescription());
        existing.setJson(data.getJson());
        existing.setTemplate(data.getTemplate());
        existing.setSqlStr(data.getSqlStr());
        existing.setSqlPart(data.getSqlPart());
        ruleNameRepository.save(existing);
    }

    @Transactional
    public void delete(Integer id) {
        RuleName existing = ruleNameRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Invalid ruleName id:" + id));
        ruleNameRepository.delete(existing);
    }
}
