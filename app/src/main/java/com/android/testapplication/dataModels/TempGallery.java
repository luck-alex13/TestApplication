package com.android.testapplication.dataModels;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * TestApplication
 * Created by Alex on 18.05.2017.
 * contact on luck.alex13@gmail.com
 * © Alexander Novikov 2017
 */

public class TempGallery extends RealmObject {

    @SerializedName("format")
    @Expose
    private String format;
    @SerializedName("width")
    @Expose
    private int width;
    @SerializedName("height")
    @Expose
    private int height;
    @SerializedName("filename")
    @Expose
    private String filename;

    @PrimaryKey
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("author")
    @Expose
    private String author;
    @SerializedName("author_url")
    @Expose
    private String authorUrl;
    @SerializedName("post_url")
    @Expose
    private String postUrl;

    /**
     * No args constructor for use in serialization
     *
     */
    public TempGallery() {
    }


    /**
     *
     * @param id
     * @param author
     * @param height
     * @param postUrl
     * @param width
     * @param authorUrl
     * @param filename
     * @param format
     */
    public TempGallery(int id, String filename, String format, int width, int height, String author, String authorUrl, String postUrl) {
        super();
        this.format = format;
        this.width = width;
        this.height = height;
        this.filename = filename;
        this.id = id;
        this.author = author;
        this.authorUrl = authorUrl;
        this.postUrl = postUrl;
    }

    public static TempGallery copyFrom(GalleryModel galleryModel){
        return new TempGallery(galleryModel.getId(),galleryModel.getFilename(),galleryModel.getFormat(),galleryModel.getWidth(),galleryModel.getHeight(),galleryModel.getAuthor(),galleryModel.getAuthorUrl(),galleryModel.getPostUrl());
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthorUrl() {
        return authorUrl;
    }

    public void setAuthorUrl(String authorUrl) {
        this.authorUrl = authorUrl;
    }

    public String getPostUrl() {
        return postUrl;
    }

    public void setPostUrl(String postUrl) {
        this.postUrl = postUrl;
    }
}
