package com.nesaak.messenger.bungee;

import com.nesaak.messenger.MessageAgent;
import com.nesaak.messenger.MessageListener;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

import java.lang.ref.WeakReference;

public class BungeeMessageAgent implements MessageAgent, Listener {

    private final String channel;

    private WeakReference<Plugin> plugin;
    private MessageListener listener;

    public BungeeMessageAgent(String channel, Plugin plugin) {
        this.plugin = new WeakReference(plugin);
        this.channel = "messenger:" + channel;

        plugin.getProxy().registerChannel(this.channel);
        plugin.getProxy().getPluginManager().registerListener(getPlugin(), this);
    }

    public Plugin getPlugin() {
        Plugin plugin = this.plugin.get();
        if (plugin == null) {
            throw new NullPointerException("Plugin cannot be null");
        }
        return plugin;
    }

    @EventHandler
    public void onMessageEvent(PluginMessageEvent event) {
        if (event.getTag().equals(channel)) {
            if (event.getReceiver() instanceof ProxiedPlayer) {
                receive(event.getData());
                event.setCancelled(true);
            }
        }
    }

    @Override
    public void send(byte[] bytes) {
        for (ServerInfo server : getPlugin().getProxy().getServers().values()) {
            server.sendData(channel, bytes, true);
        }
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
}
