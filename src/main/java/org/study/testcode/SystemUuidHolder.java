package org.study.testcode;

import lombok.AllArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
public class SystemUuidHolder implements UuidHolder{
    @Override
    public UUID randomUUID(){
        return UUID.randomUUID();
    }
}
