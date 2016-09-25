package com.internship.socialfeed.data;

import android.os.Handler;

import com.internship.socialfeed.components.Article;

import java.util.ArrayList;
import java.util.GregorianCalendar;

/**
 * Contains several methods to return random values
 * for several purposes
 */
public class ArticlesProvider {
    private final OnArticlesProviderListener listener;
    ArrayList<Article> arrayData = new ArrayList<>();
    private boolean isLoading = false;
    private int index = 1;
    int count = 10;
    final int limit;

    public ArticlesProvider(OnArticlesProviderListener listener) {
        this(listener, 10);
    }

    public ArticlesProvider(OnArticlesProviderListener listener, int limit) {
        this.listener = listener;
        this.limit = limit;
    }

    /**
     * BroadCasts a new set of articles.
     */
    public void generateNextArticlesSet() {
        // check if not occupied
        if (isLoading) return;
        // set flag
        isLoading = true;
        // update state
        broadcastState(isLoading);

        final ArrayList<Article> arrayNew = generateNewList();
        arrayData.addAll(arrayNew);

        //excutes code after 5 sec delay
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // broadcast result
                broadcastComplete(arrayNew, arrayNew.size());
                // update flag
                isLoading = false;
                // update state
                broadcastState(isLoading);
            }
        }, 5000);
    }

    /**
     * Generates an arrayList of Articles of random names
     * created at from a combination of random first name and
     * random last name.
     */
    private ArrayList<Article> generateNewList() {
        final ArrayList<Article> arrayArticles = new ArrayList<>();
        String[] fnames = {"Nader", "Nabil", "Abed", "Wassim", "Ghassan", "Mohamad", "Alaa", "Samar", "Lara", "Mark", "Bill", "Nizar", "Shereen", "Alan", "Stephen", "Napoleon"};
        String[] lnames = {"Baltaji", "Souk", "Halawi", "Jardali", "Khalifeh", "Hashem", "Zein", "Ghanem", "Sleiman", "Zuckerberg", "Page", "Jobs", "Gates", "Turing", "Hawking", "Hill"};

        while (index <= count) {
            Article article = new Article(index, fnames[randBetween(0, fnames.length - 1)] + " " + lnames[randBetween(0, fnames.length - 1)], title(), date());
            arrayArticles.add(article);
            index++;
        }
        count += 10;

        return arrayArticles;
    }


    /**
     * Generates a random date between 2005 and 2016
     * of the form Day-Month-Year
     */
    public String date() {

        GregorianCalendar gc = new GregorianCalendar();

        int year = randBetween(2005, 2016);

        gc.set(gc.YEAR, year);

        int dayOfYear = randBetween(1, gc.getActualMaximum(gc.DAY_OF_YEAR));

        gc.set(gc.DAY_OF_YEAR, dayOfYear);

        return (gc.get(gc.DAY_OF_MONTH) + "-" + (gc.get(gc.MONTH) + 1) + "-" + gc.get(gc.YEAR));

    }

    /**
     * Generates a String of random characters of length 20 chars
     * and takes care of spaces and letter capitalization of first character
     */

    public String title() {
        String title = "";
        char a;
        for (int i = 0; i < 20; i++) {
            int x = randBetween(0, 4);
            if (x == 2 && i > 2 && title.charAt(title.length() - 1) != ' ' && title.charAt(title.length() - 2) != ' ') {
                a = ' ';
            } else {
                a = (char) randBetween(97, 122);
                if (i == 0) {
                    a -= 32;
                }
                if (i > 0 && title.charAt(title.length() - 1) == ' ') {
                    a -= 32;
                }

            }
            title += a;
        }
        title += ":";
        return title;
    }

    /**
     * returns a random integer between two numbers
     * @param start is the first number
     * @param end   is the second number
     */

    public int randBetween(int start, int end) {
        return start + (int) Math.round(Math.random() * (end - start));
    }

    public boolean isLoading() {
        return isLoading;
    }

    // CALLBACKS //

    public interface OnArticlesProviderListener {
        /**
         * Triggered once the requested data is broadcasted
         * back by the provider.
         *
         * @param arrayArticles The requested data set.
         */
        void onComplete(ArrayList<Article> arrayArticles, int currentCount);

        /**
         * Indicates the state of the provider.
         *
         * @param loading true if currently executing,
         *                false otherwise.
         */
        void onStateChanged(boolean loading);
    }

    private void broadcastState(boolean loading) {
        if (listener != null) listener.onStateChanged(loading);
    }

    private void broadcastComplete(ArrayList<Article> arrayArticles, int currentCount) {
        if (listener != null) listener.onComplete(arrayArticles, currentCount);
    }
}
