package com.app.poseidon;

import com.app.poseidon.controllers.RuleNameController;
import com.app.poseidon.domain.RuleName;
import com.app.poseidon.security.SecurityConfig;
import com.app.poseidon.services.RuleNameService;
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

@WebMvcTest(controllers = RuleNameController.class)
@Import(SecurityConfig.class)
class RuleNameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RuleNameService ruleNameService;

    @MockBean
    private UserService userService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    private RuleName sample;


    @BeforeEach
    void setUp() {
        sample = new RuleName();
        sample.setName("name");
        sample.setDescription("desc");
        sample.setJson("json");
        sample.setTemplate("temp");
        sample.setSqlStr("sqlstr");
        sample.setSqlPart("sqlpart");
        when(ruleNameService.getAllRuleName()).thenReturn(List.of(sample));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void getList_shouldShowAll() throws Exception {
        RuleName b = new RuleName();
        b.setName("name");
        b.setDescription("desc");
        b.setJson("json");
        b.setTemplate("temp");
        b.setSqlStr("sqlstr");
        b.setSqlPart("sqlpart");
        b.setId(10);
        given(ruleNameService.getAllRuleName()).willReturn(List.of(b));

        mockMvc.perform(get("/ruleName/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("ruleName/list"))
                .andExpect(model().attributeExists("ruleNames"))
                .andExpect(model().attribute("ruleNames", List.of(b)));
    }

    @Test
    @DisplayName("GET /ruleName/list sans authentification → redirige vers /login")
    void whenNotAuthenticated_thenRedirectToLogin() throws Exception {
        mockMvc.perform(get("/ruleName/list"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("GET /ruleName/list avec authentification → affiche la liste")
    void whenAuthenticated_thenShowRuleName() throws Exception {
        mockMvc.perform(get("/ruleName/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("ruleName/list"))
                .andExpect(model().attributeExists("ruleNames"))
                .andExpect(model().attribute("ruleNames", List.of(sample)));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void getAddForm() throws Exception {
        mockMvc.perform(get("/ruleName/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("ruleName/add"))
                .andExpect(model().attributeExists("ruleName"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("POST /ruleName/validate valid → redirect to list")
    void postValidate_valid() throws Exception {
        mockMvc.perform(post("/ruleName/validate")
                        .with(csrf())
                        .param("name", "name")
                        .param("description", "desc")
                        .param("json", "json")
                        .param("template", "temp")
                        .param("sqlStr", "sqlstr")
                        .param("sqlPart", "sqlpart")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ruleName/list"));

        then(ruleNameService).should().save(any(RuleName.class));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("POST /ruleName/validate invalid → back to add")
    void postValidate_invalid() throws Exception {
        // champ manquant : name
        mockMvc.perform(post("/ruleName/validate")
                        .with(csrf())
                        .param("name", "")
                        .param("description", "")
                        .param("json", "json")
                        .param("template", "temp")
                        .param("sqlStr", "sqlstr")
                        .param("sqlPart", "sqlpart")
                )
                .andExpect(status().isOk())
                .andExpect(view().name("ruleName/add"))
                .andExpect(model().attributeHasFieldErrors("ruleName", "name", "description"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void getUpdateForm() throws Exception {
        RuleName b = new RuleName();
        b.setName("nameNew");
        b.setDescription("descNew");
        b.setJson("jsonNew");
        b.setTemplate("tempNew");
        b.setSqlStr("sqlstrNew");
        b.setSqlPart("sqlpartNew");
        b.setId(5);
        given(ruleNameService.findById(5)).willReturn(b);

        mockMvc.perform(get("/ruleName/update/5"))
                .andExpect(status().isOk())
                .andExpect(view().name("ruleName/update"))
                .andExpect(model().attribute("ruleName", b));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void postUpdate_valid() throws Exception {
        mockMvc.perform(post("/ruleName/update/5")
                        .with(csrf())
                        .param("name", "name")
                        .param("description", "desc")
                        .param("json", "json")
                        .param("template", "temp")
                        .param("sqlStr", "sqlstr")
                        .param("sqlPart", "sqlpart")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ruleName/list"));
        then(ruleNameService).should().update(eq(5), any(RuleName.class));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void deleteBid() throws Exception {
        mockMvc.perform(get("/ruleName/delete/7"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ruleName/list"));
        then(ruleNameService).should().delete(7);
    }
}
