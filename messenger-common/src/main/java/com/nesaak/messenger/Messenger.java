package com.nesaak.messenger;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class Messenger {

    private MessageAgent agent;

    private ProtocolManager protocolManager = new ProtocolManager();
    private MessageListener listener = bytes -> receive(bytes);

    public Messenger(MessageAgent agent) {
        this.agent = agent;
        agent.setListener(listener);
    }

    public MessageAgent getAgent() {
        return agent;
    }

    public ProtocolManager getProtocolManager() {
        return protocolManager;
    }

    private void receive(byte[] bytes) {
        try {
            DataInputStream stream = new DataInputStream(new BufferedInputStream(new ByteArrayInputStream(bytes)));
            int id = stream.readInt();
        } catch (IOException e) {
            throw new RuntimeException("Invalid Message Received:\n" + new String(bytes));
        }
    }

    public void publish(byte[] bytes) {
        agent.send(bytes);
    }

    public void publish(Message message) {
        publish(message.getProtocol().getBytes(message));
    }
}
