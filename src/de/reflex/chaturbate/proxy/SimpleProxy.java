package de.reflex.chaturbate.proxy;

import de.reflex.chaturbate.captchaBypass.Helper.DebugHelper;

import java.io.IOException;
import java.net.*;
import java.util.Objects;

public class SimpleProxy {
    private String host;
    private int port;

    private long delay;

    public SimpleProxy(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean isAlive(int timeout) {
        long t0 = System.currentTimeMillis();
        boolean status = false;
        try {
            if (new InetSocketAddress(host, port).getAddress().isReachable(timeout)) {
                status = true;
            }
        } catch (IOException ignored) {}
        long t1 = System.currentTimeMillis();
        delay = t1-t0;

        return status;
    }

    @Override
    public String toString() {
        return "SimpleProxy{" +
                "host='" + host + '\'' +
                ", port=" + port +
                '}';
    }

    public long getDelay() {
        return delay;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SimpleProxy)) return false;
        SimpleProxy proxy = (SimpleProxy) o;
        return getPort() == proxy.getPort() &&
                Objects.equals(getHost(), proxy.getHost());
    }

}
