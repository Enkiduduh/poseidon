//package com.app.poseidon;
//
//import com.app.poseidon.domain.BidList;
//import com.app.poseidon.repositories.BidListRepository;
//import org.junit.Assert;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.junit.jupiter.MockitoSettings;
//import org.mockito.quality.Strictness;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//
//import java.math.BigDecimal;
//import java.util.List;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.when;
//
//@DataJpaTest
//@ExtendWith(SpringExtension.class)
//public class BidListRepositoryTest {
//
//    @Autowired
//    private BidListRepository bidListRepository;
//
//    @Test
//    public void testBidListLifecycle() {
//        // Create
//        BidList bid = new BidList();
//        bid.setAccount("TestAccount");
//        bid.setType("TestType");
//        bid.setBidQuantity(new BigDecimal("10.00"));
//        bid.setAskQuantity(new BigDecimal("10.00"));
//        bid.setBid(new BigDecimal("10.00"));
//        bid.setAsk(new BigDecimal("10.00"));
//        bid.setCommentary("Comment");
//        bid.setCreationName("Creator");
//        bid.setRevisionName("Revisor");
//        bid.setDealName("Deal");
//        bid.setDealType("Type");
//
//        // Test auto-generated fields
//        assertNull(bid.getId());
//        assertNull(bid.getBidListDate());
//
//        // Save
//        BidList savedBid = bidListRepository.save(bid);
//        assertNotNull(savedBid.getId());
//        assertNotNull(savedBid.getBidListDate());
//        assertNotNull(savedBid.getCreationDate());
//        assertNotNull(savedBid.getRevisionDate());
//
//        // Retrieve
//        Optional<BidList> foundBid = bidListRepository.findById(savedBid.getId());
//        assertTrue(foundBid.isPresent());
//        assertEquals(0, new BigDecimal("10.00").compareTo(foundBid.get().getBidQuantity()));
//
//        // Update
//        savedBid.setAccount("UpdatedAccount");
//        bidListRepository.save(savedBid);
//        Optional<BidList> updatedBid = bidListRepository.findById(savedBid.getId());
//        assertEquals("UpdatedAccount", updatedBid.get().getAccount());
//
//        // Delete
//        bidListRepository.delete(savedBid);
//        assertFalse(bidListRepository.findById(savedBid.getId()).isPresent());
//    }
//
//
//}
