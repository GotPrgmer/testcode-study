package org.study.testcode;

import lombok.AllArgsConstructor;

import java.util.UUID;
@AllArgsConstructor
public class TestUuidHolder implements UuidHolder{
    private final UUID fixed;
    @Override
    public UUID randomUUID(){
        return fixed;
    }
}
