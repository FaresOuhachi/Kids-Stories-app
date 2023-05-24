package com.example.kidsapp.Classes;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class StoryStructure implements Serializable {
    private int imageResource;
    private Integer id;
    private String title;
    private String author;
    private List<Page> pages;

    private String favStatus;

    public StoryStructure(Integer id, int imageResource,String title, String author,String favStatus) {
        this.id = id;
        this.imageResource = imageResource;
        this.title = title;
        this.author = author;
        this.pages = new ArrayList<>();
        this.favStatus = favStatus;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    public void setPages(List<Page> pages) {
        this.pages = pages;
    }

    public List<Page> getPages() {
        return pages;
    }

    public void addPage(Page page) {
        pages.add(page);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("StoryStructure ID: ").append(id).append("\n");
        sb.append("Title: ").append(title).append("\n");
        sb.append("Author: ").append(author).append("\n");
        sb.append("Pages:\n");

        for (Page page : pages) {
            sb.append(page.toString()).append("\n");
        }

        return sb.toString();
    }
    public String getFavStatus() {
        return favStatus;
    }

    public void setFavStatus(String favStatus) {
        this.favStatus = favStatus;
    }

}
