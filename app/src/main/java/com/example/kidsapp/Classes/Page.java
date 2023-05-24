package com.example.kidsapp.Classes;

import java.io.Serializable;

public class Page implements Serializable{
    private Integer pageNum;
    private Integer image;
    private String text;

    public Page(int pageNum, int image, String text) {
        this.pageNum = pageNum;
        this.image = image;
        this.text = text;
    }

    private String getPackageName() {
        return "com.example.kidsapp";
    }

    public int getPageNum() {
        return pageNum;
    }

    public int getImage() {
        return image;

    }

    public String getText() {
        return text;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public void setImage(Integer image) {
        this.image = image;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Page Number: ").append(pageNum).append("\n");
        sb.append("Image: ").append(image).append("\n");
        sb.append("Text: ").append(text);
        return sb.toString();
    }
}
