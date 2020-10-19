package me.kiseok.jwtskeleton.config.jwt;

import lombok.RequiredArgsConstructor;
import me.kiseok.jwtskeleton.domain.token.TokenRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

    private final TokenRepository tokenRepository;
    private final JwtProvider jwtProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String jwt = jwtProvider.resolveJwt((HttpServletRequest) request);

        if(( jwt != null ) && (!tokenRepository.findByToken(jwt.substring(7)).isPresent()) && ( jwtProvider.validateJwt(jwt) && ( jwt.startsWith("Bearer ") ) )) {
            Authentication authentication = jwtProvider.getAuthentication(jwt.substring(7));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        chain.doFilter(request, response);
    }
}
