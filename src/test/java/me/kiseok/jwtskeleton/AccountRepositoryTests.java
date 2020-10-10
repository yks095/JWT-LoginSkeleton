package me.kiseok.jwtskeleton;

import me.kiseok.jwtskeleton.account.Account;
import me.kiseok.jwtskeleton.account.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class AccountRepositoryTests {

    @Autowired
    AccountRepository accountRepository;

    @Test
    void test_db()  {
        Account account = Account.builder()
                .email("H2@email.com")
                .password("H2Password")
                .build();

        accountRepository.save(account);

        List<Account> all = accountRepository.findAll();
        Account saved = all.get(1);
//        Account saved = accountRepository.findById(2L).get();

        assertThat(all.size()).isEqualTo(2);
        assertThat(saved.getEmail()).isEqualTo("H2@email.com");
        assertThat(saved.getPassword()).isEqualTo("H2Password");
    }
}
