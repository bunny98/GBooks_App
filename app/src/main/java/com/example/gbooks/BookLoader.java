package com.example.gbooks;

import android.content.Context;
import android.content.AsyncTaskLoader;

import java.util.List;

public class BookLoader extends AsyncTaskLoader<List<BookDetails>> {
    private String mUrl;

    public BookLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<BookDetails> loadInBackground() {
        if(mUrl==null){
            return  null;
        }

        List<BookDetails> result = QueryUtils.fetchBookData(mUrl);
        return result;
    }
}
