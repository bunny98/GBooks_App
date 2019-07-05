package com.example.gbooks;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class BookDetailsAdapter extends ArrayAdapter {
    public static final String LOG_TAG = BookDetailsAdapter.class.getName();

    public BookDetailsAdapter(Context context, ArrayList<BookDetails> bookDetails) {
        super(context, 0, bookDetails);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listView = convertView;
        if (listView == null) {
            listView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }
        BookDetails currentBook = (BookDetails) getItem(position);

        ImageView imageView = (ImageView) listView.findViewById(R.id.image);
        imageView.setImageBitmap(currentBook.getmImgBmp());

        TextView bookName = (TextView) listView.findViewById(R.id.BookName);
        bookName.setText(currentBook.getmName());

        TextView authorsView = (TextView) listView.findViewById(R.id.authors);
        String[] authors = currentBook.getmAuthors();
        String AuthorStr = TextUtils.join(",", authors);
        authorsView.setText(AuthorStr);

        TextView genre = (TextView) listView.findViewById(R.id.categories);
        String [] categories = currentBook.getmCategories();
        String genreStr = TextUtils.join(",", categories);
        genre.setText(genreStr);

        TextView description = (TextView) listView.findViewById(R.id.description);
        description.setText(currentBook.getmDescription());

        return listView;

    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e("Building URL: ", "Problem building the URL ", e);
        }
        return url;
    }
}
