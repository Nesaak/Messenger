package com.nesaak.messenger;

import java.util.HashMap;
import java.util.Map;

public class ProtocolManager {

    private Map<Integer, Protocol> protocols = new HashMap<>();

    public Protocol getProtocol(int id) {
        return protocols.get(id);
    }

    public void register(Protocol protocol) {
        protocols.put(protocol.getID(), protocol);
    }
}
