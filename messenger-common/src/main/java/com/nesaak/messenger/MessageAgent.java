package com.nesaak.messenger;

public interface MessageAgent {

    void send(byte[] bytes);

    MessageListener getListener();

    void setListener(MessageListener listener);

    default void receive(byte[] bytes) {
        getListener().receive(bytes);
    }

}
