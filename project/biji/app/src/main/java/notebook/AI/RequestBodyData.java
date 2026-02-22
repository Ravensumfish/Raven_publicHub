package notebook.AI;

import com.example.biji.R;

import java.util.ArrayList;
import java.util.List;

//将提示词与一些信息一起包装成类，转化为json
public class RequestBodyData {
    List<APIClient.Message> messages;
    String model = "deepseek-chat";

        RequestBodyData(String prompt,String systemPrompt) {
            this.messages = new ArrayList<>();
            this.messages.add(new APIClient.Message("system", systemPrompt));
            this.messages.add(new APIClient.Message("user",prompt));
        }

}
