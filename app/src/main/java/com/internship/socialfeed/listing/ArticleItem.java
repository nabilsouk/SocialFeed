package com.internship.socialfeed.listing;

import com.internship.socialfeed.components.Article;

/**
 * Created by Nader on 23-Aug-16.
 */

public class ArticleItem extends ListingItem {
    final Article article;

    public ArticleItem(Article article) {
        this.article = article;
        setType(ListingItemType.ARTICLE);
    }

    public Article getArticle() {
        return article;
    }
}
