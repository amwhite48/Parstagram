package com.example.parstagram;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    Button btLogout;
    Button btPost;
    RecyclerView rvPosts;
    PostsAdapter adapter;
    List<Post> allPosts;
    SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btLogout = findViewById(R.id.btLogout);
        btPost = findViewById(R.id.btPost);
        rvPosts = findViewById(R.id.rvPosts);

        //initialize array that will hold all posts and create posts adapter
        allPosts = new ArrayList<>();
        adapter = new PostsAdapter(this, allPosts);

        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i(TAG, "refreshing posts...");
                queryPosts();
            }
        });

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

        // set up recyclerview and retrieve posts
        // set adapter on recyclerview
        rvPosts.setAdapter(adapter);
        //set layout manager
        rvPosts.setLayoutManager(new LinearLayoutManager(this));

        queryPosts();

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }


    private void queryPosts() {
        // Specify which class to query
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        // include user in query
        query.include(Post.KEY_USER);
        // set query limit to 20 posts per query
        query.setLimit(20);
        // order posts by newest posts first
        query.addDescendingOrder(Post.KEY_CREATED_AT);
        // find all posts asynchronously
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                // if there are any errors, return
                if(e != null) {
                    Log.e(TAG, "issue with retrieving posts", e);
                    return;
                }
                // clear old posts from adapter
                adapter.clear();

                // iterate through all posts to ensure they are being retrieved
                for(Post post: posts) {
                    Log.i(TAG, "Post: " + post.getDescription() + ", User: " + post.getUser().getUsername());
                }

                // save retrieved posts and add to adapter
                adapter.addAll(posts);

                // stop refreshing if querying posts on a refresh
                swipeContainer.setRefreshing(false);
            }
        });
    }
}