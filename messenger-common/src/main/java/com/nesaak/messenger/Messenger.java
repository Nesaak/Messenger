package com.nesaak.messenger;

import com.google.common.collect.HashMultimap;
import com.google.common.primitives.Ints;

import java.util.Arrays;

public class Messenger {

    private MessageAgent agent;
    private ProtocolManager protocolManager;
    private MessageListener listener = bytes -> receive(bytes);
    private HashMultimap<Integer, MessageListener> listeners = HashMultimap.create();

    public Messenger(MessageAgent agent) {
        protocolManager = new ProtocolManager();
        this.agent = agent;
        agent.setListener(listener);
    }

    public ProtocolManager getProtocolManager() {
        return protocolManager;
    }

    public MessageAgent getAgent() {
        return agent;
    }

    public void listen(Protocol protocol, MessageListener listener) {
        listeners.put(protocol.getID().hashCode(), listener);
    }

    private void receive(byte[] bytes) {
        System.out.println("[IN] " + Arrays.asList(bytes));
        if (bytes.length < 4) return;
        int id = Ints.fromBytes(bytes[0], bytes[1], bytes[2], bytes[4]);
        for (MessageListener listener : listeners.get(id)) {
            listener.receive(bytes);
        }
    }

    public void publish(byte[] bytes) {
        agent.send(bytes);
    }

    public void publish(Message message) {
        byte[] id = Ints.toByteArray(message.getProtocol().getID().hashCode());
        byte[] bytes = message.getProtocol().toBytes(message);
        System.out.println("[OUT] " + Arrays.asList(id));
        System.out.println("[OUT] " + Arrays.asList(bytes));
        System.out.println("[OUT] " + Arrays.asList(concat(id, bytes)));
        publish(concat(id, bytes));
    }

    public static void main(String[] args) {
        Messenger messenger = new Messenger(new MessageAgent() {
            private MessageListener listener;

            @Override
            public void send(byte[] bytes) {
                receive(bytes);
            }

            @Override
            public MessageListener getListener() {
                return listener;
            }

            @Override
            public void setListener(MessageListener listener) {
                this.listener = listener;
            }
        });
        messenger.publish(new Message() {
            @Override
            public Protocol getProtocol() {
                return null;
            }
        });
    }

    private byte[] concat(byte[] a, byte[] b) {
        byte[] ret = new byte[a.length + b.length];
        int index = -1;
        for (byte aByte : a) {
            ret[index++] = aByte;
        }
        for (byte aByte : b) {
            ret[index++] = aByte;
        }
        return ret;
    }
}
