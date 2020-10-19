package me.kiseok.jwtskeleton.domain.auth;

import lombok.RequiredArgsConstructor;
import me.kiseok.jwtskeleton.config.jwt.JwtProvider;
import me.kiseok.jwtskeleton.domain.account.Account;
import me.kiseok.jwtskeleton.domain.account.AccountRepository;
import me.kiseok.jwtskeleton.domain.auth.dto.LoginRequest;
import me.kiseok.jwtskeleton.domain.auth.dto.LoginResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import javax.validation.Valid;

@RequiredArgsConstructor
@RequestMapping(value = "/api/login")
@RestController
public class LoginController {

    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final AccountRepository accountRepository;

    @PostMapping
    ResponseEntity<?> login(@RequestBody @Valid LoginRequest loginRequest)  {
        Account account = accountRepository.findByEmail(loginRequest.getEmail()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Exist!"));
        if(!passwordEncoder.matches(loginRequest.getPassword(), account.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password Not Match");
        }
        String jwt = jwtProvider.generateJwt(account.getEmail(), account.getRoles());

        return new ResponseEntity<>(new LoginResponse(jwt), HttpStatus.OK);
    }

}
