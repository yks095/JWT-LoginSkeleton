package me.kiseok.jwtskeleton.domain.login;

import lombok.RequiredArgsConstructor;
import me.kiseok.jwtskeleton.config.jwt.JwtProvider;
import me.kiseok.jwtskeleton.domain.account.Account;
import me.kiseok.jwtskeleton.domain.account.AccountRepository;
import me.kiseok.jwtskeleton.domain.login.dto.LoginRequestDto;
import me.kiseok.jwtskeleton.domain.login.dto.LoginResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.Optional;

@RequiredArgsConstructor
@RequestMapping(value = "/api/login")
@RestController
public class LoginController {

    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final AccountRepository accountRepository;

    @PostMapping
    ResponseEntity<?> login(@RequestBody @Valid LoginRequestDto loginRequestDto)  {
        Account account = accountRepository.findByEmail(loginRequestDto.getEmail()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Exist!"));
        if(!passwordEncoder.matches(loginRequestDto.getPassword(), account.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password Not Match");
        }
        String jwt = jwtProvider.generateJwt(account.getEmail(), account.getRoles());

        return new ResponseEntity<>(new LoginResponseDto(jwt), HttpStatus.OK);
    }

}
