package com.mycompany.app;

import java.util.List;

// this class represents the item object of the Hackernews API
public class Item {
    // The item's unique id.
    private String id;

    private boolean deleted;

    // The type of item. One of "job", "story", "comment", "poll", or "pollopt".
    private String type;

    // The username of the item's author
    private String by;

    private String time;

    private String text;
    private boolean dead;
    private String parent;
    private String poll;

    // The ids of the item's comments, in ranked display order.
    private List<String> kids;

    private String url;
    private int score;

    // The title of the story, poll or job. HTML.
    private String title;

    private List<String> parts;

    private int descendants;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBy() {
        return by;
    }

    public void setBy(String by) {
        this.by = by;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isDead() {
        return dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getPoll() {
        return poll;
    }

    public void setPoll(String poll) {
        this.poll = poll;
    }

    public List<String> getKids() {
        return kids;
    }

    public void setKids(List<String> kids) {
        this.kids = kids;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getParts() {
        return parts;
    }

    public void setParts(List<String> parts) {
        this.parts = parts;
    }

    public int getDescendants() {
        return descendants;
    }

    public void setDescendants(int descendants) {
        this.descendants = descendants;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id='" + id + '\'' +
                ", deleted=" + deleted +
                ", type='" + type + '\'' +
                ", by='" + by + '\'' +
                ", time='" + time + '\'' +
                ", text='" + text + '\'' +
                ", dead=" + dead +
                ", parent='" + parent + '\'' +
                ", poll='" + poll + '\'' +
                ", kids=" + kids +
                ", url='" + url + '\'' +
                ", score=" + score +
                ", title='" + title + '\'' +
                ", parts=" + parts +
                ", descendants=" + descendants +
                '}';
    }
}
