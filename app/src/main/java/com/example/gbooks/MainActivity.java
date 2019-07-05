package com.example.gbooks;

import android.app.LoaderManager.LoaderCallbacks;
import android.content.Loader;
import android.support.v4.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderCallbacks<List<BookDetails>> {
    private int id = 0;
    private String RequestUrl;
    private BookDetailsAdapter mAdapter;
    LoaderManager loaderManager;

    @Override
    public Loader<List<BookDetails>> onCreateLoader(int id, Bundle args) {
        return new BookLoader(this, RequestUrl);
    }

    @Override
    public void onLoadFinished(Loader<List<BookDetails>> loader, List<BookDetails> data) {
        mAdapter.clear();
        if(data!=null && !data.isEmpty()){
            mAdapter.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<BookDetails>> loader) {
        mAdapter.clear();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void onClick(View view){
        RequestUrl = "https://www.googleapis.com/books/v1/volumes?q=";
        TextView book = (TextView) findViewById(R.id.Book_input);
        String bookName = book.getText().toString();
//        bookName = bookName.replaceAll("\\s+","");
        Log.v("Main Activity", bookName);
        RequestUrl+=bookName + "&maxResults=10";
        Log.v("Main Activity", RequestUrl);
        final ArrayList<BookDetails> Books = new ArrayList<BookDetails>();
        mAdapter = new BookDetailsAdapter(MainActivity.this, Books);
        ListView BooksListView = (ListView) findViewById(R.id.list);
        BooksListView.setAdapter(mAdapter);
        getLoaderManager().restartLoader(id, null, MainActivity.this);

    }



}
