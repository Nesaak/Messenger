package com.nesaak.messenger;

@FunctionalInterface
public interface MessageListener {
    void receive(byte[] bytes);
}
