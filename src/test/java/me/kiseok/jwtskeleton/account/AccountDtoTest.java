package me.kiseok.jwtskeleton.account;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AccountDtoTest {

    @DisplayName("AccountDto @Getter, @Builder 테스트")
    @Test
    void builder_getter_accountDto_test() {
        String email = "test@email.com";
        String password = "testPassword";
        String name = "testName";
        String picture = "testPicture";

        AccountDto accountDto = AccountDto.builder()
                .email(email)
                .password(password)
                .name(name)
                .picture(picture)
                .build();

        assertEquals(accountDto.getEmail(), email);
        assertEquals(accountDto.getPassword(), password);
        assertEquals(accountDto.getName(), name);
        assertEquals(accountDto.getPicture(), picture);
    }

    @DisplayName("AccountDto @Setter 테스트")
    @Test
    void setter_accountDto()    {
        AccountDto accountDto = AccountDto.builder()
                .email("email")
                .password("password")
                .name("name")
                .picture("picture")
                .build();

        String email = "test@email.com";
        String password = "testPassword";
        String name = "testName";
        String picture = "testPicture";

        accountDto.setEmail(email);
        accountDto.setPassword(password);
        accountDto.setName(name);
        accountDto.setPicture(picture);

        assertEquals(accountDto.getEmail(), email);
        assertEquals(accountDto.getPassword(), password);
        assertEquals(accountDto.getName(), name);
        assertEquals(accountDto.getPicture(), picture);
    }
}
