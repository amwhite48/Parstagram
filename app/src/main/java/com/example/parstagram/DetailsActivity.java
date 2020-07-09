package com.example.parstagram;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.parse.ParseFile;

import org.parceler.Parcels;

public class DetailsActivity extends AppCompatActivity {

    public static final String TAG = "DetailsActivity";
    TextView tvUsername;
    TextView tvDescription;
    TextView tvTimestamp;
    ImageView ivPost;
    Post post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        // unwrap post sent from intent and main activity key
        post = Parcels.unwrap(getIntent().getParcelableExtra(Post.class.getSimpleName()));
        Log.i(TAG, "Showing post details for: " + post.getDescription());

        tvUsername = findViewById(R.id.tvUsernameD);
        tvDescription = findViewById(R.id.tvDescriptionD);
        tvTimestamp = findViewById(R.id.tvTimestamp);
        ivPost = findViewById(R.id.ivImageD);

        // set details into views
        tvUsername.setText(post.getUser().getUsername());
        tvDescription.setText(post.getDescription());
        if (post.getCreatedAt() != null) {
            tvTimestamp.setText(post.getCreatedAt().toString());
        }

        ParseFile image = post.getImage();
        if(image != null) {
            Glide.with(this).load(image.getUrl()).into(ivPost);
        }


    }
}