package com.example.android.moviebud;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{
    SpeechRecognizer recognizer;
    Intent recognitionIntent;
    //TextToSpeech speaker;
    FloatingActionButton speakNow;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        speakNow = (FloatingActionButton)findViewById(R.id.floatingActionButton);
        speakNow.setSize(FloatingActionButton.SIZE_NORMAL);
        tv=(TextView)findViewById(R.id.speech);
        //just testing
        recognizer=SpeechRecognizer.createSpeechRecognizer(this);
        recognitionIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognitionIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        recognitionIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, this.getPackageName());
        recognitionIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
        recognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int i) {
                //recognizer.startListening(recognitionIntent);
                Toast.makeText(getApplicationContext(),"some error happened",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResults(Bundle bundle) {
                ArrayList<String> matches = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                tv.setText(matches.get(0));
            }

            @Override
            public void onPartialResults(Bundle bundle) {
                ArrayList<String> matches = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                tv.setText(matches.get(0));
            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });

        speakNow.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                recognizer.startListening(recognitionIntent);
                //ViewAnimationUtils.createCircularReveal();
            }
        });


        /*playBtn = (Button) findViewById(R.id.playBtn);
        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                speaker = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int i) {
                        speaker.setLanguage(Locale.US);
                        CharSequence c=((TextView)findViewById(R.id.speech)).getText().toString();
                        speaker.speak(c,TextToSpeech.QUEUE_FLUSH,null,"0");
                    }
                });

                speaker.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                    @Override
                    public void onStart(String s) {

                    }

                    @Override
                    public void onDone(String s) {
                        speaker.shutdown();
                    }

                    @Override
                    public void onError(String s) {

                    }
                });
            }
        });*/
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        recognizer.destroy();
    }
}