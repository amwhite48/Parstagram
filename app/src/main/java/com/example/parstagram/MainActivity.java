package com.example.parstagram;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    Button btLogout;
    Button btPost;
    RecyclerView rvPosts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btLogout = findViewById(R.id.btLogout);
        btPost = findViewById(R.id.btPost);
        rvPosts = findViewById(R.id.rvPosts);

        btLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // logs out user and navigates to login activity
                ParseUser.logOut();
                ParseUser currentUser = ParseUser.getCurrentUser(); // this will now be null

                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

        btPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // navigates to create post activity
                Intent i = new Intent(MainActivity.this, PostActivity.class);
                startActivity(i);

            }
        });
        
        queryPosts();
    }

    private void queryPosts() {
        // Specify which class to query
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        // include user in query
        query.include(Post.KEY_USER);
        // find all posts
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                // if there are any errors, return
                if(e != null) {
                    Log.e(TAG, "issue with retrieving posts", e);
                    return;
                }
                // iterate through all posts
                for(Post post: posts) {
                    Log.i(TAG, "Post: " + post.getDescription() + ", User: " + post.getUser().getUsername());
                }
            }
        });
    }
}