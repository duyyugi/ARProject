package com.example.arproject.model;

public class TextARContent {
    private int contentID;
    private String text;
    private String font;
    private int size;
    private String color;
    private String backgroundColor;
    private int isTransparent;

    public int getContentID() {
        return contentID;
    }

    public void setContentID(int contentID) {
        this.contentID = contentID;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getFont() {
        return font;
    }

    public void setFont(String font) {
        this.font = font;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public int getIsTransparent() {
        return isTransparent;
    }

    public void setIsTransparent(int isTransparent) {
        this.isTransparent = isTransparent;
    }
}
