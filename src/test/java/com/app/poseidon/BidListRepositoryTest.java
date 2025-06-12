package com.app.poseidon;

import com.app.poseidon.domain.BidList;
import com.app.poseidon.repositories.BidListRepository;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;


import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@DataJpaTest
@SpringBootTest
@AutoConfigureMockMvc
@MockitoSettings(strictness = Strictness.LENIENT)
public class BidListRepositoryTest {

    @MockBean
    BidListRepository bidListRepository;

    @BeforeEach
    void setup() {
        BidList b = new BidList();
        b.setId(1);
        b.setAccount("Cpt1");
        b.setType("T1");
        b.setBidQuantity(new BigDecimal("1.2345"));
        b.setAskQuantity(new BigDecimal("2.3456"));
        b.setBid(new BigDecimal("1.1111"));
        b.setAsk(new BigDecimal("2.2222"));
        b.setCommentary("OK");
        b.setCreationName("UserA");
        b.setDealName("DealA");
        b.setDealType("TypeA");
        b.onCreate();
        when(bidListRepository.findById(1))
                .thenReturn(Optional.of(b));
    }

    @Test
    public void saveBidListTest() {
        BidList b = new BidList();
        b.setId(2);
        b.setAccount("Cpt2");
        b.setType("T2");
        b.setBidQuantity(new BigDecimal("1.2345"));
        b.setAskQuantity(new BigDecimal("2.3456"));
        b.setBid(new BigDecimal("1.1111"));
        b.setAsk(new BigDecimal("2.2222"));
        b.setCommentary("OK");
        b.setCreationName("UserA");
        b.setDealName("DealA");
        b.setDealType("TypeA");
        b.onCreate();
        bidListRepository.save(b);

        // Save
        Assert.assertNotNull(b.getId());
        Assert.assertEquals("1.2345", b.getBidQuantity());
    }

    @Test
    public void updateBidListTest() {
        int id = 1;
        BidList bid = bidListRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Bid not found with this id:" + id));
        bid.setCommentary("New commentary");
        bidListRepository.save(bid);

        assertThat(bid.getCommentary()).isEqualTo("New commentary");
    }

    @Test
    public void getAllBidListsTest() {
        // Find
        List<BidList> listResult = bidListRepository.findAll();
        Assert.assertFalse(listResult.isEmpty());
    }

    @Test
    public void deleteBidListTest() {
        BidList bid = new BidList();
        bid.setId(3);
        bid.setAccount("Cpt1");
        bid.setType("T1");
        bid.setBidQuantity(new BigDecimal("1.2345"));
        bid.setAskQuantity(new BigDecimal("2.3456"));
        bid.setBid(new BigDecimal("1.1111"));
        bid.setAsk(new BigDecimal("2.2222"));
        bid.setCommentary("OK");
        bid.setCreationName("UserA");
        bid.setDealName("DealA");
        bid.setDealType("TypeA");
        bid.onCreate();
        bidListRepository.save(bid);

        // Delete
        Integer id = bid.getId();
        bidListRepository.delete(bid);
        Optional<BidList> bidList = bidListRepository.findById(id);
        Assert.assertFalse(bidList.isPresent());
    }



}
