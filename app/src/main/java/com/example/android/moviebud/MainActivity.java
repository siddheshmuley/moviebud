package com.example.android.moviebud;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity{
    SpeechRecognizer recognizer;
    Intent recognitionIntent;
    FloatingActionButton fab;
    ListView chatView;
    TextToSpeech tts;
    boolean isListening=false, isVideo=false, isError=false, isSpeaking=false;
    RelativeLayout chatLayout;
    public Map<String,List<String>>movies;
    MessageAdapter movieAdapter;
    ProgressBar loop, line;
    List<ChatMessage> messageList;
    String currentText="", url ="http://192.168.0.21:8000/my/?msg=", result="";
    String c="";
    RequestQueue queue;
    JsonObjectRequest request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fab = (FloatingActionButton)findViewById(R.id.floatingActionButton);
        fab.setSize(FloatingActionButton.SIZE_NORMAL);
        chatView = findViewById(R.id.chatView);
        /*chatView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getApplicationContext(),"listitem clicked",Toast.LENGTH_SHORT).show();
            }
        });*/
        loop=(ProgressBar) findViewById(R.id.progress_bar);
        line=(ProgressBar) findViewById(R.id.progress_horizontal);



        queue = Volley.newRequestQueue(this);
        //tv=(TextView)findViewById(R.id.speech);
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
                isListening=true;
            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {
                isListening=false;
                flipFAB();
            }

            @Override
            public void onError(int i) {
                //recognizer.startListening(recognitionIntent);
                Toast.makeText(getApplicationContext(),"some error happened",Toast.LENGTH_SHORT).show();
                isListening=false;
                flipFAB();
            }

            @Override
            public void onResults(Bundle bundle) {
                ArrayList<String> matches = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                currentText=matches.get(0);
                updateListView(new ChatMessage(1,currentText,false));
                sendRequest();
            }

            @Override
            public void onPartialResults(Bundle bundle) {
                ArrayList<String> matches = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });

        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                isListening=true;
                if(!isSpeaking){
                    flipFAB();
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

    public void sendRequest(){
        request= new JsonObjectRequest(Request.Method.GET,
                url + currentText,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        line.setVisibility(View.INVISIBLE);
                        parseResult(currentText, response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                speak("Some error occurred");
                speak("Some error occurred");
                line.setVisibility(View.INVISIBLE);
                Snackbar.make(chatView,"Error",Snackbar.LENGTH_SHORT).show();
            }
        });
        // Add the request to the RequestQueue.
        queue.add(request);
        line.setVisibility(View.VISIBLE);
    }

    public void parseResult(String input, JSONObject response){
        try {
            if(response !=null && "Exact,Similar,Genre,Quest,Video".contains(response.get("State").toString())){
                if(response.get("State").equals("Exact")){
                    c="Here are some results from the search for "+response.get("Input");
                    updateListView(new ChatMessage(2,c.toString(),false));
                    result=response.get("Content").toString();
                    String s="I could get you a link to a video related to any of these movies, if you like.";
                    speak(c+"$$$"+s);
                    updateListView(new ChatMessage(3,result,false));
                    updateListView(new ChatMessage(2,s,false));
                }
                else if(response.get("State").equals("Similar")){
                    c="Here are some movies similar to "+response.get("Input");
                    updateListView(new ChatMessage(2,c.toString(),false));
                    result=response.get("Content").toString();
                    String s="I could get you a link to a video related to any of these movies, if you like.";
                    speak(c+"$$$"+s);
                    updateListView(new ChatMessage(3,result,false));
                    updateListView(new ChatMessage(2,s,false));
                }
                else if(response.get("State").equals("Genre")){
                    c="Here are some results from the same genre as "+response.get("Input");
                    updateListView(new ChatMessage(2,c.toString(),false));
                    result=response.get("Content").toString();
                    String s="I could get you a link to a video related to any of these movies, if you like.";
                    speak(c+"$$$"+s);
                    updateListView(new ChatMessage(3,result,false));
                    updateListView(new ChatMessage(2,s,false));
                }
                else if(response.get("State").equals("Video")){
                    c="Here is the link to the trailer of "+response.get("Input");
                    updateListView(new ChatMessage(2,c.toString(),false));
                    result=response.get("Content").toString();
                    String s="Would you like to watch it?";
                    speak(c+"$$$"+s);
                    updateListView(new ChatMessage(4,result,true));
                    updateListView(new ChatMessage(2,s,false));
                }
                else {
                    c="You searched for "+response.get("Input")+". Here are the results";
                    updateListView(new ChatMessage(2,c.toString(),false));
                    result=response.get("Content").toString();
                    String s="I could get you a link to a video related to any of these movies, if you like.";
                    speak(c+"$$$"+s);
                    updateListView(new ChatMessage(3,result,false));
                    updateListView(new ChatMessage(2,s,false));
                }
            }

            else if(response !=null && response.get("State").equals("InternalError")){
                c="I'm sorry I could not find any results for "+input+".";
                updateListView(new ChatMessage(2,c.toString(),false));
                speak(c);
            }
            else{
                c="It seems I am facing some technical difficulties at the moment";
                updateListView(new ChatMessage(2,c.toString(),false));
                speak(c);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void speak(final String input){

        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                isSpeaking=true;
                for(String ip:input.split("***")){
                    tts.speak(ip, TextToSpeech.QUEUE_ADD,null,null);
                    tts.playSilentUtterance(100,TextToSpeech.QUEUE_ADD,null);
                }

                tts.speak("", TextToSpeech.QUEUE_ADD, null,"done");
            }
        });
        tts.setLanguage(Locale.US);
        tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String s) {
            }

            @Override
            public void onDone(String s) {
                tts.shutdown();
                isSpeaking=false;
            }
            @Override
            public void onError(String s) {}
        });
    }

    public void updateListView(ChatMessage m){
        messageList.add(m);
        movieAdapter.notifyDataSetChanged();
    }

    public void flipFAB(){
        if(isListening){
            fab.setClickable(false);
            fab.setImageResource(0);
            loop.setVisibility(View.VISIBLE);
            loop.bringToFront();
        }
        else{
            fab.setClickable(true);
            fab.setImageResource(R.drawable.ic_mic_white_24dp);
            loop.setVisibility(View.INVISIBLE);
            fab.bringToFront();
            fab.setClickable(true);
        }
    }
}