package com.nesaak.messenger.bukkit;

import com.google.common.collect.Iterables;
import com.nesaak.messenger.MessageAgent;
import com.nesaak.messenger.MessageListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.ref.WeakReference;

public class BukkitMessageAgent implements MessageAgent, PluginMessageListener {

    private static final String CHANNEL = "messenger";
    private static final long MESSAGE_RETRY_INTERVAL = 400;

    private WeakReference<Plugin> plugin;
    private MessageListener listener;

    public BukkitMessageAgent(Plugin plugin) {
        this.plugin = new WeakReference(plugin);

        Bukkit.getMessenger().registerOutgoingPluginChannel(getPlugin(), CHANNEL);
        Bukkit.getMessenger().registerIncomingPluginChannel(getPlugin(), CHANNEL, this);
    }

    public Plugin getPlugin() {
        Plugin plugin = this.plugin.get();
        if (plugin == null) {
            throw new NullPointerException("Plugin cannot be null");
        }
        return plugin;
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] bytes) {
        if (channel.equals(CHANNEL)) {
            receive(bytes);
        }
    }

    @Override
    public void send(byte[] bytes) {
        new BukkitRunnable() {
            @Override
            public void run() {
                Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
                if (player == null) return;

                player.sendPluginMessage(getPlugin(), CHANNEL, bytes);
                cancel();
            }
        }.runTaskTimer(getPlugin(), 0L, MESSAGE_RETRY_INTERVAL);
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
