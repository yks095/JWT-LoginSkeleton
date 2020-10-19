package me.kiseok.jwtskeleton.domain.account;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.kiseok.jwtskeleton.config.jwt.JwtProvider;
import me.kiseok.jwtskeleton.domain.account.dto.AccountRequest;
import me.kiseok.jwtskeleton.domain.account.dto.AccountResponse;
import me.kiseok.jwtskeleton.domain.auth.dto.LoginRequest;
import me.kiseok.jwtskeleton.domain.auth.dto.LoginResponse;
import me.kiseok.jwtskeleton.domain.token.TokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AccountControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    TokenRepository tokenRepository;

    @BeforeEach
    void setUp()    {
        accountRepository.deleteAll();
        tokenRepository.deleteAll();
    }

    @DisplayName("유저 저장 유효성 검사 실패 -> 400 BAD_REQUEST")
    @ParameterizedTest(name = "{index}) email={0}, password={1}")
    @MethodSource("validSaveAccount")
    void save_account_invalid_400(String email, String password) throws Exception    {
        AccountRequest requestDto = AccountRequest.builder()
                .email(email)
                .password(password)
                .build();

        mockMvc.perform(post("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
        ;
    }

    @DisplayName("유저 저장 성공 -> 201 CREATED")
    @Test
    void save_account_201() throws Exception    {
        String email = "test@email.com";
        String password = "testPassword";

        AccountRequest requestDto = AccountRequest.builder()
                .email(email)
                .password(password)
                .build();

        ResultActions actions = mockMvc.perform(post("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("email").value(email))
                .andExpect(jsonPath("roles").exists())
                ;

        String contentAsString = actions.andReturn().getResponse().getContentAsString();
        AccountResponse responseDto = objectMapper.readValue(contentAsString, AccountResponse.class);
        Account saved = accountRepository.findByEmail(responseDto.getEmail()).get();

        assertEquals(responseDto.getEmail(), email);
        assertEquals(saved.getEmail(), email);
        assertTrue(passwordEncoder.matches(password, saved.getPassword()));
    }

    @DisplayName("DB에 없는 유저 불러오기 -> 404 NOT_FOUND")
    @Test
    void load_account_with_none_exist_id_404() throws Exception {
        String email = "test@email.com";
        String password = "testPassword";

        AccountRequest requestDto = AccountRequest.builder()
                .email(email)
                .password(password)
                .build();

        ResultActions actions = mockMvc.perform(post("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("email").value(email));

        String contentAsString = actions.andReturn().getResponse().getContentAsString();
        AccountResponse responseDto = objectMapper.readValue(contentAsString, AccountResponse.class);
        LoginRequest loginRequest = LoginRequest.builder()
                .email(responseDto.getEmail())
                .password(requestDto.getPassword())
                .build();

        String jwt = loginAccount(loginRequest);

        mockMvc.perform(get("/api/accounts/-1")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, jwt))
                .andDo(print())
                .andExpect(status().isNotFound())
        ;
    }

    @DisplayName("비로그인 유저가 유저 정보를 불러오기 -> 401 UNAUTHORIZED")
    @Test
    void load_account_unauthorized_401() throws Exception   {
        String email = "test@email.com";
        String password = "testPassword";

        AccountRequest requestDto = AccountRequest.builder()
                .email(email)
                .password(password)
                .build();

        ResultActions actions = mockMvc.perform(post("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("email").value(email))
                ;

        String contentAsString = actions.andReturn().getResponse().getContentAsString();
        AccountResponse responseDto = objectMapper.readValue(contentAsString, AccountResponse.class);

        mockMvc.perform(get("/api/accounts/" + responseDto.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized())
        ;

    }

    @DisplayName("정상적으로 유저 불러오기 -> 200 OK")
    @Test
    void load_account_200() throws Exception    {
        String email = "test@email.com";
        String password = "testPassword";

        AccountRequest requestDto = AccountRequest.builder()
                .email(email)
                .password(password)
                .build();

        ResultActions actions = mockMvc.perform(post("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("email").value(email))
                ;

        String contentAsString = actions.andReturn().getResponse().getContentAsString();
        AccountResponse responseDto = objectMapper.readValue(contentAsString, AccountResponse.class);
        LoginRequest loginRequest = LoginRequest.builder()
                .email(responseDto.getEmail())
                .password(requestDto.getPassword())
                .build();

        String jwt = loginAccount(loginRequest);

        mockMvc.perform(get("/api/accounts/" + responseDto.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, jwt))
                .andDo(print())
                .andExpect(status().isOk())
        ;
    }

    private String loginAccount(LoginRequest request) throws Exception {
        ResultActions actions = mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                ;

        String contentAsString = actions.andReturn().getResponse().getContentAsString();
        LoginResponse responseDto = objectMapper.readValue(contentAsString, LoginResponse.class);

        return "Bearer " + responseDto.getAccessToken();
    }

    private static Stream<Arguments> validSaveAccount() {
        return Stream.of(
                Arguments.of(null, null),
                Arguments.of("null", null),
                Arguments.of(null, "null"),
                Arguments.of("", ""),
                Arguments.of(" ", ""),
                Arguments.of("", " "),
                Arguments.of(" ", " "),
                Arguments.of("", "testPassword"),
                Arguments.of(" ", "testPassword"),
                Arguments.of("test", "testPassword"),
                Arguments.of("test@", "testPassword"),
                Arguments.of("test@.com", "testPassword"),
                Arguments.of("@", "testPassword"),
                Arguments.of("@.", "testPassword"),
                Arguments.of("@email", "testPassword"),
                Arguments.of("@email.", "testPassword"),
                Arguments.of(".com", "testPassword"),
                Arguments.of("test@email.com", ""),
                Arguments.of("test@email.com", " ")
        );
    }
}