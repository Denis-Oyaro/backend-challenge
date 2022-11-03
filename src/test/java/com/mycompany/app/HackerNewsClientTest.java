package com.mycompany.app;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.*;

public class HackerNewsClientTest {

    private HackerNewsClient hackerNewsClient;

    @Before
    public void setUp(){
        hackerNewsClient = Mockito.spy(new HackerNewsClient());
    }

    @Test
    public void TopStoriesTest() throws ExecutionException, InterruptedException {
        // get top stories
        List<String> topStories = hackerNewsClient.getTopStories().join();
        assertNotNull(topStories);
        assertNotEquals(topStories.size(),0);

        // get item
        Item item = hackerNewsClient.getItem(topStories.get(0)).join();
        assertNotNull(item);
        assertEquals(item.getId(),topStories.get(0));
    }
}
