package com.example.parstagram.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.parstagram.Post;
import com.example.parstagram.PostsAdapter;
import com.example.parstagram.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;


public class PostsFragment extends Fragment {

    public static final String TAG = "Posts Fragment";
    RecyclerView rvPosts;
    PostsAdapter adapter;
    List<Post> allPosts;
    SwipeRefreshLayout swipeContainer;

    public PostsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_posts, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        rvPosts = view.findViewById(R.id.rvPosts);

        //initialize array that will hold all posts and create posts adapter
        allPosts = new ArrayList<>();
        adapter = new PostsAdapter(getContext(), allPosts);

        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i(TAG, "refreshing posts...");
                queryPosts();
            }
        });

        // set up recyclerview and retrieve posts
        // set adapter on recyclerview
        rvPosts.setAdapter(adapter);
        //set layout manager
        rvPosts.setLayoutManager(new LinearLayoutManager(getContext()));

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