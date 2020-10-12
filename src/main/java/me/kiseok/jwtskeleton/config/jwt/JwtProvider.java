package me.kiseok.jwtskeleton.config.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import me.kiseok.jwtskeleton.domain.account.Account;
import me.kiseok.jwtskeleton.domain.account.AccountService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Component
public class JwtProvider {

    private String secretKey = "kiseok";
    private Long jwtValidateTime = 30 * 60 * 1000L;
    private final AccountService accountService;

    @PostConstruct
    protected void init()   {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String generateJwt(String email, List<String> roles)   {
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("roles", roles);
        Date now = new Date();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + jwtValidateTime))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact()
                ;
    }

    public Authentication getAuthentication(String jwt)   {
        Account account = (Account) accountService.loadUserByUsername(this.getEmailFromJwt(jwt));

        return new UsernamePasswordAuthenticationToken(accountService, "", account.getAuthorities());
    }

    public String getEmailFromJwt(String jwt)   {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(jwt)
                .getBody()
                .getSubject()
                ;
    }

    public String resolveJwt(HttpServletRequest request)  {
        return request.getHeader("Authorization");
    }

    public boolean validateJwt(String jwt)  {
        try {
            Jws<Claims> claimsJws = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(jwt.substring(7));

            return !claimsJws.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

}
