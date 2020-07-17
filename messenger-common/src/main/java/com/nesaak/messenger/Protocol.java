package com.nesaak.messenger;

public interface Protocol<T extends Message> {

    byte[] toBytes(T type);

    T fromBytes(byte[] bytes);

    String getID();
}
