package me.kiseok.jwtskeleton.domain.oauth2;

import me.kiseok.jwtskeleton.domain.oauth2.dto.GoogleRequest;
import me.kiseok.jwtskeleton.domain.oauth2.dto.GoogleResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
public class GoogleDtoTest {

    @DisplayName("GoogleRequest test")
    @Test
    void test_google_request()  {
        String redirectUri = "testRedirectUri";
        String clientId = "testClientId";
        String clientSecret = "testClientSecret";
        String code = "testCode";
        String responseType = "testResponseType";
        String scope = "testScope";
        String accessType = "testAccessType";
        String grantType = "testGrantType";
        String state = "testState";
        String includeGrantedScopes = "testIncludeGrantedScopes";
        String loginHint = "testLoginHint";
        String prompt = "testPrompt";

        GoogleRequest googleRequest = GoogleRequest.builder()
                .redirectUri(redirectUri)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .code(code)
                .responseType(responseType)
                .scope(scope)
                .accessType(accessType)
                .grantType(grantType)
                .state(state)
                .includeGrantedScopes(includeGrantedScopes)
                .loginHint(loginHint)
                .prompt(prompt)
                .build();

        assertEquals(googleRequest.getRedirectUri(), redirectUri);
        assertEquals(googleRequest.getClientId(), clientId);
        assertEquals(googleRequest.getClientSecret(), clientSecret);
        assertEquals(googleRequest.getCode(), code);
        assertEquals(googleRequest.getResponseType(), responseType);
        assertEquals(googleRequest.getScope(), scope);
        assertEquals(googleRequest.getAccessType(), accessType);
        assertEquals(googleRequest.getGrantType(), grantType);
        assertEquals(googleRequest.getState(), state);
        assertEquals(googleRequest.getIncludeGrantedScopes(), includeGrantedScopes);
        assertEquals(googleRequest.getLoginHint(), loginHint);
        assertEquals(googleRequest.getPrompt(), prompt);
    }

    @DisplayName("GoogleResponse test")
    @Test
    void test_google_response()  {
        String accessToken = "testAccessToken";
        String expiresIn = "testExpiresIn";
        String refreshToken = "testRefreshToken";
        String scope = "testScope";
        String tokenType = "testTokenType";
        String idToken = "testIdToken";

        GoogleResponse googleResponse = new GoogleResponse();
        googleResponse.setAccessToken(accessToken);
        googleResponse.setExpiresIn(expiresIn);
        googleResponse.setRefreshToken(refreshToken);
        googleResponse.setScope(scope);
        googleResponse.setTokenType(tokenType);
        googleResponse.setIdToken(idToken);

        assertEquals(googleResponse.getAccessToken(), accessToken);
        assertEquals(googleResponse.getExpiresIn(), expiresIn);
        assertEquals(googleResponse.getRefreshToken(), refreshToken);
        assertEquals(googleResponse.getScope(), scope);
        assertEquals(googleResponse.getTokenType(), tokenType);
        assertEquals(googleResponse.getIdToken(), idToken);
    }



}
