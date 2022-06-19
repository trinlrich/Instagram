package com.example.instagram;

import com.parse.Parse;
import com.parse.ParseObject;

import android.app.Application;

public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Register your parse models
        ParseObject.registerSubclass(Post.class);
        ParseObject.registerSubclass(Comment.class);

//        ParseObject.registerSubclass(Post.class);
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("dSpWgxTRKBukmyhIVykj82OHtNFhNT0Cuq1D63Tt")
                .clientKey("bPTl05b4dMv7sdY7T96lgUbr46n4T4S5uFaFDGBe")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
