package com.internship.socialfeed.components;

import java.io.Serializable;

/**
 * Created by Nader on 12-Aug-16.
 *
 * <p>
 *     Class holding data related to a local article.
 * </p>
 */
public class Article implements Serializable {
    private int id;
    private String authorName;
    private String title;
    private String date;

    /**
     *
    * @param authorName the name of the author of the post
     *@param title is the title of the post
    * @param date is the creation date of the post
     */
    public Article(int id, String authorName, String title, String date) {
        this.id = id;
        this.authorName = authorName;
        this.title = title;
        this.date = date;
    }

    public String getAuthorName() {
        return authorName;
    }

    public int getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getTitle() {
        return title;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
