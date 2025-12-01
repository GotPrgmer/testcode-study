package org.study.testcode;

import org.junit.jupiter.api.Test;
import org.powermock.api.mockito.PowerMockito;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class AccountTest {
    @Test
    void login_create_테스트(){
        // given
        String username = "footbar";
        String expectedAuthToken = "550e8400-e29b-41d4-a716-446655440000";
        UUID uuid = UUID.fromString(expectedAuthToken);

        // when
        Account account = Account.create(username, new TestUuidHolder(uuid));

        // then
        assertThat(account.getUsername()).isEqualTo(username);
        assertThat(account.getAuthToken()).isEqualTo(expectedAuthToken);

    }
}
