package com.axai.axai.ai;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.ChatCompletion;
import com.openai.models.ChatCompletionCreateParams;
import com.openai.models.ChatModel;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Component
public class Ai {

    private final OpenAIClient client;

    public Ai() {
        Dotenv dotenv = Dotenv.configure().filename("openai.env").load();
        this.client = OpenAIOkHttpClient.builder()
                .apiKey(dotenv.get("OPENAI_API_KEY"))
                .build();
    }

    public void closeClient(){
        client.close();
    }

    public String askGPT(String prompt) {
        ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
                .addUserMessage(prompt)
                .model(ChatModel.GPT_4)
                .build();

        CompletableFuture<ChatCompletion> chatCompletionFuture = client.async().chat().completions().create(params);

        try {
            ChatCompletion completion = chatCompletionFuture.get();
            return completion.choices().get(0).message().content().orElseThrow(() -> new RuntimeException("No content"));
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("OpenAI error: " + e.getMessage(), e);
        }
    }
}
