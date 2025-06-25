package com.app.poseidon;

import com.app.poseidon.controllers.TradeController;
import com.app.poseidon.domain.Trade;
import com.app.poseidon.security.SecurityConfig;
import com.app.poseidon.services.TradeService;
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

@WebMvcTest(controllers = TradeController.class)
@Import(SecurityConfig.class)
class TradeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TradeService tradeService;

    @MockBean
    private UserService userService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    private Trade sample;


    @BeforeEach
    void setUp() {
        sample = new Trade();
        sample.setAccount("account1");
        sample.setType( "typeA");
        sample.setBuyQuantity(BigDecimal.valueOf(123.45));
        when(tradeService.getAllTrades()).thenReturn(List.of(sample));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void getList_shouldShowAll() throws Exception {
        Trade b = new Trade();
        b.setAccount("a");
        b.setType( "t");
        b.setBuyQuantity(new BigDecimal("1"));
        b.setId(10);
        given(tradeService.getAllTrades()).willReturn(List.of(b));

        mockMvc.perform(get("/trade/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("trade/list"))
                .andExpect(model().attributeExists("trades"))
                .andExpect(model().attribute("trades", List.of(b)));
    }

    @Test
    @DisplayName("GET /trade/list sans authentification → redirige vers /login")
    void whenNotAuthenticated_thenRedirectToLogin() throws Exception {
        mockMvc.perform(get("/trade/list"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("GET /trade/list avec authentification → affiche la liste")
    void whenAuthenticated_thenShowTrade() throws Exception {
        mockMvc.perform(get("/trade/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("trade/list"))
                .andExpect(model().attributeExists("trades"))
                .andExpect(model().attribute("trades", List.of(sample)));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void getAddForm() throws Exception {
        mockMvc.perform(get("/trade/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("trade/add"))
                .andExpect(model().attributeExists("trade"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("POST /trade/validate valid → redirect to list")
    void postValidate_valid() throws Exception {
        mockMvc.perform(post("/trade/validate")
                        .with(csrf())
                        .param("account", "acc")
                        .param("type", "t")
                        .param("buyQuantity", "12.34")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/trade/list"));

        then(tradeService).should().save(any(Trade.class));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("POST /trade/validate invalid → back to add")
    void postValidate_invalid() throws Exception {
        // champ manquant : buyQuantity
        mockMvc.perform(post("/trade/validate")
                        .with(csrf())
                        .param("account", "tooLongAccountNameExceeding30Chars__________")
                        .param("type", "t")
                        .param("buyQuantity", String.valueOf(new BigDecimal("12.100002")))
                )
                .andExpect(status().isOk())
                .andExpect(view().name("trade/add"))
                .andExpect(model().attributeHasFieldErrors("trade", "account", "buyQuantity"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void getUpdateForm() throws Exception {
        Trade b = new Trade();
        b.setAccount("a");
        b.setType( "t");
        b.setBuyQuantity(new BigDecimal("1"));
        b.setId(5);
        given(tradeService.findById(5)).willReturn(b);

        mockMvc.perform(get("/trade/update/5"))
                .andExpect(status().isOk())
                .andExpect(view().name("trade/update"))
                .andExpect(model().attribute("trade", b));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void postUpdate_valid() throws Exception {
        mockMvc.perform(post("/trade/update/5")
                        .with(csrf())
                        .param("account", "acc")
                        .param("type", "t")
                        .param("buyQuantity", "2.22")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/trade/list"));
        then(tradeService).should().update(eq(5), any(Trade.class));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void deleteBid() throws Exception {
        mockMvc.perform(get("/trade/delete/7"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/trade/list"));
        then(tradeService).should().delete(7);
    }
}
