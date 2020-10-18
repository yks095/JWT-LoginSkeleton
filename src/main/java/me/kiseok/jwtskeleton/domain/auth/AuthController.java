package me.kiseok.jwtskeleton.domain.auth;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import lombok.RequiredArgsConstructor;
import me.kiseok.jwtskeleton.config.jwt.JwtProvider;
import me.kiseok.jwtskeleton.domain.account.Account;
import me.kiseok.jwtskeleton.domain.account.AccountAdapter;
import me.kiseok.jwtskeleton.domain.account.AccountRepository;
import me.kiseok.jwtskeleton.domain.auth.dto.GoogleRequest;
import me.kiseok.jwtskeleton.domain.auth.dto.GoogleResponse;
import me.kiseok.jwtskeleton.domain.login.dto.LoginResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import java.util.Collections;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class AuthController {

    private final JwtProvider jwtProvider;
    private final AccountRepository accountRepository;

    private static final String GOOGLE_TOKEN_BASE_URL = "https://oauth2.googleapis.com/token";
    private static final String GOOGLE_TOKEN_INFO_URL = "https://oauth2.googleapis.com/tokeninfo";
    private static final String GOOGLE_REDIRECT_URL = "http://localhost:8080/login/google/auth";

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String CLIENT_ID;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String CLIENT_SECRET;

    @GetMapping("/login/google/auth")
    ResponseEntity<?> googleAuth(@RequestParam(value = "code") String authCode) throws JsonProcessingException {

        // HTTP Request를 위한 RestTemplate
        RestTemplate restTemplate = new RestTemplate();

        // Google OAuth Access Token 요청을 위한 파라미터 세팅
        GoogleRequest googleRequest = createGoogleRequest(authCode);

        // JSON 파싱을 위한 기본값 세팅
        ObjectMapper mapper = new ObjectMapper();
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        mapper.setSerializationInclusion(Include.NON_NULL);

        // AccessToken 발급 요청
        ResponseEntity<String> resultEntity = restTemplate.postForEntity(GOOGLE_TOKEN_BASE_URL, googleRequest, String.class);

        // Token Request
        GoogleResponse googleResponse = mapper.readValue(resultEntity.getBody(), new TypeReference<GoogleResponse>() {
        });

        // ID Token만 추출 (사용자의 정보는 jwt로 인코딩 되어있다.)
        String jwt = googleResponse.getIdToken();
        String requestURL = UriComponentsBuilder.fromHttpUrl(GOOGLE_TOKEN_INFO_URL)
                .queryParam("id_token", jwt)
                .encode()
                .toUriString();

        String responseJson = restTemplate.getForObject(requestURL, String.class);
        Map<String, String> userInfo = mapper.readValue(responseJson, new TypeReference<Map<String, String>>() {});

        jwt = createServerJwt(userInfo);

        return new ResponseEntity<>(new LoginResponseDto(jwt), HttpStatus.OK);
    }

    private GoogleRequest createGoogleRequest(String authCode) {
        return GoogleRequest.builder()
                .clientId(CLIENT_ID)
                .clientSecret(CLIENT_SECRET)
                .code(authCode)
                .redirectUri(GOOGLE_REDIRECT_URL)
                .grantType("authorization_code")
                .build();
    }

    private AccountAdapter createAccount(Map<String, String> userInfo) {
        String email = userInfo.get("email");
        String name = userInfo.get("name");
        String picture = userInfo.get("picture");

        Account account = Account.builder()
                .email(email)
                .name(name)
                .picture(picture)
                .roles(Collections.singletonList("ROLE_USER"))
                .build();

        accountRepository.save(account);

        return new AccountAdapter(account);
    }

    private String createServerJwt(Map<String, String> userInfo) {
        AccountAdapter accountAdapter = createAccount(userInfo);
        return "Bearer " + jwtProvider.generateJwt(accountAdapter.getUsername(), accountAdapter.getAccount().getRoles());
    }
}
