package com.nesaak.messenger;

public interface Protocol<T extends Message> {

    byte[] getBytes(T object);

    T fromBytes(byte[] bytes);

    int getID();
}
