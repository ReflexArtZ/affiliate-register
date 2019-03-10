package de.reflex.chaturbate.captchaBypass.Api;


import de.reflex.chaturbate.captchaBypass.AnticaptchaBase;
import de.reflex.chaturbate.captchaBypass.ApiResponse.TaskResultResponse;
import de.reflex.chaturbate.captchaBypass.Helper.DebugHelper;
import de.reflex.chaturbate.captchaBypass.IAnticaptchaTaskProtocol;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

public class NoCaptchaProxyless extends AnticaptchaBase implements IAnticaptchaTaskProtocol {
    private URL websiteUrl;
    private String websiteKey;
    private String websiteSToken;

    public void setWebsiteUrl(URL websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    public void setWebsiteKey(String websiteKey) {
        this.websiteKey = websiteKey;
    }

    public void setWebsiteSToken(String websiteSToken) {
        this.websiteSToken = websiteSToken;
    }

    @Override
    public JSONObject getPostData() {
        JSONObject postData = new JSONObject();

        try {
            postData.put("type", "NoCaptchaTaskProxyless");
            postData.put("websiteURL", websiteUrl.toString());
            postData.put("websiteKey", websiteKey);
            postData.put("websiteSToken", websiteSToken);
        } catch (JSONException e) {
            DebugHelper.out("JSON compilation error: " + e.getMessage(), DebugHelper.Type.ERROR);

            return null;
        }

        return postData;
    }

    @Override
    public TaskResultResponse.SolutionData getTaskSolution() {
        return taskInfo.getSolution();
    }
}
