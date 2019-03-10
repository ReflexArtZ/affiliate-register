package de.reflex.chaturbate.captchaBypass;


import de.reflex.chaturbate.captchaBypass.ApiResponse.TaskResultResponse;
import org.json.JSONObject;

public interface IAnticaptchaTaskProtocol {
    JSONObject getPostData();

    TaskResultResponse.SolutionData getTaskSolution();
}
