package com.nesaak.messenger;

import java.util.ArrayList;
import java.util.List;

public class Messenger {

    private MessageAgent agent;
    private MessageListener listener = bytes -> receive(bytes);
    private List<MessageListener> listeners = new ArrayList();

    public Messenger(MessageAgent agent) {
        this.agent = agent;
        agent.setListener(listener);
    }

    public MessageAgent getAgent() {
        return agent;
    }

    public List<MessageListener> getListeners() {
        return listeners;
    }

    private void receive(byte[] bytes) {
        for (MessageListener listener : listeners) {
            listener.receive(bytes);
        }
    }

    public void publish(byte[] bytes) {
        agent.send(bytes);
    }
}
