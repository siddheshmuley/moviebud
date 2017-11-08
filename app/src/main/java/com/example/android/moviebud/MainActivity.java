package com.example.android.moviebud;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity{
    SpeechRecognizer recognizer;
    Intent recognitionIntent;
    TextToSpeech speaker;
    FloatingActionButton speakNow;
    //TextView tv;
    TextView speechBox;
    ListView movieSection;
    boolean isOpen=false,isListening=false;
    RelativeLayout chatLayout;
    public Map<String,List<String>>movies;
    ArrayAdapter<String>movieAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        speakNow = (FloatingActionButton)findViewById(R.id.floatingActionButton);
        speakNow.setSize(FloatingActionButton.SIZE_NORMAL);
        movieSection = (ListView)findViewById(R.id.movieList);
        //tv=(TextView)findViewById(R.id.speech);
        speechBox=(TextView)findViewById(R.id.speechBox);
        chatLayout=(RelativeLayout)findViewById(R.id.chatLayout);

        movies=new HashMap<String,List<String>>();
        movies.put("1980s",new ArrayList<String>(){{
            add("E.T.: The Extra-Terrestrial");
            add("Star Wars: Episode VI - Return of the Jedi");
            add("Star Wars: Episode V - The Empire Strikes Back");
            add("Batman");
            add("Raiders of the Lost Ark");
            add("Ghostbusters");
            add("Beverly Hills Cop");
            add("Back to the Future");
            add("Indiana Jones and the Last Crusade");
            add("Indiana Jones and the Temple of Doom");
        }});
        movies.put("1990s 1990's",new ArrayList<String>(){{
            add("Titanic");
            add("Star Wars: Episode I - The Phantom Menace");
            add("Jurassic Park");
            add("The Lion King");
            add("Forrest Gump");
            add("Independence Day");
            add("The Sixth Sense");
            add("Home Alone");
            add("Men in Black");
            add("Toy Story 2");
        }});
        movies.put("2000s",new ArrayList<String>(){{
            add("Avatar");
            add("The Dark Knight");
            add("Shrek 2");
            add("Pirates of the Caribbean: Dead Man's Chest");
            add("Spider-Man");
            add("Transformers: Revenge of the Fallen");
            add("Star Wars: Episode III - Revenge of the Sith");
            add("The Lord of the Rings: The Return of the King");
            add("Spider-Man 2");
            add("The Passion of the Christ");
        }});
        movies.put("2010s",new ArrayList<String>(){{
            add("Star Wars: Episode VII - The Force Awakens");
            add("Jurassic World");
            add("Marvel's The Avengers");
            add("Rogue One: A Star Wars Story");
            add("Beauty and the Beast");
            add("Finding Dory");
            add("Avengers: Age of Ultron");
            add("The Dark Knight Rises");
            add("The Hunger Games: Catching Fire");
            add("Toy Story 3");
        }});
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
                //tv.setText("");
            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {
                hideMenu();
                //tv.setText(speechBox.getText());
                speechBox.setText("");
            }

            @Override
            public void onError(int i) {
                //recognizer.startListening(recognitionIntent);
                Toast.makeText(getApplicationContext(),"some error happened",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResults(Bundle bundle) {
                ArrayList<String> matches = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                speechBox.setText(matches.get(0));
                showMovieList();
            }

            @Override
            public void onPartialResults(Bundle bundle) {
                ArrayList<String> matches = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                speechBox.setText(matches.get(0));
            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });

        speakNow.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(!isOpen){
                    showMenu();
                    recognizer.startListening(recognitionIntent);
                }
                else{
                    hideMenu();
                    recognizer.stopListening();
                }
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        recognizer.destroy();
    }

    public void showMenu(){
        int right=chatLayout.getRight();
        int left=chatLayout.getLeft();
        int bottom=chatLayout.getBottom();
        int top=chatLayout.getTop();
        int x=speakNow.getLeft();
        int y=speakNow.getBottom();
        int r0=0;
        int r1=(int)Math.hypot(chatLayout.getWidth(),chatLayout.getHeight());
        speakNow.setImageResource(R.drawable.ic_mic_off_white_24dp);

        Animator animator= ViewAnimationUtils.createCircularReveal(chatLayout,x,y,r0,r1);
        chatLayout.setVisibility(View.VISIBLE);
        animator.start();
        isOpen=true;
    }
    public void hideMenu(){
        int right=chatLayout.getRight();
        int left=chatLayout.getLeft();
        int bottom=chatLayout.getBottom();
        int top=chatLayout.getTop();
        int x=speakNow.getLeft();
        int y=speakNow.getBottom();
        int r0=0;
        int r1=(int)Math.hypot(chatLayout.getWidth(),chatLayout.getHeight());
        speakNow.setImageResource(R.drawable.ic_mic_white_24dp);

        Animator animator= ViewAnimationUtils.createCircularReveal(chatLayout,x,y,r1,r0);
        chatLayout.setVisibility(View.INVISIBLE);
        animator.start();
        isOpen=false;
    }

    public void showMovieList(){
        String []text=speechBox.getText().toString().split(" ");
        List<String>result=null;
        String input="";
        for(String k:movies.keySet()){
            for(String s:text){
                if(k.contains(s)){
                    result=movies.get(k);
                    input=s;
                    break;
                }
            }
        }
        if(result!=null){
            movieAdapter=new ArrayAdapter<String>(getApplicationContext(),R.layout.listrow, R.id.row,result);
            movieSection.setAdapter(movieAdapter);
            speak(input);
        }
        else{
            speak("");
        }
    }

    public void speak(final String input){
        speaker = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                speaker.setLanguage(Locale.US);
                CharSequence c="";
                if(!input.equals(""))c="Here is the list of top 10 movies from "+input;
                else c="Sorry, could not find any relevant results from the search";
                speaker.speak(c,TextToSpeech.QUEUE_FLUSH,null,"0");
                hideMenu();
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
}