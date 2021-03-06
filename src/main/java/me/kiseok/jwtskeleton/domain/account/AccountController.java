package me.kiseok.jwtskeleton.domain.account;

import lombok.RequiredArgsConstructor;
import me.kiseok.jwtskeleton.domain.account.dto.AccountRequest;
import me.kiseok.jwtskeleton.domain.account.dto.AccountResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import javax.validation.Valid;

@RequiredArgsConstructor
@RequestMapping(value = "/api/accounts")
@RestController
public class AccountController {

    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;

    @GetMapping("/{id}")
    ResponseEntity<?> loadAccount(@PathVariable Long id)    {
        Account account =  accountRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ID is NULL"));
        AccountResponse responseDto = new AccountResponse();

        return new ResponseEntity<>(responseDto.toResponse(account), HttpStatus.OK);
    }

    @PostMapping
    ResponseEntity<?> saveAccount(@RequestBody @Valid AccountRequest accountDto, Errors errors)    {
        if(errors.hasErrors())  {
            return new ResponseEntity<>(errors.getFieldError(), HttpStatus.BAD_REQUEST);
        }
        Account savedAccount = accountRepository.save(accountDto.toEntity(passwordEncoder));
        AccountResponse responseDto = new AccountResponse();

        return new ResponseEntity<>(responseDto.toResponse(savedAccount), HttpStatus.CREATED);
    }

}
