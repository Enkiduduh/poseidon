//package com.app.poseidon;
//
//import com.app.poseidon.controllers.BidListController;
//import com.app.poseidon.domain.BidList;
//import com.app.poseidon.services.BidListService;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.math.BigDecimal;
//import java.util.List;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(controllers = BidListController.class)
//@AutoConfigureMockMvc(addFilters = false)        // désactive les filtres Spring Security
//class BidListControllerTest {
//
//    @Autowired
//    MockMvc mockMvc;
//
//    @MockBean
//    BidListService bidListService;     // bean dont votre controller dépend
//
//    @Test
//    void displayAllBidListTest() throws Exception {
//        // GIVEN
//        List<BidList> stub = List.of(new BidList("acc", "type", BigDecimal.ONE));
//        when(bidListService.getAllBids()).thenReturn(stub);
//
//        // WHEN / THEN
//        mockMvc.perform(get("/bidList/list"))
//                .andExpect(status().isOk())
//                .andExpect(model().attributeExists("bidLists"))
//                .andExpect(view().name("bidList/list"));
//    }
//}
