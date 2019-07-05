package com.example.gbooks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class QueryUtils {
    public static final String LOG_TAG = QueryUtils.class.getName();

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e("Building URL: ", "Problem building the URL ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static List<BookDetails> extractFeaturesFromJson(String BooksJSON) {
        if (TextUtils.isEmpty(BooksJSON)) {
            return null;
        }
        List<BookDetails> Books = new ArrayList<>();

       try {
           JSONObject baseJsonResponse = new JSONObject(BooksJSON);
           Log.v(LOG_TAG, "baseJSON");
           JSONArray BooksArray = baseJsonResponse.getJSONArray("items");
           Log.v(LOG_TAG, "ItemsArray");

           for (int i = 0; i < BooksArray.length(); i++) {
               try {
                   JSONObject currentBook = BooksArray.getJSONObject(i);
                   Log.v(LOG_TAG, "index: " + i);
                   JSONObject volumeInfo = currentBook.getJSONObject("volumeInfo");
                   Log.v(LOG_TAG, "VolumeInfo");

                   String title = volumeInfo.getString("title");
                   Log.v(LOG_TAG, "title");

                   JSONArray arrJson = volumeInfo.getJSONArray("authors");
                   Log.v(LOG_TAG, "authorsJsonArray");
                   String[] arr = new String[arrJson.length()];
                   for (int j = 0; j < arrJson.length(); j++) {
                       arr[j] = arrJson.getString(j);
                       Log.v("arrJson: ", arr[j]);
                   }
                   Log.v(LOG_TAG, "AuthorsArray");

                   String description = volumeInfo.getString("description");
                   Log.v(LOG_TAG, "Description");

                   JSONArray catJson = volumeInfo.getJSONArray("categories");
                   Log.v(LOG_TAG, "categoriesJson");
                   String[] catArr = new String[catJson.length()];
                   for (int j = 0; j < catJson.length(); j++)
                       catArr[j] = catJson.getString(j);
                   Log.v(LOG_TAG, "categoriesArray");


                   JSONObject imgJson = volumeInfo.getJSONObject("imageLinks");
                   Log.v(LOG_TAG, "imageJsonObj");
                   String imgUrl = imgJson.getString("smallThumbnail");
                   URL url = createUrl(imgUrl);
                   Bitmap bmp = null;
                   try {
                       HttpURLConnection urlConnection = null;
                       InputStream inputStream = null;
                       try {
                           urlConnection = (HttpURLConnection) url.openConnection();
                           if (urlConnection.getResponseCode() == 200) {
                               inputStream = urlConnection.getInputStream();
                               bmp = BitmapFactory.decodeStream(inputStream, null, new BitmapFactory.Options());
                           } else {
                               Log.e(LOG_TAG, "Error IMAGE response code: " + urlConnection.getResponseCode());
                           }
                       } catch (IOException e) {
                           Log.e(LOG_TAG, "Problem retrieving IMAGE", e);
                       } finally {
                           if (urlConnection != null) {
                               urlConnection.disconnect();
                           }
                           if (inputStream != null) {
                               inputStream.close();
                           }
                       }
                   } catch (IOException e) {
                   }


                   Log.v(LOG_TAG, "getting Image");

                   BookDetails book = new BookDetails(title, bmp, arr, catArr, description);
                   Log.v(LOG_TAG, "OBJ: " + i + " created!");

                   Books.add(book);
                   Log.v(LOG_TAG, "OBJ:" + i + " added!");


               } catch (JSONException e) {
                   Log.e(LOG_TAG, "ERROR PARSING JSON");
               }
           }
       }
       catch (JSONException e){ }



        return Books;
    }

    public static List<BookDetails> fetchBookData(String requestUrl) {
        URL url = createUrl(requestUrl);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }
        List<BookDetails> Books = extractFeaturesFromJson(jsonResponse);
        return Books;
    }

}
