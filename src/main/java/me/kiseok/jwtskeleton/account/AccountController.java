package me.kiseok.jwtskeleton.account;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.Optional;

@RequiredArgsConstructor
@RequestMapping(value = "/api/accounts")
@RestController
public class AccountController {

    private final AccountRepository accountRepository;

    @GetMapping("/{id}")
    ResponseEntity<?> loadAccount(@PathVariable Long id)    {
        Optional<Account> optionalAccount = accountRepository.findById(id);
        if(!optionalAccount.isPresent()) {
            return new ResponseEntity<>("{\"message\" : \"ID is NULL.\"}", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(optionalAccount.get(), HttpStatus.OK);
    }

    @PostMapping
    ResponseEntity<?> saveAccount(@RequestBody @Valid AccountDto accountDto, Errors errors)    {
        if(errors.hasErrors())  {
            return new ResponseEntity<>(errors.getFieldErrorCount(), HttpStatus.BAD_REQUEST);
        }
        Account savedAccount = accountRepository.save(accountDto.toEntity());

        return new ResponseEntity<>(savedAccount, HttpStatus.CREATED);
    }

}
