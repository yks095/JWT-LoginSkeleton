package me.kiseok.jwtskeleton.domain.login;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.kiseok.jwtskeleton.domain.account.AccountRepository;
import me.kiseok.jwtskeleton.domain.account.dto.AccountRequestDto;
import me.kiseok.jwtskeleton.domain.login.dto.LoginRequestDto;
import me.kiseok.jwtskeleton.domain.login.dto.LoginResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class LoginControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    AccountRepository accountRepository;

    static LoginRequestDto requestDto;

    @BeforeEach
    void setUp() throws Exception {
        accountRepository.deleteAll();

        String email = "test@email.com";
        String password = "testPassword";

        AccountRequestDto accountRequestDto = AccountRequestDto.builder()
                .email(email)
                .password(password)
                .build();

        mockMvc.perform(post("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(accountRequestDto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("email").value(email))
        ;

        requestDto = LoginRequestDto.builder()
                .email(email)
                .password(password)
                .build();
    }

    @DisplayName("로그인 실패 by email")
    @Test
    void login_fail_by_email() throws Exception {
        String wrongEmail = "wrong@email.com";

        requestDto.setEmail(wrongEmail);
        mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andDo(print())
                .andExpect(status().isNotFound())
        ;
    }

    @DisplayName("로그인 실패 by password")
    @Test
    void login_fail_by_password() throws Exception  {
        String wrongPassword = "wrongPassword";

        requestDto.setPassword(wrongPassword);
        mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
        ;
    }

    @DisplayName("로그인 성공 -> 200 OK")
    @Test
    void login_200() throws Exception   {
        ResultActions actions = mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andDo(print())
                .andExpect(status().isOk())
                ;

        String contentAsString = actions.andReturn().getResponse().getContentAsString();
        LoginResponseDto responseDto = objectMapper.readValue(contentAsString, LoginResponseDto.class);

        assertFalse(responseDto.getJwt().isEmpty());
    }

}
