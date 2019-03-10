package de.reflex.chaturbate;


import de.reflex.chaturbate.captchaBypass.Helper.DebugHelper;
import de.reflex.chaturbate.fakeCredentials.ChaturbateAccount;
import de.reflex.chaturbate.fakeCredentials.FakeCredsSupport;
import de.reflex.chaturbate.proxy.ProxyRest;
import de.reflex.chaturbate.proxy.ProxySupport;
import de.reflex.chaturbate.proxy.SimpleProxy;
import org.json.JSONException;

import java.io.File;
import java.io.IOException;

public class Main implements IProxyCheckDone{

    private static final String CHATURBATE_URL = "https://en.chaturbate.com/in/?track=default&tour=OT2s&campaign=BRMrQ";
    private static final String PASS_FILE_PATH = "C:\\proxies\\fakeCreds\\pass.txt";
    private static final String USER_FILE_PATH = "C:\\proxies\\fakeCreds\\users.txt";

    private static int money_made = 0;

    public static void main(String[] args) throws InterruptedException, IOException, JSONException {
        ChaturbateControl chaturbateControl = new ChaturbateControl();
        FakeCredsSupport fakeCredsSupport = new FakeCredsSupport(new File(USER_FILE_PATH).toPath(), new File(PASS_FILE_PATH).toPath());

        ProxyRest rest = new ProxyRest();

        //chaturbateControl.startWithSelenium(CHATURBATE_URL, fakeCredsSupport.getRandomAccount(), rest.getProxy(), 10);

        for (int i = 0; i < 500; i++) {   //ToDo
            try {
                if (chaturbateControl.start(CHATURBATE_URL, fakeCredsSupport.getRandomAccount(),rest.getProxy(), 5)) {
                    money_made++;
                    DebugHelper.out("[+] You again made 1$!", DebugHelper.Type.SUCCESS);
                }
            } catch (Exception e) {
                continue;
            }
            DebugHelper.out("[i] Money made > "+money_made, DebugHelper.Type.INFO);
            Thread.sleep(15000);
        }

    }

    @Override
    public void proxiesChecked() {

    }
}
