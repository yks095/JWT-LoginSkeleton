package me.kiseok.jwtskeleton;

import lombok.RequiredArgsConstructor;
import me.kiseok.jwtskeleton.account.Account;
import me.kiseok.jwtskeleton.account.AccountRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AppRunner implements CommandLineRunner {

    private final AccountRepository accountRepository;

    @Override
    public void run(String... args) throws Exception {
        Account account = Account.builder()
                .email("AppRunner@email.com")
                .password("AppRunnerPassword")
                .build();

        accountRepository.save(account);
    }
}
