package com.app.poseidon;

import com.app.poseidon.controllers.CurveController;
import com.app.poseidon.domain.CurvePoint;
import com.app.poseidon.security.SecurityConfig;
import com.app.poseidon.services.CurveService;
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

@WebMvcTest(controllers = CurveController.class)
@Import(SecurityConfig.class)
class CurveControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CurveService curveService;

    @MockBean
    private UserService userService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    private CurvePoint sample;


    @BeforeEach
    void setUp() {
        sample = new CurvePoint();
        sample.setTerm(BigDecimal.valueOf(120.45));
        sample.setValue(BigDecimal.valueOf(10.50));
        when(curveService.getAllCurvePoints()).thenReturn(List.of(sample));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void getList_shouldShowAll() throws Exception {
        CurvePoint b = new CurvePoint();
        sample.setTerm(BigDecimal.valueOf(50.45));
        sample.setValue(BigDecimal.valueOf(60.50));
        b.setId(10);
        given(curveService.getAllCurvePoints()).willReturn(List.of(b));

        mockMvc.perform(get("/curvePoint/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/list"))
                .andExpect(model().attributeExists("curvePoints"))
                .andExpect(model().attribute("curvePoints", List.of(b)));
    }

    @Test
    @DisplayName("GET /curvePoint/list sans authentification → redirige vers /login")
    void whenNotAuthenticated_thenRedirectToLogin() throws Exception {
        mockMvc.perform(get("/curvePoint/list"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("GET /curvePoint/list avec authentification → affiche la liste")
    void whenAuthenticated_thenShowCurve() throws Exception {
        mockMvc.perform(get("/curvePoint/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/list"))
                .andExpect(model().attributeExists("curvePoints"))
                .andExpect(model().attribute("curvePoints", List.of(sample)));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void getAddForm() throws Exception {
        mockMvc.perform(get("/curvePoint/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/add"))
                .andExpect(model().attributeExists("curvePoint"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("POST /curvePoint/validate valid → redirect to list")
    void postValidate_valid() throws Exception {
        mockMvc.perform(post("/curvePoint/validate")
                        .with(csrf())
                        .param("term", "120.45")
                        .param("value", "10.50")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/curvePoint/list"));

        then(curveService).should().save(any(CurvePoint.class));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("POST /curvePoint/validate invalid → back to add")
    void postValidate_invalid() throws Exception {
        // champ manquant : buyQuantity
        mockMvc.perform(post("/curvePoint/validate")
                        .with(csrf())
                        .param("term", "2272.277727")
                        .param("value", "t")
                )
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/add"))
                .andExpect(model().attributeHasFieldErrors("curvePoint", "term", "value"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void getUpdateForm() throws Exception {
        CurvePoint b = new CurvePoint();
        sample.setTerm(BigDecimal.valueOf(50.45));
        sample.setValue(BigDecimal.valueOf(60.50));
        b.setId(5);
        given(curveService.findById(5)).willReturn(b);

        mockMvc.perform(get("/curvePoint/update/5"))
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/update"))
                .andExpect(model().attribute("curvePoint", b));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void postUpdate_valid() throws Exception {
        mockMvc.perform(post("/curvePoint/update/5")
                        .with(csrf())
                        .param("term", "50.45")
                        .param("value", "60.50")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/curvePoint/list"));
        then(curveService).should().update(eq(5), any(CurvePoint.class));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void deleteBid() throws Exception {
        mockMvc.perform(get("/curvePoint/delete/7"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/curvePoint/list"));
        then(curveService).should().delete(7);
    }
}
