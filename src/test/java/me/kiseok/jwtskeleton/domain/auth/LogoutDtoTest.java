package me.kiseok.jwtskeleton.domain.auth;

import me.kiseok.jwtskeleton.domain.auth.dto.LogoutResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
public class LogoutDtoTest {

    @DisplayName("LogoutResponse @Getter @Builder 테스트")
    @Test
    void test_logout_response_getter_builder()  {
        Long id = 1L;
        LocalDateTime localDateTime = LocalDateTime.now();

        LogoutResponse logoutResponse = LogoutResponse.builder()
                .id(id)
                .localDateTime(localDateTime)
                .build();

        assertEquals(logoutResponse.getId(), id);
        assertEquals(logoutResponse.getLocalDateTime(), localDateTime);
    }
}
