package com.mycompany.app;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

// Service for displaying top 30 hacker news stories including
// the title and top 10 commenters
public class HackerNewsService {

    private static final int TOP_STORIES_COUNT = 30;
    private static final int TOP_COMMENTER_COUNT = 10;

    public void displayTopStories() throws ExecutionException, InterruptedException {
        // Get list of top stories
        HackerNewsClient.getTopStories()
                .thenCompose(topStoryIds -> {
                    //System.out.println(Arrays.toString(topStoryIds.toArray(String[]::new)));

                    // Only use top 30 story ids
                    List<String> topSubStoryIds = IntStream.range(0, TOP_STORIES_COUNT)
                            .mapToObj(topStoryIds::get).collect(Collectors.toList());

                    List<CompletableFuture<TopStory>> topStoryFutures = topSubStoryIds.stream().map(storyId -> {
                        try {
                            return HackerNewsClient.getItem(storyId)
                                            .thenCompose(story -> {
                                                CompletableFuture<List<String>> commentersFuture = addCommenters(story);

                                                return commentersFuture.thenApply(commenters -> {
                                                        TopStory topStory = new TopStory();
                                                        topStory.setId(story.getId());
                                                        topStory.setTitle(story.getTitle());
                                                        topStory.setCommenters(commenters);
                                                        return topStory;
                                                    });
                                            });
                        } catch (InterruptedException | ExecutionException e) {
                            throw new RuntimeException(e);
                        }
                    }).collect(Collectors.toList());

                    CompletableFuture<Void> done = CompletableFuture
                            .allOf(topStoryFutures.toArray(new CompletableFuture[topStoryFutures.size()]));


                    return done.thenApply(v -> topStoryFutures.stream()
                            .map(CompletableFuture::join).collect(Collectors.toList()));
                }).whenComplete((topStories,e)->{
                    if (e == null) {
                        topStories.forEach(HackerNewsService::setTopCommenters);
                        generateDisplay(topStories);
                        //topStories.forEach(System.out::println);
                    } else {
                        throw new RuntimeException(e);
                    }
                }).join();
    }

    // recursive method to add commenters for an item
    private static CompletableFuture<List<String>> addCommenters(Item item){
        List<String> commentIds = item.getKids();
        if(commentIds != null) {
            List<CompletableFuture<List<String>>> commentersFutureList = commentIds.stream().map(commentId ->
                    {
                        try {
                            return HackerNewsClient.getItem(commentId).thenCompose((comment)->
                                    Objects.requireNonNull(addCommenters(comment)).thenApply(commenters -> {
                                         commenters.add(comment.getBy());
                                 return commenters;
                             }));
                        } catch (InterruptedException | ExecutionException e) {
                            throw new RuntimeException(e);
                        }
                    }
            ).collect(Collectors.toList());

            CompletableFuture<Void> doneCommenters = CompletableFuture
                    .allOf(commentersFutureList.toArray(new CompletableFuture[commentersFutureList.size()]));

            return doneCommenters.thenApply(v -> {
                List<List<String>> commentersList = commentersFutureList.stream().map(CompletableFuture::join).collect(Collectors.toList());
                List<String> commenters = commentersList.stream()
                        .flatMap(Collection::stream)
                        .collect(Collectors.toList());

                return commenters;
            });
        }else {
            return CompletableFuture.supplyAsync(()-> {
                List<String> commenters = new ArrayList<>();
                return commenters;

            });
        }
    }

    // method to set top commenters for a story
    private static void setTopCommenters(TopStory topStory){
        if(topStory.getCommenters() != null && topStory.getCommenters().size() != 0){
            Map<String,CommenterCount> commenterCountMap = new HashMap<>();
            topStory.getCommenters().forEach(commenter->{
                if(commenter!=null) {
                    CommenterCount commenterCount;
                    if (!commenterCountMap.containsKey(commenter)) {
                        commenterCount = new CommenterCount();
                        commenterCount.setId(commenter);
                        commenterCount.setCount(1);
                    } else {
                        commenterCount = commenterCountMap.get(commenter);
                        commenterCount.setCount(commenterCount.getCount() + 1);
                    }
                    commenterCountMap.put(commenter,commenterCount);
                }
            });

            // sort the commenter count in descending order
            List<CommenterCount> mapValues = new ArrayList<>(commenterCountMap.values());
            mapValues.sort(Comparator.comparing(CommenterCount::getCount).reversed());

            // only get at most 10 top commenters
            topStory.setTopCommenters(mapValues.stream().limit(TOP_COMMENTER_COUNT).collect(Collectors.toList()));
        }
    }

    // method to display the top stories and their top commenters on stdout
    private static void generateDisplay(List<TopStory> topStories){
        Map<String,Integer> totalCommenterCountMap = new HashMap<>();
        topStories.forEach(topStory -> {
            if (topStory.getTopCommenters() != null){
                topStory.getTopCommenters().forEach(topCommenter -> {
                    if (!totalCommenterCountMap.containsKey(topCommenter.getId())) {
                        totalCommenterCountMap.put(topCommenter.getId(), topCommenter.getCount());
                    } else {
                        int currCount = totalCommenterCountMap.get(topCommenter.getId());
                        totalCommenterCountMap.put(topCommenter.getId(), currCount + topCommenter.getCount());
                    }
                });
            }
        });

        // display
        System.out.printf("| %-8s | %-12s | %-12s | %-12s | %-12s | %-12s | %-12s | %-12s | %-12s | %-12s | %-12s |%n",
                "Story", "1st Top Commenter", "2nd Top Commenter","3rd Top Commenter","4th Top Commenter","5th Top Commenter",
                "6th Top Commenter","7th Top Commenter","8th Top Commenter","9th Top Commenter",
                "10th Top Commenter");
        System.out.printf("| %-8s | %-12s | %-12s | %-12s | %-12s | %-12s | %-12s | %-12s | %-12s | %-12s | %-12s |%n",
                "------", "----------", "----------","----------","----------","----------",
                "----------","----------","----------","----------",
                "----------");

        topStories.forEach(topStory -> {
            System.out.printf("| %-8s | %-12s | %-12s | %-12s | %-12s | %-12s | %-12s | %-12s | %-12s | %-12s | %-12s |%n",
                    topStory.getTitle(),
                    topStory.getTopCommenters() != null && topStory.getTopCommenters().size() >= 1 ? getCountString(topStory.getTopCommenters(),0,totalCommenterCountMap) :"",
                    topStory.getTopCommenters() != null && topStory.getTopCommenters().size() >= 2 ? getCountString(topStory.getTopCommenters(),1,totalCommenterCountMap) :"",
                    topStory.getTopCommenters() != null && topStory.getTopCommenters().size() >= 3 ? getCountString(topStory.getTopCommenters(),2,totalCommenterCountMap) :"",
                    topStory.getTopCommenters() != null && topStory.getTopCommenters().size() >= 4 ? getCountString(topStory.getTopCommenters(),3,totalCommenterCountMap) :"",
                    topStory.getTopCommenters() != null && topStory.getTopCommenters().size() >= 5 ? getCountString(topStory.getTopCommenters(),4,totalCommenterCountMap) :"",
                    topStory.getTopCommenters() != null && topStory.getTopCommenters().size() >= 6 ? getCountString(topStory.getTopCommenters(),5,totalCommenterCountMap) :"",
                    topStory.getTopCommenters() != null && topStory.getTopCommenters().size() >= 7 ? getCountString(topStory.getTopCommenters(),6,totalCommenterCountMap) :"",
                    topStory.getTopCommenters() != null && topStory.getTopCommenters().size() >= 8 ? getCountString(topStory.getTopCommenters(),7,totalCommenterCountMap) :"",
                    topStory.getTopCommenters() != null && topStory.getTopCommenters().size() >= 9 ? getCountString(topStory.getTopCommenters(),8,totalCommenterCountMap) :"",
                    topStory.getTopCommenters() != null && topStory.getTopCommenters().size() >= 10 ? getCountString(topStory.getTopCommenters(),9,totalCommenterCountMap) :"");
        });

    }

    // method to generate a formatted string associated with top commenter for a story
    private static String getCountString(List<CommenterCount> topCommenters, int indx, Map<String,Integer> totalCommenterCountMap){
        CommenterCount commenterCount = topCommenters.get(indx);
        return commenterCount.getId() + " (" + commenterCount.getCount() + " for story - " +
                totalCommenterCountMap.get(commenterCount.getId()) + " total)";
    }
}

