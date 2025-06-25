package com.app.poseidon;

import com.app.poseidon.controllers.RatingController;
import com.app.poseidon.domain.Rating;
import com.app.poseidon.security.SecurityConfig;
import com.app.poseidon.services.RatingService;
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

@WebMvcTest(controllers = RatingController.class)
@Import(SecurityConfig.class)
class RatingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RatingService ratingService;

    @MockBean
    private UserService userService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    private Rating sample;


    @BeforeEach
    void setUp() {
        sample = new Rating();
        sample.setMoodysRating("moody");
        sample.setSandPRating("sand");
        sample.setFitchRating("fitch");
        sample.setOrderNumber(5);

        when(ratingService.getAllRatings()).thenReturn(List.of(sample));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void getList_shouldShowAll() throws Exception {
        Rating b = new Rating();
        b.setMoodysRating("moody");
        b.setSandPRating("sand");
        b.setFitchRating("fitch");
        b.setOrderNumber(5);
        b.setId(10);
        given(ratingService.getAllRatings()).willReturn(List.of(b));

        mockMvc.perform(get("/rating/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("rating/list"))
                .andExpect(model().attributeExists("ratings"))
                .andExpect(model().attribute("ratings", List.of(b)));
    }

    @Test
    @DisplayName("GET /rating/list sans authentification → redirige vers /login")
    void whenNotAuthenticated_thenRedirectToLogin() throws Exception {
        mockMvc.perform(get("/rating/list"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("GET /rating/list avec authentification → affiche la liste")
    void whenAuthenticated_thenShowRating() throws Exception {
        mockMvc.perform(get("/rating/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("rating/list"))
                .andExpect(model().attributeExists("ratings"))
                .andExpect(model().attribute("ratings", List.of(sample)));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void getAddForm() throws Exception {
        mockMvc.perform(get("/rating/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("rating/add"))
                .andExpect(model().attributeExists("rating"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("POST /rating/validate valid → redirect to list")
    void postValidate_valid() throws Exception {
        mockMvc.perform(post("/rating/validate")
                        .with(csrf())
                        .param("moodysRating", "moody")
                        .param("sandPRating", "sand")
                        .param("fitchRating", "fitch")
                        .param("orderNumber", "5")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/rating/list"));

        then(ratingService).should().save(any(Rating.class));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("POST /rating/validate invalid → back to add")
    void postValidate_invalid() throws Exception {
        // champ manquant : name
        mockMvc.perform(post("/rating/validate")
                        .with(csrf())
                        .param("moodysRating", "")
                        .param("sandPRating", "")
                        .param("fitchRating", "fitch")
                        .param("orderNumber", "a")
                )
                .andExpect(status().isOk())
                .andExpect(view().name("rating/add"))
                .andExpect(model().attributeHasFieldErrors("rating", "moodysRating", "sandPRating","orderNumber"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void getUpdateForm() throws Exception {
        Rating b = new Rating();
        b.setMoodysRating("moody");
        b.setSandPRating("sand");
        b.setFitchRating("fitch");
        b.setOrderNumber(5);
        b.setId(5);
        given(ratingService.findById(5)).willReturn(b);

        mockMvc.perform(get("/rating/update/5"))
                .andExpect(status().isOk())
                .andExpect(view().name("rating/update"))
                .andExpect(model().attribute("rating", b));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void postUpdate_valid() throws Exception {
        mockMvc.perform(post("/rating/update/5")
                        .with(csrf())
                        .param("moodysRating", "moody")
                        .param("sandPRating", "sand")
                        .param("fitchRating", "fitch")
                        .param("orderNumber", "5")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/rating/list"));
        then(ratingService).should().update(eq(5), any(Rating.class));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void deleteBid() throws Exception {
        mockMvc.perform(get("/rating/delete/7"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/rating/list"));
        then(ratingService).should().delete(7);
    }
}
