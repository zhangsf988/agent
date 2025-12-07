//import org.junit.jupiter.api.Test;
//import org.springframework.ai.chat.client.ChatClient;
//import org.springframework.ai.openai.OpenAiChatModel;
//import org.springframework.ai.openai.api.OpenAiApi;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.test.context.SpringBootTest;
//
//@SpringBootTest
//public class AiTest {
//
//    @Value("${spring.ai.openai.api-key}")
//    String apiKey;
//
//    @Value("${spring.ai.openai.base-url}")
//    String baseUrl;
//
//    @Test
//    public void test() {
//        OpenAiChatModel openAiChatModel = OpenAiChatModel.builder()
//                .openAiApi(OpenAiApi.builder().apiKey(apiKey).baseUrl(baseUrl).build())
//                .build();
//        ChatClient chatClient = ChatClient.builder(openAiChatModel).build();
//        String content = chatClient.prompt("你是谁").call().content();
//        System.out.println(content);
//    }
//
//
//}
