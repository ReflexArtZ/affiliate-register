package de.reflex.chaturbate;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.ProxyConfig;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import de.reflex.chaturbate.captchaBypass.Api.NoCaptchaProxyless;
import de.reflex.chaturbate.captchaBypass.Helper.DebugHelper;
import de.reflex.chaturbate.proxy.SimpleProxy;
import de.reflex.chaturbate.fakeCredentials.ChaturbateAccount;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.Select;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class ChaturbateControl extends Thread {
    private static final String CLIENT_API_KEY = "...";

    private static final String AGREE_TEXT = "I AGREE";     // text used for the 18 confirmation
    private static final String SIGNUP_TEXT = "Create Free Account";




    public ChaturbateControl() {

    }


    public boolean startWithSelenium(String url, ChaturbateAccount account, SimpleProxy proxy, int waitForRegister) throws IOException, InterruptedException {
        DebugHelper.out("########################################################", DebugHelper.Type.INFO);
        DebugHelper.out("[i] Using Proxy: " + proxy.toString(), DebugHelper.Type.INFO);
        DebugHelper.out("[i] Using Account: " + account.toString(), DebugHelper.Type.INFO);
        DebugHelper.out("[i] Initializing WebClient...", DebugHelper.Type.INFO);

        System.setProperty("webdriver.chrome.driver", "C:\\Users\\Patrick\\Documents\\driver-selenium\\chromedriver_win32\\chromedriver.exe");
        Proxy p = new Proxy();
        p.setHttpProxy(proxy.getHost()+"."+proxy.getPort());
        DesiredCapabilities cap = new DesiredCapabilities();
        cap.setCapability(CapabilityType.PROXY, p);
        WebDriver driver = new ChromeDriver(cap);

        driver.navigate().to(url);

        driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

        driver.findElement(By.linkText("SIGN UP")).click();

        driver.findElement(By.xpath("//*[@id=\"husername\"]")).sendKeys(account.getUsername());
        driver.findElement(By.xpath("//*[@id=\"hpassword\"]")).sendKeys(account.getPassword());

        Select birthday_day = new Select(driver.findElement(By.xpath("//*[@id=\"id_birthday_day\"]")));
        Select birthday_month = new Select(driver.findElement(By.xpath("//*[@id=\"id_birthday_month\"]")));
        Select birthday_year = new Select(driver.findElement(By.xpath("//*[@id=\"id_birthday_year\"]")));
        Select gender = new Select(driver.findElement(By.xpath("//*[@id=\"id_gender\"]")));
        birthday_day.selectByValue(String.valueOf(account.getBirthday_day()));
        birthday_month.selectByValue(String.valueOf(account.getBirthday_month()));
        birthday_year.selectByValue(String.valueOf(account.getBirthday_year()));
        gender.selectByValue(String.valueOf(account.getGender()));

        WebElement human_aprove_id = driver.findElement(By.id("id_prove_you_are_human"));
        String recaptcha_key = human_aprove_id.getAttribute("data-sitekey");

        String code = bypassCaptcha(recaptcha_key, driver.getCurrentUrl());
        if (code == null) {
            return false;
        }

        WebElement captchaArea = driver.findElement(By.id("g-recaptcha-response"));

        JavascriptExecutor jse = (JavascriptExecutor) driver;
        jse.executeScript("arguments[0].style.display = 'block';", captchaArea);
        captchaArea.sendKeys(code);

        driver.findElement(By.xpath("//*[@id=\"id_terms\"]")).click();
        driver.findElement(By.xpath("//*[@id=\"id_privacy_policy\"]")).click();

        driver.findElement(By.xpath("//*[@id=\"formsubmit\"]")).click();

        driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

        return false;

    }

    public boolean start(String url, ChaturbateAccount account, SimpleProxy proxy, int waitForRegister) throws IOException, InterruptedException {

        // --- setting client options ---


        DebugHelper.out("########################################################", DebugHelper.Type.INFO);
        DebugHelper.out("[i] Using Proxy: " + proxy.toString(), DebugHelper.Type.INFO);
        DebugHelper.out("[i] Using Account: " + account.toString(), DebugHelper.Type.INFO);
        DebugHelper.out("[i] Initializing WebClient...", DebugHelper.Type.INFO);

        WebClient webClient = new WebClient(BrowserVersion.BEST_SUPPORTED);
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setCssEnabled(false);
        webClient.getCookieManager().setCookiesEnabled(true);



        ProxyConfig proxyConfig = new ProxyConfig(proxy.getHost(), proxy.getPort(), false);
       webClient.getOptions().setProxyConfig(proxyConfig);

        HtmlPage page;
        try {
            page = webClient.getPage(url);
        } catch (FailingHttpStatusCodeException ex) {
            return false;
        }



        if (page.asText().contains("cloudflare")) { //ToDo: Analyse cloudflare presite
            DebugHelper.out("[-] Cloudflare detected Proxy! Trying to bypass protection...", DebugHelper.Type.ERROR);
            Thread.sleep(1000);
            HtmlDivision human_prove_id = page.getHtmlElementById("id_prove_you_are_human");
            String recaptcha_key = human_prove_id.getAttribute("data-sitekey");

            DebugHelper.out("[+] Got recaptcha key for bypass > " + recaptcha_key, DebugHelper.Type.SUCCESS);

            String code = bypassCaptcha(recaptcha_key, page.getBaseURL().toString());
            if (code == null) {
                return false;
            }

            DebugHelper.out("[i] Injecting captcha result into google recaptcha hidden field...", DebugHelper.Type.INFO);
            HtmlTextArea captchaResult = (HtmlTextArea) page.getElementById("g-recaptcha-response");
            captchaResult.setText(code);
            System.out.println(page.asText());
        }

        // --- Agree adult information ---

        DebugHelper.out("[i] Agreeing adult information...", DebugHelper.Type.INFO);


        if (page.asText().contains(AGREE_TEXT)) {
            HtmlAnchor agree = page.getAnchorByText(AGREE_TEXT);
            agree.click();
        }

        // --- Click register button after some time

        DebugHelper.out("[legit] Waiting... ", DebugHelper.Type.INFO);
        try {
            Thread.sleep((long) (waitForRegister + (Math.random() * 25)));
        } catch (InterruptedException ignored) {
        }   // ToDo
        HtmlAnchor register = page.getAnchorByHref("/accounts/register/");
        page = register.click();
        DebugHelper.out("[i] Got onto register page!", DebugHelper.Type.INFO);
        System.out.println(page.getWebClient().getBrowserVersion().toString());

        System.out.println(webClient.getCookieManager().getCookies().toString());

        // --- Getting the register form and it's values ---

        DebugHelper.out("[i] Getting register form and it's values...", DebugHelper.Type.INFO);
        HtmlForm form = page.getForms().get(1);
        HtmlSubmitInput submitInput = form.getInputByValue(SIGNUP_TEXT);
        HtmlTextInput name = form.getInputByName("username");
        HtmlPasswordInput pass = form.getInputByName("password");
        HtmlSelect birthday_day = page.getElementByName("birthday_day");
        HtmlSelect birthday_month = page.getElementByName("birthday_month");
        HtmlSelect birthday_year = page.getElementByName("birthday_year");
        HtmlSelect gender = page.getElementByName("gender");
        HtmlCheckBoxInput terms = form.getInputByName("terms");
        HtmlCheckBoxInput privacy_policy = form.getInputByName("privacy_policy");
        HtmlDivision human_prove_id = page.getHtmlElementById("id_prove_you_are_human");


        // --- Getting recaptcha key for the bypass ---

        String recaptcha_key = human_prove_id.getAttribute("data-sitekey");
        DebugHelper.out("[+] Got recaptcha key for bypass > " + recaptcha_key, DebugHelper.Type.SUCCESS);

        // --- Setting form value attributes ---

        DebugHelper.out("[i] Setting Account value attributes...", DebugHelper.Type.INFO);
        name.setValueAttribute(account.getUsername());
        pass.setValueAttribute(account.getPassword());
        birthday_day.setSelectedAttribute(String.valueOf(account.getBirthday_day()), true);
        birthday_month.setSelectedAttribute(String.valueOf(account.getBirthday_month()), true);
        birthday_year.setSelectedAttribute(String.valueOf(account.getBirthday_year()), true);
        gender.setSelectedAttribute(String.valueOf(account.getGender()), true);
        terms.setChecked(true);
        privacy_policy.setChecked(true);

        // --- Bypassing captcha

        String code = bypassCaptcha(recaptcha_key, page.getBaseURL().toString());
        if (code == null) {
            return false;
        }


        // --- Injecting captcha result into google recaptchas hidden response field ---

        DebugHelper.out("[i] Injecting captcha result into google recaptcha hidden field...", DebugHelper.Type.INFO);
        HtmlTextArea captchaResult = (HtmlTextArea) page.getElementById("g-recaptcha-response");
        captchaResult.setText(code);

        // --- Submit the registration ---

        HtmlPage submitPage = submitInput.click();

        System.out.println(submitPage.asText());

        return submitPage.asText().contains("Your account has been created and you are logged in.");

    }

    private static String bypassCaptcha(String recaptcha_key, String website) throws IOException, InterruptedException {
        DebugHelper.out("########################################################", DebugHelper.Type.INFO);
        DebugHelper.out("# [i] Start bypassing Captcha...                       #", DebugHelper.Type.INFO);

        NoCaptchaProxyless api = new NoCaptchaProxyless();
        api.setClientKey(CLIENT_API_KEY);
        api.setWebsiteUrl(new URL(website));
        api.setWebsiteKey(recaptcha_key);

        if (!api.createTask()) {
            DebugHelper.out(
                    "# [-] API v2 send failed. " + api.getErrorMessage(),
                    DebugHelper.Type.ERROR
            );
        } else if (!api.waitForResult()) {
            DebugHelper.out("# [-] Could not solve the captcha.", DebugHelper.Type.ERROR);
        } else {
            String success = api.getTaskSolution().getGRecaptchaResponse();

            DebugHelper.out("# [+] Bypassed Captcha! Result: " + success, DebugHelper.Type.SUCCESS);
            DebugHelper.out("########################################################", DebugHelper.Type.INFO);
            DebugHelper.out("", DebugHelper.Type.INFO);
            return success;
        }
        return null;
    }

}
