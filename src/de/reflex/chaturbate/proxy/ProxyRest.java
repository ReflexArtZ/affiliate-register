package de.reflex.chaturbate.proxy;

import de.reflex.chaturbate.captchaBypass.Helper.DebugHelper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class ProxyRest {
    private static final String URI = "https://api.getproxylist.com/proxy?&protocol[]=http&allowsCookies=1&allowsHttps=1&country[]=CA&country[]=US&country[]=DE&country[]=AT&country[]=FR&country[]=CH&minUptime=75";


    private ArrayList<SimpleProxy> usedProxies = new ArrayList<>();

    public ProxyRest() {

    }


    public SimpleProxy getProxy() throws IOException, JSONException {
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpGet getRequest = new HttpGet(URI);
        getRequest.addHeader("accept", "application/json");

        HttpResponse response = httpClient.execute(getRequest);

        if (response.getStatusLine().getStatusCode() != 200) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + response.getStatusLine().getStatusCode());
        }

        BufferedReader br = new BufferedReader(
                new   InputStreamReader((response.getEntity().getContent())));
        String s;
        StringBuilder rdy = new StringBuilder();
        while ((s = br.readLine()) != null) {
            rdy.append(s);
        }
        JSONObject object = new JSONObject(rdy.toString());
        String host = object.getString("ip");
        int port = object.getInt("port");

        SimpleProxy proxy = new SimpleProxy(host, port);
        if (!proxy.isAlive(4000)) return getProxy();
        if (usedProxies.stream().anyMatch(p -> p.equals(proxy))) return getProxy(); // proxy already used
        usedProxies.add(proxy);
        DebugHelper.out("[+] Checked Proxy! > "+proxy.toString()+"\n"+"\t"+"-> Delay > "+proxy.getDelay()+"ms", DebugHelper.Type.SUCCESS);
        return new SimpleProxy(host, port);
    }
}
