package de.reflex.chaturbate.captchaBypass;

/*
public class Main {

    public static void main(String[] args) throws InterruptedException, MalformedURLException, JSONException {

    }

    private static void exampleImageToText() throws InterruptedException {
        DebugHelper.setVerboseMode(true);

        ImageToText api = new ImageToText();
        api.setClientKey("1234567890123456789012");
        api.setFilePath("captcha.jpg");

        if (!api.createTask()) {
            DebugHelper.out(
                    "API v2 send failed. " + api.getErrorMessage(),
                    DebugHelper.Type.ERROR
            );
        } else if (!api.waitForResult()) {
            DebugHelper.out("Could not solve the captcha.", DebugHelper.Type.ERROR);
        } else {
            DebugHelper.out("Result: " + api.getTaskSolution().getText(), DebugHelper.Type.SUCCESS);
        }
    }

    private static void exampleNoCaptchaProxyless() throws MalformedURLException, InterruptedException {
        DebugHelper.setVerboseMode(true);

        NoCaptchaProxyless api = new NoCaptchaProxyless();
        api.setClientKey("1234567890123456789012");
        api.setWebsiteUrl(new URL("http://http.myjino.ru/recaptcha/test-get.php"));
        api.setWebsiteKey("6Lc_aCMTAAAAABx7u2W0WPXnVbI_v6ZdbM6rYf16");

        if (!api.createTask()) {
            DebugHelper.out(
                    "API v2 send failed. " + api.getErrorMessage(),
                    DebugHelper.Type.ERROR
            );
        } else if (!api.waitForResult()) {
            DebugHelper.out("Could not solve the captcha.", DebugHelper.Type.ERROR);
        } else {
            DebugHelper.out("Result: " + api.getTaskSolution().getGRecaptchaResponse(), DebugHelper.Type.SUCCESS);
        }
    }

    private static void exampleNoCaptcha() throws MalformedURLException, InterruptedException {
        DebugHelper.setVerboseMode(true);

        NoCaptcha api = new NoCaptcha();
        api.setClientKey("1234567890123456789012");
        api.setWebsiteUrl(new URL("http://http.myjino.ru/recaptcha/test-get.php"));
        api.setWebsiteKey("6Lc_aCMTAAAAABx7u2W0WPXnVbI_v6ZdbM6rYf16");
        api.setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/537.36 " +
                "(KHTML, like Gecko) Chrome/52.0.2743.116");

        // proxy access parameters
        api.setProxyType(NoCaptcha.ProxyTypeOption.HTTP);
        api.setProxyAddress("xx.xxx.xx.xx");
        api.setProxyPort(8282);
        api.setProxyLogin("login");
        api.setProxyPassword("password");

        if (!api.createTask()) {
            DebugHelper.out(
                    "API v2 send failed. " + api.getErrorMessage(),
                    DebugHelper.Type.ERROR
            );
        } else if (!api.waitForResult()) {
            DebugHelper.out("Could not solve the captcha.", DebugHelper.Type.ERROR);
        } else {
            DebugHelper.out("Result: " + api.getTaskSolution().getGRecaptchaResponse(), DebugHelper.Type.SUCCESS);
        }
    }

    private static void exampleGetBalance() {
        DebugHelper.setVerboseMode(true);

        ImageToText api = new ImageToText();
        api.setClientKey("1234567890123456789012");

        Double balance = api.getBalance();

        if (balance == null) {
            DebugHelper.out("GetBalance() failed. " + api.getErrorMessage(), DebugHelper.Type.ERROR);
        } else {
            DebugHelper.out("Balance: " + balance, DebugHelper.Type.SUCCESS);
        }
    }

    private static void exampleCustomCaptcha() throws JSONException, InterruptedException {
        DebugHelper.setVerboseMode(true);
        int randInt = ThreadLocalRandom.current().nextInt(0, 10000);

        JSONArray forms = new JSONArray();

        forms.put(0, new JSONObject());
        forms.getJSONObject(0).put("label", "number");
        forms.getJSONObject(0).put("labelHint", false);
        forms.getJSONObject(0).put("contentType", false);
        forms.getJSONObject(0).put("name", "license_plate");
        forms.getJSONObject(0).put("inputType", "text");
        forms.getJSONObject(0).put("inputOptions", new JSONObject());
        forms.getJSONObject(0).getJSONObject("inputOptions").put("width", "100");
        forms.getJSONObject(0).getJSONObject("inputOptions").put(
                "placeHolder",
                "Enter letters and numbers without spaces"
        );

        forms.put(1, new JSONObject());
        forms.getJSONObject(1).put("label", "Car color");
        forms.getJSONObject(1).put("labelHint", "Select the car color");
        forms.getJSONObject(1).put("contentType", false);
        forms.getJSONObject(1).put("name", "color");
        forms.getJSONObject(1).put("inputType", "select");
        forms.getJSONObject(1).put("inputOptions", new JSONArray());
        forms.getJSONObject(1).getJSONArray("inputOptions").put(0, new JSONObject());
        forms.getJSONObject(1).getJSONArray("inputOptions").getJSONObject(0).put(
                "value",
                "white"
        );
        forms.getJSONObject(1).getJSONArray("inputOptions").getJSONObject(0).put(
                "caption",
                "White color"
        );
        forms.getJSONObject(1).getJSONArray("inputOptions").put(1, new JSONObject());
        forms.getJSONObject(1).getJSONArray("inputOptions").getJSONObject(1).put(
                "value",
                "black"
        );
        forms.getJSONObject(1).getJSONArray("inputOptions").getJSONObject(1).put(
                "caption",
                "Black color"
        );
        forms.getJSONObject(1).getJSONArray("inputOptions").put(2, new JSONObject());
        forms.getJSONObject(1).getJSONArray("inputOptions").getJSONObject(2).put(
                "value",
                "gray"
        );
        forms.getJSONObject(1).getJSONArray("inputOptions").getJSONObject(2).put(
                "caption",
                "Gray color"
        );

        CustomCaptcha api = new CustomCaptcha();
        api.setClientKey("1234567890123456789012");
        api.setImageUrl("https://files.anti-captcha.com/26/41f/c23/7c50ff19.jpg?random=" + randInt);
        api.setAssignment("Enter the licence plate number");
        api.setForms(forms);

        if (!api.createTask()) {
            DebugHelper.out(
                    "API v2 send failed. " + api.getErrorMessage(),
                    DebugHelper.Type.ERROR
            );
        } else if (!api.waitForResult()) {
            DebugHelper.out("Could not solve the captcha.", DebugHelper.Type.ERROR);
        } else {
            JSONObject answers = api.getTaskSolution().getAnswers();
            Iterator<?> keys = answers.keys();

            while (keys.hasNext()) {
                String question = (String) keys.next();
                String answer = answers.getString(question);

                DebugHelper.out(
                        "The answer for the question '" + question + "' : " + answer,
                        DebugHelper.Type.SUCCESS
                );
            }
        }
    }

    private static void exampleFuncaptcha() throws MalformedURLException, InterruptedException {
        DebugHelper.setVerboseMode(true);

        FunCaptcha api = new FunCaptcha();
        api.setClientKey("1234567890123456789012");
        api.setWebsiteUrl(new URL("http://http.myjino.ru/funcaptcha_test/"));
        api.setWebsitePublicKey("DE0B0BB7-1EE4-4D70-1853-31B835D4506B");
        api.setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/537.36 \" +\n" +
                "(KHTML, like Gecko) Chrome/52.0.2743.116");

        // proxy access parameters
        api.setProxyType(NoCaptcha.ProxyTypeOption.HTTP);
        api.setProxyAddress("xx.xxx.xx.xx");
        api.setProxyPort(8282);
        api.setProxyLogin("login");
        api.setProxyPassword("password");

        if (!api.createTask()) {
            DebugHelper.out(
                    "API v2 send failed. " + api.getErrorMessage(),
                    DebugHelper.Type.ERROR
            );
        } else if (!api.waitForResult()) {
            DebugHelper.out("Could not solve the captcha.", DebugHelper.Type.ERROR);
        } else {
            DebugHelper.out("Result: " + api.getTaskSolution().getToken(), DebugHelper.Type.SUCCESS);
        }
    }
}
*/