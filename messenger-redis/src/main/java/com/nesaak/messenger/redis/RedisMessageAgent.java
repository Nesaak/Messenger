package com.nesaak.messenger.redis;

import com.nesaak.messenger.MessageAgent;
import com.nesaak.messenger.MessageListener;
import redis.clients.jedis.BinaryJedisPubSub;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.lang.ref.WeakReference;
import java.util.concurrent.CompletableFuture;

public class RedisMessageAgent extends BinaryJedisPubSub implements MessageAgent {

    private static final byte[] CHANNEL = "messages".getBytes();

    private WeakReference<JedisPool> pool;
    private MessageListener listener;

    public RedisMessageAgent(JedisPool pool) {
        this.pool = new WeakReference<JedisPool>(pool);

        CompletableFuture.runAsync(() -> {
            Jedis jedis = getJedis();
            jedis.subscribe(this, CHANNEL);
        });
    }

    public Jedis getJedis() {
        JedisPool jedisPool = pool.get();
        if (jedisPool == null) {
            System.err.println("Could not send message: JedisPool no longer exists");
            return null;
        }
        return jedisPool.getResource();
    }

    @Override
    public void send(byte[] bytes) {
        Jedis jedis = getJedis();
        if (jedis == null) return;

        jedis.publish(CHANNEL, bytes);
    }

    @Override
    public void onMessage(byte[] channel, byte[] message) {
        receive(message);
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
