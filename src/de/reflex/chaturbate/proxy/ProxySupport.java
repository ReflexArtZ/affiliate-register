package de.reflex.chaturbate.proxy;

import de.reflex.chaturbate.IProxyCheckDone;
import de.reflex.chaturbate.captchaBypass.Helper.DebugHelper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ProxySupport implements ProxyCheckCallback{

    private ArrayList<SimpleProxy> uncheckedProxies;
    private ArrayList<SimpleProxy> proxies;

    private Random random = new Random();


    private IProxyCheckDone proxyCheckDone;

    public ProxySupport(/*IProxyCheckDone proxyCheckDone*/) {
        uncheckedProxies = new ArrayList<>();
        proxies = new ArrayList<>();
        this.proxyCheckDone = proxyCheckDone;
    }

    public void addProxies(Path proxyFilePath) throws IOException {
        Files.lines(proxyFilePath).forEach(p -> addUncheckedProxy(convertStringToProxy(p)));
    }
    public void checkProxies(int threads) throws InterruptedException {
        if (uncheckedProxies.size() == 0) {
            DebugHelper.out("[-] No proxies to check!", DebugHelper.Type.ERROR);
            return;
        }
        while (threads > uncheckedProxies.size()) threads--;

        int proxysPerThread = uncheckedProxies.size()/threads;
        ExecutorService es = Executors.newCachedThreadPool();

        for (int i = 0; i < threads; i++) {
            if (i+1 == threads) {
                es.execute(new ProxyCheck(uncheckedProxies.subList(i*proxysPerThread, uncheckedProxies.size()), this));
            } else {
                es.execute(new ProxyCheck(uncheckedProxies.subList(i * proxysPerThread, i * proxysPerThread + proxysPerThread), this));
            }
        }
        es.shutdown();
        while(!es.awaitTermination(-1, TimeUnit.MINUTES));

       // proxyCheckDone.proxiesChecked();

    }
    public static SimpleProxy convertStringToProxy(String proxyString) {
        if (proxyString.contains(":")) {
            String host = proxyString.split(":")[0];
            String portStr = proxyString.split(":")[1];
            int port;
            try {
                port = Integer.parseInt(portStr);
            } catch (NumberFormatException ex) {
                DebugHelper.out("[-] Could Not convert String to Proxy! \n"+proxyString, DebugHelper.Type.ERROR);
                return null;
            }
            return new SimpleProxy(host, port);
        }
        if (proxyString.contains(" ")) {
            String host = proxyString.split(" ")[0];
            String portStr = proxyString.split(" ")[1];
            int port;
            try {
                port = Integer.parseInt(portStr);
            } catch (NumberFormatException ex) {
                DebugHelper.out("[-] Could Not convert String to Proxy! \n"+proxyString, DebugHelper.Type.ERROR);
                return null;
            }
            return new SimpleProxy(host, port);
        }
        return null;
    }

    public SimpleProxy getRandomProxy() {
        SimpleProxy proxy = proxies.get(random.nextInt(proxies.size()));
        proxies.remove(proxy);
        return proxy;
    }

    private void addUncheckedProxy(SimpleProxy proxy) {
        if (!uncheckedProxies.contains(proxy)) uncheckedProxies.add(proxy);
    }

    public int getUncheckedProxiesSize() {
        return uncheckedProxies.size();
    }


    @Override
    public synchronized void addCheckedProxy(SimpleProxy proxy) {
        proxies.add(proxy);
    }

    public ArrayList<SimpleProxy> getProxies() {
        return proxies;
    }
}
