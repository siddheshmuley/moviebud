package com.example.android.moviebud;

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
import android.widget.HorizontalScrollView;
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
    TextView speechBox;
    ListView chatView;
    HorizontalScrollView scrollView;
    boolean isOpen=false,isListening=false;
    RelativeLayout chatLayout;
    public Map<String,List<String>>movies;
    MessageAdapter movieAdapter;
    List<ChatMessage> messageList;
    String currentText="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        speakNow = (FloatingActionButton)findViewById(R.id.floatingActionButton);
        speakNow.setSize(FloatingActionButton.SIZE_NORMAL);
        chatView = findViewById(R.id.chatView);
        //tv=(TextView)findViewById(R.id.speech);
        scrollView = (HorizontalScrollView)findViewById(R.id.scroll_view1);
        movies=new HashMap<String,List<String>>();
        movies.put("1980s 1980's",new ArrayList<String>(){{
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
        movies.put("2000s 2000's",new ArrayList<String>(){{
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
        movies.put("2010s 2010's",new ArrayList<String>(){{
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

        messageList=new ArrayList<ChatMessage>();
        movieAdapter=new MessageAdapter(this,R.layout.my_message,messageList);
        chatView.setAdapter(movieAdapter);

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
                Toast.makeText(getApplicationContext(),"speech begins",Toast.LENGTH_SHORT).show();
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
                currentText=matches.get(0);
                messageList.add(new ChatMessage(1,currentText));
                try{
                    Thread.sleep(700);
                }
                catch (Exception e){

                }
                movieAdapter.notifyDataSetChanged();
                showMovieList();
            }

            @Override
            public void onPartialResults(Bundle bundle) {
                ArrayList<String> matches = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });

        speakNow.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(!isOpen && (speaker==null || !speaker.isSpeaking())){
                    recognizer.startListening(recognitionIntent);
                }
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        recognizer.destroy();
    }

    public void showMovieList(){
        String []text=currentText.split(" ");
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
        if(result!=null)
            speak(input, result);
        else
            speak("",null);
    }

    public void speak(final String input, final List<String> result){
        speaker = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                speaker.setLanguage(Locale.US);
                CharSequence c="";
                if(!input.equals(""))c="Here is the list of top movies from "+input;
                else c="Sorry, could not find any relevant results from the search";

                messageList.add(new ChatMessage(2,c.toString()));
                speaker.speak(c,TextToSpeech.QUEUE_FLUSH,null,"0");
                movieAdapter.notifyDataSetChanged();

                if(result!=null){
                    String x="";
                    int z=1;
                    for(String s:result){
                        x+=z+". "+s+"\n";
                        z++;
                    }
                    messageList.add(new ChatMessage(3,x));
                    movieAdapter.notifyDataSetChanged();
                }
            }
        });

        speaker.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String s) {}

            @Override
            public void onDone(String s) {
                speaker.shutdown();
            }

            @Override
            public void onError(String s) {}
        });


    }
}