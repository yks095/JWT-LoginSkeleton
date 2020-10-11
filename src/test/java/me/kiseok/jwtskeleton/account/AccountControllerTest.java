package me.kiseok.jwtskeleton.account;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.kiseok.jwtskeleton.common.TestProperties;
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
import org.springframework.http.MediaType;
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
class AccountControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    TestProperties testProperties;

    @Autowired
    AccountRepository accountRepository;

    @BeforeEach
    void setUp()    {
        accountRepository.deleteAll();
    }

    @DisplayName("유저 저장 유효성 검사 실패 -> 400 BAD_REQUEST")
    @ParameterizedTest(name = "{index}) email={0}, password={1}")
    @MethodSource("validSaveAccount")
    void save_account_invalid_400(String email, String password) throws Exception    {
        AccountDto accountDto = AccountDto.builder()
                .email(email)
                .password(password)
                .name(testProperties.getName())
                .picture(testProperties.getPicture())
                .build();

        mockMvc.perform(post("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(accountDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
        ;
    }

    @DisplayName("유저 저장 성공 -> 201 CREATED")
    @Test
    void save_account_201() throws Exception    {
        AccountDto accountDto = AccountDto.builder()
                .email(testProperties.getEmail())
                .password(testProperties.getPassword())
                .build();

        ResultActions actions = mockMvc.perform(post("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(accountDto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("email").value(testProperties.getEmail()))
                .andExpect(jsonPath("password").value(testProperties.getPassword()))
                ;

        String contentAsString = actions.andReturn().getResponse().getContentAsString();
        AccountDto response = objectMapper.readValue(contentAsString, AccountDto.class);
        Account saved = accountRepository.findByEmail(response.getEmail()).get();

        assertEquals(response.getEmail(), testProperties.getEmail());
        assertEquals(response.getPassword(), testProperties.getPassword());

        assertEquals(saved.getEmail(), testProperties.getEmail());
        assertEquals(saved.getPassword(), testProperties.getPassword());
    }

    @DisplayName("DB에 없는 유저 불러오기 -> 404 NOT_FOUND")
    @Test
    void load_account_with_none_exist_id_404() throws Exception {
        mockMvc.perform(get("/api/accounts/-1")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
        ;
    }

    @DisplayName("정상적으로 유저 불러오기 -> 200 OK")
    @Test
    void load_account_200() throws Exception    {
        AccountDto accountDto = AccountDto.builder()
                .email(testProperties.getEmail())
                .password(testProperties.getPassword())
                .build();

        ResultActions actions = mockMvc.perform(post("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(accountDto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("email").value(testProperties.getEmail()))
                .andExpect(jsonPath("password").value(testProperties.getPassword()))
                ;

        String contentAsString = actions.andReturn().getResponse().getContentAsString();
        Account response = objectMapper.readValue(contentAsString, Account.class);

        mockMvc.perform(get("/api/accounts/" + response.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
        ;
    }

    private static Stream<Arguments> validSaveAccount() {
        return Stream.of(
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