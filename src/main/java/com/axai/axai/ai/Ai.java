package com.axai.axai.ai;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.ChatCompletion;
import com.openai.models.ChatCompletionCreateParams;
import com.openai.models.ChatModel;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * Ai class handles communication with OpenAI's API using the GPT-4 model.
 * It loads the API key from an external .env file and provides methods for sending prompts and receiving responses.
 */
@Component
public class Ai {

    private final OpenAIClient client;

    /**
     * Constructor initializes the OpenAI client by loading the API key from the 'openai.env' file.
     * Throws a NullPointerException if the key is missing.
     */
    public Ai() {
        Dotenv dotenv = Dotenv.configure().filename("openai.env").load();
        this.client = OpenAIOkHttpClient.builder()
                .apiKey(Objects.requireNonNull(dotenv.get("OPENAI_API_KEY")))
                .build();
    }

    /**
     * Closes the OpenAI client and releases resources.
     * Should be called when the application shuts down.
     */
    public void closeClient(){
        client.close();
    }

    /**
     * Sends a prompt to the GPT-4 model asynchronously and returns the generated response as a String.
     *
     * @param prompt the input prompt for the AI model
     * @return the content of the first response choice
     * @throws RuntimeException if the request fails or no content is returned
     */
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
