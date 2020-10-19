package me.kiseok.jwtskeleton.domain.account;

import me.kiseok.jwtskeleton.domain.account.dto.AccountRequest;
import me.kiseok.jwtskeleton.domain.account.dto.AccountResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
class AccountDtoTest {

    @DisplayName("AccountRequestDto @Getter, @Builder 테스트")
    @Test
    void builder_getter_account_requestDto_test() {
        String email = "test@email.com";
        String password = "testPassword";
        String name = "testName";
        String picture = "testPicture";

        AccountRequest requestDto = AccountRequest.builder()
                .email(email)
                .password(password)
                .name(name)
                .picture(picture)
                .build();

        assertEquals(requestDto.getEmail(), email);
        assertEquals(requestDto.getPassword(), password);
        assertEquals(requestDto.getName(), name);
        assertEquals(requestDto.getPicture(), picture);
    }

    @DisplayName("AccountRequestDto @Setter 테스트")
    @Test
    void setter_account_requestDto()    {
        AccountRequest requestDto = AccountRequest.builder()
                .email("email")
                .password("password")
                .name("name")
                .picture("picture")
                .build();

        String email = "test@email.com";
        String password = "testPassword";
        String name = "testName";
        String picture = "testPicture";

        requestDto.setEmail(email);
        requestDto.setPassword(password);
        requestDto.setName(name);
        requestDto.setPicture(picture);

        assertEquals(requestDto.getEmail(), email);
        assertEquals(requestDto.getPassword(), password);
        assertEquals(requestDto.getName(), name);
        assertEquals(requestDto.getPicture(), picture);
    }

    @DisplayName("AccountResponseDto @Getter, @Builder 테스트")
    @Test
    void builder_getter_account_responseDto_test() {
        String email = "test@email.com";
        String name = "testName";
        String picture = "testPicture";

        AccountResponse responseDto = AccountResponse.builder()
                .email(email)
                .name(name)
                .picture(picture)
                .build();

        assertEquals(responseDto.getEmail(), email);
        assertEquals(responseDto.getName(), name);
        assertEquals(responseDto.getPicture(), picture);
    }

    @DisplayName("AccountResponseDto @Setter 테스트")
    @Test
    void setter_account_responseDto()    {
        AccountResponse responseDto = AccountResponse.builder()
                .email("email")
                .name("name")
                .picture("picture")
                .build();

        String email = "test@email.com";
        String name = "testName";
        String picture = "testPicture";

        responseDto.setEmail(email);
        responseDto.setName(name);
        responseDto.setPicture(picture);

        assertEquals(responseDto.getEmail(), email);
        assertEquals(responseDto.getName(), name);
        assertEquals(responseDto.getPicture(), picture);
    }
}
