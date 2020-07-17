package com.nesaak.messenger;

import java.util.HashMap;
import java.util.Map;

public class ProtocolManager {

    private Map<String, Protocol> registeredProtocols = new HashMap<>();
    private Map<Integer, String> hashResolves = new HashMap<>();

    public void registerProtocol(Protocol protocol) {
        registeredProtocols.put(protocol.getID(), protocol);
        hashResolves.put(protocol.getID().hashCode(), protocol.getID());
    }

    public Protocol getRegisteredProtocol(String id) {
        return registeredProtocols.get(id);
    }

    public Protocol getRegisteredProtocol(int hash) {
        return registeredProtocols.get(hashResolves.get(hash));
    }
}
