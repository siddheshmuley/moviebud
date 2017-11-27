package com.example.android.moviebud;
/**
 * Created by Siddhesh on 11/23/2017.
 */

public class ChatMessage {
    int id;
    String message;

    ChatMessage(int id, String message){
        this.id=id;
        this.message=message;
    }

    int getId(){
        return id;
    }

    String getData(){
        return message;
    }
}
