package com.example.android.moviebud;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{
    Button recBtn;
    SpeechRecognizer recognizer;
    Intent recognitionIntent;
    private final int SPEECH_RECOGNITION_CODE=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recBtn = (Button) findViewById(R.id.recBtn);
        recBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recognitionIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                //recognizer.startListening(recognitionIntent);
                recognitionIntent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                        "Say something...");
                try {
                    startActivityForResult(recognitionIntent,SPEECH_RECOGNITION_CODE);
                } catch (ActivityNotFoundException a) {
                    Toast.makeText(getApplicationContext(),
                            "Sorry! Speech recognition is not supported in this device.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==SPEECH_RECOGNITION_CODE){
            TextView tv=(TextView)findViewById(R.id.speech);
            ArrayList<String>al=data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            tv.setText(al.get(0));

        }
    }
}