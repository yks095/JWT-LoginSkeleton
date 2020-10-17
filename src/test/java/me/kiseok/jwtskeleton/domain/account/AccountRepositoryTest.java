package me.kiseok.jwtskeleton.domain.account;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class AccountRepositoryTest {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AccountRepository accountRepository;

    @BeforeEach
    void setUp()    {
        accountRepository.deleteAll();
    }

    @DisplayName("findById 테스트")
    @Test
    void test_find_by_id()  {
        String email = "test@email.com";
        String password = "testPassword";
        String name = "testName";
        String picture = "testPicture";

        Account requestDto = Account.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .name(name)
                .picture(picture)
                .build();

        Account save = accountRepository.save(requestDto);

        Optional<Account> optionalAccount = accountRepository.findById(save.getId());
        Account savedAccount = optionalAccount.get();

        assertNotNull(optionalAccount);
        assertEquals(savedAccount.getEmail(), email);
        assertTrue(passwordEncoder.matches(password, savedAccount.getPassword()));
        assertEquals(savedAccount.getName(), name);
        assertEquals(savedAccount.getPicture(), picture);
    }

    @DisplayName("findByEmail 테스트")
    @Test
    void test_find_by_email()  {
        String email = "test@email.com";
        String password = "testPassword";
        String name = "testName";
        String picture = "testPicture";

        Account requestDto = Account.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .name(name)
                .picture(picture)
                .build();

        Account save = accountRepository.save(requestDto);

        Optional<Account> optionalAccount = accountRepository.findByEmail(save.getEmail());
        Account savedAccount = optionalAccount.get();

        assertNotNull(optionalAccount);
        assertEquals(savedAccount.getEmail(), email);
        assertTrue(passwordEncoder.matches(password, savedAccount.getPassword()));
        assertEquals(savedAccount.getName(), name);
        assertEquals(savedAccount.getPicture(), picture);
    }

}
