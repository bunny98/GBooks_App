package com.example.gbooks;

import android.graphics.Bitmap;

public class BookDetails {
    private String mName;
    private Bitmap mImgBmp;
    private String [] mAuthors;
    private String [] mCategories;
    private String mDescription;

    public BookDetails(String mName, Bitmap mImgBmp, String[] mAuthors, String[] mCategories, String mDescription) {
        this.mName = mName;
        this.mImgBmp = mImgBmp;
        this.mAuthors = mAuthors;
        this.mCategories = mCategories;
        this.mDescription = mDescription;
    }

    public String getmName() {
        return mName;
    }

    public Bitmap getmImgBmp() {
        return mImgBmp;
    }

    public String[] getmAuthors() {
        return mAuthors;
    }

    public String[] getmCategories() {
        return mCategories;
    }

    public String getmDescription() {
        return mDescription;
    }

}
