package me.kiseok.jwtskeleton.account;

import me.kiseok.jwtskeleton.common.TestProperties;
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
    TestProperties testProperties;

    @Autowired
    AccountRepository accountRepository;

    private static Account save;

    @BeforeEach
    void setUp()    {
        accountRepository.deleteAll();

        Account account = Account.builder()
                .email(testProperties.getEmail())
                .password(testProperties.getPassword())
                .name(testProperties.getName())
                .picture(testProperties.getPicture())
                .build();

        save = accountRepository.save(account);

    }

    @DisplayName("findById 테스트")
    @Test
    void test_find_by_id()  {
        Optional<Account> optionalAccount = accountRepository.findById(save.getId());
        Account savedAccount = optionalAccount.get();

        assertNotNull(optionalAccount);
        assertEquals(savedAccount.getEmail(), testProperties.getEmail());
        assertEquals(savedAccount.getPassword(), testProperties.getPassword());
        assertEquals(savedAccount.getName(), testProperties.getName());
        assertEquals(savedAccount.getPicture(), testProperties.getPicture());
    }

    @DisplayName("findByEmail 테스트")
    @Test
    void test_find_by_email()  {
       Optional<Account> optionalAccount = accountRepository.findByEmail(save.getEmail());
        Account savedAccount = optionalAccount.get();

        assertNotNull(optionalAccount);
        assertEquals(savedAccount.getEmail(), testProperties.getEmail());
        assertEquals(savedAccount.getPassword(), testProperties.getPassword());
        assertEquals(savedAccount.getName(), testProperties.getName());
        assertEquals(savedAccount.getPicture(), testProperties.getPicture());
    }

}
