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

    private static final String CHATURBATE_URL = "...";
    private static final String PASS_FILE_PATH = "C:\\proxies\\fakeCreds\\pass.txt";
    private static final String USER_FILE_PATH = "C:\\proxies\\fakeCreds\\users.txt";

    private static int loops = 0;

    public static void main(String[] args) throws InterruptedException, IOException, JSONException {
        ChaturbateControl chaturbateControl = new ChaturbateControl();
        FakeCredsSupport fakeCredsSupport = new FakeCredsSupport(new File(USER_FILE_PATH).toPath(), new File(PASS_FILE_PATH).toPath());

        ProxyRest rest = new ProxyRest();

        //chaturbateControl.startWithSelenium(CHATURBATE_URL, fakeCredsSupport.getRandomAccount(), rest.getProxy(), 10);

        for (int i = 0; i < 500; i++) {   //ToDo
            try {
                if (chaturbateControl.start(CHATURBATE_URL, fakeCredsSupport.getRandomAccount(),rest.getProxy(), 5)) {
                    loops++;
                    DebugHelper.out("[+] +1 Loop!", DebugHelper.Type.SUCCESS);
                }
            } catch (Exception e) {
                continue;
            }
            DebugHelper.out("[i] Loops made > "+loops, DebugHelper.Type.INFO);
            Thread.sleep(15000);
        }

    }

    @Override
    public void proxiesChecked() {

    }
}
