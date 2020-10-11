package me.kiseok.jwtskeleton.account;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class AccountRepositoryTest {

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

        Account account = Account.builder()
                .email(email)
                .password(password)
                .name(name)
                .picture(picture)
                .build();

        Account save = accountRepository.save(account);

        Optional<Account> optionalAccount = accountRepository.findById(save.getId());
        Account savedAccount = optionalAccount.get();

        assertNotNull(optionalAccount);
        assertEquals(savedAccount.getEmail(), email);
        assertEquals(savedAccount.getPassword(), password);
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

        Account account = Account.builder()
                .email(email)
                .password(password)
                .name(name)
                .picture(picture)
                .build();

        Account save = accountRepository.save(account);

        Optional<Account> optionalAccount = accountRepository.findByEmail(save.getEmail());
        Account savedAccount = optionalAccount.get();

        assertNotNull(optionalAccount);
        assertEquals(savedAccount.getEmail(), email);
        assertEquals(savedAccount.getPassword(), password);
        assertEquals(savedAccount.getName(), name);
        assertEquals(savedAccount.getPicture(), picture);
    }

}
