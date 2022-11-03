package com.mycompany.app;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

// Implementation of http calls to the HackerNews API
public class HackerNewsClient {

    private final static String topStoriesUri = "https://hacker-news.firebaseio.com/v0/topstories.json";
    private final static String itemUri = "https://hacker-news.firebaseio.com/v0/item/";

    public CompletableFuture<List<String>> getTopStories() throws InterruptedException, ExecutionException {
        // create http client
        HttpClient client = HttpClient.newHttpClient();

        // create a top stories request
        HttpRequest request = HttpRequest.newBuilder(
                        URI.create(topStoriesUri))
                .header("accept", "application/json")
                .build();

        // return a completableFuture object whose result is the response body
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {
                    ObjectMapper om = new ObjectMapper();
                    try {
                        return om.readValue(response.body(),new TypeReference<List<String>>(){});
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    public CompletableFuture<Item> getItem(String itemId) throws InterruptedException, ExecutionException {
        // create http client
        HttpClient client = HttpClient.newHttpClient();

        // create an item request
        HttpRequest request = HttpRequest.newBuilder(
                        URI.create(itemUri + itemId + ".json"))
                .header("accept", "application/json")
                .build();

        // return a completableFuture object whose result is the response body
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {
                    ObjectMapper om = new ObjectMapper();
                    try {
                        return om.readValue(response.body(),Item.class);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
    }
}
