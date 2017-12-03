package com.example.android.moviebud;

/**
 * Created by Siddhesh on 11/23/2017.
 */

public class ChatMessage {
    int id;
    String message;
    boolean isUrl;

    ChatMessage(int id, String message, boolean url){
        this.id=id;
        this.message=message;
        this.isUrl=url;
    }

    int getId(){
        return id;
    }

    String getData(){
        return message;
    }

    boolean isURL(){
        return isUrl;
    }
}
