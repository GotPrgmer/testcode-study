package org.study.testcode;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class Account {
    private final String username;
    private final String authToken;
    private final UUID uuid;

    public static Account create(String username,UuidHolder uuid) {
        return Account.builder()
                .username(username)
                .authToken(uuid.randomUUID().toString())
                .build();
    }
}
