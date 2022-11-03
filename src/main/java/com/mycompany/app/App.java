package com.mycompany.app;

import java.util.concurrent.ExecutionException;

/**
 * Hello world!
 *
 */
public class App 
{
    // entry point for the application
    public static void main( String[] args ) throws ExecutionException, InterruptedException {
       HackerNewsService hackerNewsService = new HackerNewsService();
       hackerNewsService.displayTopStories();
    }
}
