/**
 * description: 用于连接ai，发送网路请求
 * author:Manticore
 * email:3100776336@qq.com
 * date:2026/2/20
 */

package notebook.AI;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class APIClient {
    private final String API_KEY = "sk-833d1e4956a14542959970382b6dd966";
    private final OkHttpClient client;
    private final Gson gson = new Gson();

    public APIClient() {
        client = new OkHttpClient.Builder()
                .connectTimeout(6, TimeUnit.SECONDS)
                .readTimeout(20,TimeUnit.SECONDS).build();
    }

    //输入提示词，获取回复
    public void getResponse(String prompt,String systemPrompt,String url, Callback callback) {
        RequestBodyData bodyData = new RequestBodyData(prompt,systemPrompt);
        String json = gson.toJson(bodyData);
        Log.d("TAG","(requestBody:)-->>" + json);

        //先构建请求体，将提示词转换为json
        RequestBody body = RequestBody.create(MediaType.get("application/json;charset=utf-8"), json);


        //构建请求
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Authorization","Bearer " + API_KEY)
                .addHeader("Content-Type","application/json").build();
        //异步发送请求
        Log.d("TAG","(ask:getResponse)-->>发送请求");
        Log.d("TAG", "(请求:)-->>" + url);
        client.newCall(request).enqueue(callback);
    }


    public static class Message {
        //区分角色，以达到更好的记忆效果
        //"user" "assistant"
        private String role;
        private String content;

        public Message(String role, String content) {
            this.role = role;
            this.content = content;
        }
    }
}
