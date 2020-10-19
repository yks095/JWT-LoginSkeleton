package me.kiseok.jwtskeleton.domain.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.kiseok.jwtskeleton.domain.account.Account;
import me.kiseok.jwtskeleton.domain.account.AccountRepository;
import me.kiseok.jwtskeleton.domain.account.dto.AccountRequest;
import me.kiseok.jwtskeleton.domain.auth.dto.LoginRequest;
import me.kiseok.jwtskeleton.domain.auth.dto.LoginResponse;
import me.kiseok.jwtskeleton.domain.token.TokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class LogoutControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    TokenRepository tokenRepository;

    @Autowired
    ObjectMapper objectMapper;

    private static String jwt;

    @BeforeEach
    void setUp() throws Exception {
        accountRepository.deleteAll();
        tokenRepository.deleteAll();

        String email = "test@email.com";
        String password = "testPassword";
        String name = "testName";
        String picture = "testPicture";

        AccountRequest accountRequest = AccountRequest.builder()
                .email(email)
                .password(password)
                .name(name)
                .picture(picture)
                .build();

        mockMvc.perform(post("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(accountRequest)))
                .andExpect(status().isCreated())
        ;

        LoginRequest loginRequest = LoginRequest.builder()
                .email(email)
                .password(password)
                .build();

        ResultActions resultActions = mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                ;

        String contentAsString = resultActions.andReturn().getResponse().getContentAsString();
        LoginResponse loginResponse = objectMapper.readValue(contentAsString, LoginResponse.class);

        jwt = "Bearer " + loginResponse.getAccessToken();
    }

    @DisplayName("로그아웃 후, 재로그아웃 요청시 -> 401 UNAUTHORIZED")
    @Test
    void logout_401() throws Exception  {
        mockMvc.perform(post("/api/logout")
                .header(HttpHeaders.AUTHORIZATION, jwt))
                .andExpect(status().isOk())
        ;

        mockMvc.perform(post("/api/logout")
                .header(HttpHeaders.AUTHORIZATION, jwt))
                .andDo(print())
                .andExpect(status().isUnauthorized())
        ;
    }

    @DisplayName("로그아웃 성공 -> 200 OK")
    @Test
    void logout_200() throws Exception  {
        mockMvc.perform(post("/api/logout")
                .header(HttpHeaders.AUTHORIZATION, jwt))
                .andDo(print())
                .andExpect(status().isOk())
        ;
    }

    @DisplayName("setAuthentication(null) test")
    @Test
    void test_authentication_null() throws Exception    {
        String email = "test2@email.com";
        String password = "testPassword2";
        String name = "testName2";
        String picture = "testPicture2";

        AccountRequest accountRequest = AccountRequest.builder()
                .email(email)
                .password(password)
                .name(name)
                .picture(picture)
                .build();

        mockMvc.perform(post("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(accountRequest)))
                .andExpect(status().isCreated())
        ;

        LoginRequest loginRequest = LoginRequest.builder()
                .email(email)
                .password(password)
                .build();

        ResultActions resultActions = mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                ;

        String contentAsString = resultActions.andReturn().getResponse().getContentAsString();
        LoginResponse loginResponse = objectMapper.readValue(contentAsString, LoginResponse.class);
        String jwt2 = "Bearer " + loginResponse.getAccessToken();

        mockMvc.perform(post("/api/logout")
                .header(HttpHeaders.AUTHORIZATION, jwt))
                .andExpect(status().isOk())
        ;

        Account account = accountRepository.findByEmail(email).get();
        mockMvc.perform(get("/api/accounts/" + account.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, jwt2))
                .andDo(print())
                .andExpect(status().isOk())
        ;
    }
}
