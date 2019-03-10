package de.reflex.chaturbate.proxy;

import de.reflex.chaturbate.captchaBypass.Helper.DebugHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProxyCheck implements Runnable {
    private static final int TIMEOUT = 3000;

    private Thread t;

    private List<SimpleProxy> proxyCake;

    private ProxyCheckCallback callback;

    public ProxyCheck(List<SimpleProxy> proxyCake, ProxyCheckCallback callback) {
        this.proxyCake = proxyCake;
        this.callback = callback;
    }


    @Override
    public void run() {
        for (SimpleProxy proxy : proxyCake) {
                if (proxy.isAlive(TIMEOUT)) {
                    callback.addCheckedProxy(proxy);
                    DebugHelper.out("[+] Checked Proxy! > "+proxy.toString()+"\n"+"\t"+"-> Delay > "+proxy.getDelay()+"ms", DebugHelper.Type.SUCCESS);
                } else DebugHelper.out("[!] Removed Proxy! > "+proxy.toString(), DebugHelper.Type.ERROR);
        }
        t = null;
    }

    protected void start() {
        DebugHelper.out("[i] Starting new ProxyChecking Thread!", DebugHelper.Type.INFO);

        if (t == null) {
            t = new Thread(this);
            t.start();
        }
    }
}
