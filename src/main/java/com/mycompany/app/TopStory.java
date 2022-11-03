package com.mycompany.app;

import java.util.List;

public class TopStory {
    // story's id
    private String id;

    // story's title
    private String title;

    // list of commenters for a story
    private List<String> commenters;

    private List<CommenterCount> topCommenters;

    public List<CommenterCount> getTopCommenters() {
        return topCommenters;
    }

    public void setTopCommenters(List<CommenterCount> topCommenters) {
        this.topCommenters = topCommenters;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getCommenters() {
        return commenters;
    }

    public void setCommenters(List<String> commenters) {
        this.commenters = commenters;
    }

    @Override
    public String toString() {
        return "TopStory{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", commenters=" + commenters +
                ", topCommenters=" + topCommenters +
                '}';
    }
}
