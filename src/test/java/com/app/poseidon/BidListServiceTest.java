package com.app.poseidon.services;

import com.app.poseidon.domain.BidList;
import com.app.poseidon.repositories.BidListRepository;
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
class BidListServiceTest {

    @Mock
    private BidListRepository repo;

    @InjectMocks
    private BidListService service;

    private BidList sample;

    @BeforeEach
    void setUp() {
        sample = new BidList("acc", "type", new BigDecimal("123.45"));
        sample.setId(1);
    }

    @Test
    void getAllBids_shouldReturnAll() {
        when(repo.findAll()).thenReturn(List.of(sample));
        var all = service.getAllBids();
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
                .hasMessageContaining("Invalid bid Id:2");
    }

    @Test
    void save_callsRepository() {
        service.save(sample);
        verify(repo).save(sample);
    }

    @Test
    void update_success() {
        BidList updated = new BidList("newAcc","newType", new BigDecimal("9.99"));
        when(repo.findById(1)).thenReturn(Optional.of(sample));

        service.update(1, updated);

        assertThat(sample.getAccount()).isEqualTo("newAcc");
        assertThat(sample.getType()).isEqualTo("newType");
        assertThat(sample.getBidQuantity()).isEqualByComparingTo("9.99");
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
