package com.herokuapp.ezhao.codepathinstagram;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class PhotosActivity extends ActionBarActivity {
    private ArrayList<InstagramPhoto> photos;
    private InstagramPhotosAdapter photosAdapter;
    private static final String CLIENT_ID = "7a59905f20f5494fa997a4b0a7f47708";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);

        // Link listview with adapter to our photosAdapter and photos array
        photos = new ArrayList<InstagramPhoto>();

        // Link adapter
        photosAdapter = new InstagramPhotosAdapter(this, photos);
        ListView lvPhotos = (ListView) findViewById(R.id.lvPhotosList);
        lvPhotos.setAdapter(photosAdapter);

        // Fetch the popular photos
        fetchPopularPhotos();
    }

    private void fetchPopularPhotos() {
        String apiUrl = "https://api.instagram.com/v1/media/popular?client_id=" + CLIENT_ID;

        // Create the network client
        AsyncHttpClient client = new AsyncHttpClient();

        // Trigger the GET request
        client.get(apiUrl, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray photosJSON = null;
                try {
                    photosJSON = response.getJSONArray("data");
                    for (int i=0; i < photosJSON.length(); i++) {
                        JSONObject photoJSON = photosJSON.getJSONObject(i);
                        InstagramPhoto photo = new InstagramPhoto();

                        // Parse JSON
                        photo.setUsername(photoJSON.getJSONObject("user").getString("username"));
                        photo.setUserImageUrl(photoJSON.getJSONObject("user").getString("profile_picture"));
                        photo.setImageUrl(photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getString("url"));
                        photo.setImageHeight(photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getInt("height"));
                        if (!photoJSON.isNull("caption")) {
                            photo.setCaption(photoJSON.getJSONObject("caption").getString("text"));
                        }
                        photo.setLikeCount(photoJSON.getJSONObject("likes").getInt("count"));
                        photo.setCommentCount(photoJSON.getJSONObject("comments").getInt("count"));
                        JSONArray commentsJSON = photoJSON.getJSONObject("comments").getJSONArray("data");
                        for (int j = 0; j < commentsJSON.length(); j++) {
                            JSONObject commentJSON = commentsJSON.getJSONObject(j);
                            String username = commentJSON.getJSONObject("from").getString("username");
                            String text = commentJSON.getString("text");
                            photo.addComment(username, text);
                        }
                        photo.setCreatedTime(Long.parseLong(photoJSON.getString("created_time")));

                        photos.add(photo);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Update our adapter
                photosAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("emily_is_debugging FAILED", responseString);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_photos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
