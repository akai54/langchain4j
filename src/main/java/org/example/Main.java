package org.example;

import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import dev.langchain4j.model.input.Prompt;
import dev.langchain4j.model.input.PromptTemplate;
import io.github.cdimascio.dotenv.Dotenv;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.configure().directory("./").load();
        String apiKey = dotenv.get("API_KEY");

        ChatLanguageModel model = GoogleAiGeminiChatModel.builder()
                .apiKey(apiKey)
                .modelName("gemini-1.5-flash")
                .temperature(0.3)
                .build();

        /*PromptTemplate promptTemplate = PromptTemplate
                .from("Tell me a {{adjective}} joke about {{content}}.");
        Map<String, Object> variables = new HashMap<>();
        variables.put("adjective", "scary");
        variables.put("content", "computers");

        Prompt prompt = promptTemplate.apply(variables);
        String response = model.generate(prompt.text());
        System.out.println(response);*/

        ChatMemory chatMemory = MessageWindowChatMemory.withMaxMessages(100);
        chatMemory.add(UserMessage.userMessage("Hello, my name is Besher."));
        AiMessage answer = model.generate(chatMemory.messages()).content();
        System.out.println(answer.text());

        chatMemory.add(answer);
        chatMemory.add(UserMessage.userMessage("What is my name?"));
        AiMessage answerWithName = model.generate(chatMemory.messages()).content();
        System.out.println(answerWithName.text());
    }
}