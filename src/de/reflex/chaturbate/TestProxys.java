package de.reflex.chaturbate;

import de.reflex.chaturbate.captchaBypass.Helper.DebugHelper;
import de.reflex.chaturbate.proxy.ProxyCheck;
import de.reflex.chaturbate.proxy.ProxySupport;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class TestProxys {
    public static void main(String[] args) {
        DebugHelper.out("ABC", DebugHelper.Type.INFO);
        ProxySupport proxySupport = new ProxySupport();
        File p = new File("C:\\proxies\\proxylist.txt");
        try {
            proxySupport.addProxies(p.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            proxySupport.checkProxies(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
