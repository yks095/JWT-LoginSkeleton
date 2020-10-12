package me.kiseok.jwtskeleton.domain.login;

import me.kiseok.jwtskeleton.domain.login.dto.LoginRequestDto;
import me.kiseok.jwtskeleton.domain.login.dto.LoginResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class LoginDtoTest {

    @DisplayName("LoginRequestDto @Getter @Builder 테스트")
    @Test
    void builder_getter_login_requestDto_test() {
        String email = "test@email.com";
        String password = "testPassword";

        LoginRequestDto requestDto = LoginRequestDto.builder()
                .email(email)
                .password(password)
                .build();

        assertEquals(requestDto.getEmail(), email);
        assertEquals(requestDto.getPassword(), password);
    }

    @DisplayName("LoginRequestDto @Setter 테스트")
    @Test
    void setter_login_requestDto_test() {
        LoginRequestDto requestDto = LoginRequestDto.builder()
                .email("email")
                .password("password")
                .build();

        String email = "test@email.com";
        String password = "testPassword";

        requestDto.setEmail(email);
        requestDto.setPassword(password);

        assertEquals(requestDto.getEmail(), email);
        assertEquals(requestDto.getPassword(), password);
    }

    @DisplayName("LoginResponseDto @Getter @Setter 테스트")
    @Test
    void getter_setter_login_responseDto_test() {
        String jwt = "testJwt";
        LoginResponseDto responseDto = new LoginResponseDto();

        responseDto.setJwt(jwt);

        assertEquals(responseDto.getJwt(), jwt);
    }

}
