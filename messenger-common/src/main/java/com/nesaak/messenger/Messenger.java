package com.nesaak.messenger;

public class Messenger {

    private MessageAgent agent;
    private MessageListener listener;

    public Messenger(MessageAgent agent) {
        this.agent = agent;

        listener = bytes -> receive(bytes);
        agent.setListener(listener);
    }

    public MessageAgent getAgent() {
        return agent;
    }

    private void receive(byte[] bytes) {
        System.out.println("[IN] " + new String(bytes));
    }

    public void send(byte[] bytes) {
        System.out.println("[OUT] " + new String(bytes));
        agent.send(bytes);
    }
}
