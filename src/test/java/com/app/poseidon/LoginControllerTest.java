//package com.app.poseidon;
//
//import com.app.poseidon.controllers.LoginController;
//import com.app.poseidon.security.SecurityConfig;
//import com.app.poseidon.services.UserService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.mockito.junit.jupiter.MockitoSettings;
//import org.mockito.quality.Strictness;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.context.annotation.Import;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.web.context.WebApplicationContext;
//
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.BDDMockito.given;
//import static org.mockito.Mockito.when;
//import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@WebMvcTest(controllers = LoginController.class)
//@Import(SecurityConfig.class)
//@AutoConfigureMockMvc
//public class LoginControllerTest {
//
//    @Autowired
//    private WebApplicationContext wac;
//    @Autowired MockMvc mockMvc;
//    @MockBean UserService userService;    // chargé par DaoAuthenticationProvider
//    @MockBean PasswordEncoder passwordEncoder;
//
//    @BeforeEach
//    void setup() {
//        UserDetails joe = User.builder()
//                .username("joe")
//                .password(passwordEncoder.encode("test"))
//                .roles("USER")
//                .build();
//        when(userService.loadUserByUsername("joe"))
//                .thenReturn(joe);
//    }
//
//    @BeforeEach
//    void setup(MockMvc mockMvc) {
//        this.mockMvc = MockMvcBuilders
//                .webAppContextSetup(wac)
//                .apply(springSecurity())      // <-- nécessaire pour activer formLogin()
//                .build();
//    }
//
//    @Test
//    public void displayLoginPage() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.get("/login")).andExpect(status().isOk()).andDo(print());
//    }
//
//    @Test
//    void loginWithValidCredentials() throws Exception {
//        mockMvc.perform(formLogin("/login")
//                        .user("joe").password("test")
//                )
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/bidList/list"));
//    }
//
//    @Test
//    void whenAlreadyAuthenticated_thenRedirectToBidList() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.get("/login")
//                        // on simule un user déjà loggé
//                        .with(user("joe").roles("USER")))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/bidList/list"));
//    }
//
//    @Test
//    @DisplayName("POST /connexion avec mauvais identifiants → redirige vers /login?error")
//    void loginWithInvalidCredentials() throws Exception {
//        // on simule un échec d'authentification
//        given(userService.loadUserByUsername(anyString()))
//                .willThrow(new UsernameNotFoundException("not found"));
//
//        mockMvc.perform(post("/login")
//                        .param("username", "inconnu")
//                        .param("password", "whatever")
//                        .with(csrf()))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/login?error"));
//    }
//}
