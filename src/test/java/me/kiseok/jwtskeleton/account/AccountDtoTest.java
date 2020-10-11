package me.kiseok.jwtskeleton.account;

import me.kiseok.jwtskeleton.common.TestProperties;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class AccountDtoTest {

    @Autowired
    TestProperties testProperties;

    @DisplayName("AccountDto @Getter, @Builder 테스트")
    @Test
    void builder_getter_accountDto_test() {
        AccountDto accountDto = AccountDto.builder()
                .email(testProperties.getEmail())
                .password(testProperties.getPassword())
                .name(testProperties.getName())
                .picture(testProperties.getPicture())
                .build();

        assertEquals(accountDto.getEmail(), testProperties.getEmail());
        assertEquals(accountDto.getPassword(), testProperties.getPassword());
        assertEquals(accountDto.getName(), testProperties.getName());
        assertEquals(accountDto.getPicture(), testProperties.getPicture());
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

        accountDto.setEmail(testProperties.getEmail());
        accountDto.setPassword(testProperties.getPassword());
        accountDto.setName(testProperties.getName());
        accountDto.setPicture(testProperties.getPicture());

        assertEquals(accountDto.getEmail(), testProperties.getEmail());
        assertEquals(accountDto.getPassword(), testProperties.getPassword());
        assertEquals(accountDto.getName(), testProperties.getName());
        assertEquals(accountDto.getPicture(), testProperties.getPicture());
    }
}
