package com.app.poseidon.services;

import com.app.poseidon.domain.RuleName;
import com.app.poseidon.repositories.RuleNameRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RuleNameServiceTest {

    @Mock
    private RuleNameRepository repo;

    @InjectMocks
    private RuleNameService service;

    private RuleName sample;

    @BeforeEach
    void setUp() {
        sample = new RuleName();
        sample.setName("name");
        sample.setDescription("desc");
        sample.setJson("json");
        sample.setTemplate("template");
        sample.setSqlStr("sqlstr");
        sample.setSqlPart("sqlpart");
        sample.setId(1);
    }
    @Test
    void getAllRuleName_shouldReturnAll() {
        when(repo.findAll()).thenReturn(List.of(sample));
        var all = service.getAllRuleName();
        assertThat(all).containsExactly(sample);
        verify(repo).findAll();
    }

    @Test
    void findById_exists() {
        when(repo.findById(1)).thenReturn(Optional.of(sample));
        assertThat(service.findById(1)).isSameAs(sample);
    }

    @Test
    void findById_notFound() {
        when(repo.findById(2)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.findById(2))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid ruleName Id:2");
    }

    @Test
    void save_callsRepository() {
        service.save(sample);
        verify(repo).save(sample);
    }

    @Test
    void update_success() {
        RuleName updated = new RuleName();
        updated.setName("nameNew");
        updated.setDescription("descNew");
        updated.setJson("jsonNew");
        updated.setTemplate("templateNew");
        updated.setSqlStr("sqlstrNew");
        updated.setSqlPart("sqlpartNew");
        when(repo.findById(1)).thenReturn(Optional.of(sample));

        service.update(1, updated);

        assertThat(sample.getName()).isEqualTo("nameNew");
        assertThat(sample.getDescription()).isEqualTo("descNew");
        assertThat(sample.getJson()).isEqualTo("jsonNew");
        assertThat(sample.getTemplate()).isEqualTo("templateNew");
        assertThat(sample.getSqlStr()).isEqualTo("sqlstrNew");
        assertThat(sample.getSqlPart()).isEqualTo("sqlpartNew");
        verify(repo).save(sample);
    }

    @Test
    void update_notFound() {
        when(repo.findById(99)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.update(99, sample))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void delete_success() {
        when(repo.findById(1)).thenReturn(Optional.of(sample));
        service.delete(1);
        verify(repo).delete(sample);
    }

    @Test
    void delete_notFound() {
        when(repo.findById(42)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.delete(42))
                .isInstanceOf(EntityNotFoundException.class);
    }
}
