package com.app.poseidon;

import com.app.poseidon.controllers.BidListController;
import com.app.poseidon.domain.BidList;
import com.app.poseidon.security.SecurityConfig;
import com.app.poseidon.services.BidListService;
import com.app.poseidon.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.*;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = BidListController.class)
@Import(SecurityConfig.class)
class BidListControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BidListService bidListService;

    @MockBean
    private UserService userService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    private BidList sample;


    @BeforeEach
    void setUp() {
        sample = new BidList("account1", "typeA", BigDecimal.valueOf(123.45));
        when(bidListService.getAllBids()).thenReturn(List.of(sample));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void getList_shouldShowAll() throws Exception {
        BidList b = new BidList("a", "t", new BigDecimal("1"));
        b.setId(10);
        given(bidListService.getAllBids()).willReturn(List.of(b));

        mockMvc.perform(get("/bidList/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("bidList/list"))
                .andExpect(model().attributeExists("bidLists"))
                .andExpect(model().attribute("bidLists", List.of(b)));
    }

    @Test
    @DisplayName("GET /bidList/list sans authentification → redirige vers /login")
    void whenNotAuthenticated_thenRedirectToLogin() throws Exception {
        mockMvc.perform(get("/bidList/list"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("GET /bidList/list avec authentification → affiche la liste")
    void whenAuthenticated_thenShowBidList() throws Exception {
        mockMvc.perform(get("/bidList/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("bidList/list"))
                .andExpect(model().attributeExists("bidLists"))
                .andExpect(model().attribute("bidLists", List.of(sample)));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void getAddForm() throws Exception {
        mockMvc.perform(get("/bidList/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("bidList/add"))
                .andExpect(model().attributeExists("bidList"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("POST /bidList/validate valid → redirect to list")
    void postValidate_valid() throws Exception {
        mockMvc.perform(post("/bidList/validate")
                        .with(csrf())
                        .param("account", "acc")
                        .param("type", "t")
                        .param("bidQuantity", "12.34")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bidList/list"));

        then(bidListService).should().save(any(BidList.class));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("POST /bidList/validate invalid → back to add")
    void postValidate_invalid() throws Exception {
        // champ manquant : bidQuantity
        mockMvc.perform(post("/bidList/validate")
                        .with(csrf())
                        .param("account", "tooLongAccountNameExceeding30Chars__________")
                        .param("type", "t")
                        .param("bidQuantity", String.valueOf(new BigDecimal("12.100002")))
                )
                .andExpect(status().isOk())
                .andExpect(view().name("bidList/add"))
                .andExpect(model().attributeHasFieldErrors("bidList", "account", "bidQuantity"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void getUpdateForm() throws Exception {
        BidList b = new BidList("a", "t", new BigDecimal("1"));
        b.setId(5);
        given(bidListService.findById(5)).willReturn(b);

        mockMvc.perform(get("/bidList/update/5"))
                .andExpect(status().isOk())
                .andExpect(view().name("bidList/update"))
                .andExpect(model().attribute("bidList", b));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void postUpdate_valid() throws Exception {
        mockMvc.perform(post("/bidList/update/5")
                        .with(csrf())
                        .param("account", "acc")
                        .param("type", "t")
                        .param("bidQuantity", "2.22")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bidList/list"));
        then(bidListService).should().update(eq(5), any(BidList.class));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void deleteBid() throws Exception {
        mockMvc.perform(get("/bidList/delete/7"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bidList/list"));
        then(bidListService).should().delete(7);
    }
}
