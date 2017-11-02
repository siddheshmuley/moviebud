package com.example.android.moviebud;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity{
    Button recBtn,playBtn;
    SpeechRecognizer recognizer;
    Intent recognitionIntent;
    TextToSpeech speaker;
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

        playBtn = (Button) findViewById(R.id.playBtn);
        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speaker = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int i) {
                        speaker.setLanguage(Locale.US);
                        CharSequence c=((TextView)findViewById(R.id.speech)).getText().toString();
                        speaker.speak(c,TextToSpeech.QUEUE_FLUSH,null,"0");
                        speaker.shutdown();
                        speaker.stop();
                    }
                });
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

    public void onPause(){
        if(speaker !=null){
            speaker.stop();
            speaker.shutdown();
        }
        super.onPause();
    }
}