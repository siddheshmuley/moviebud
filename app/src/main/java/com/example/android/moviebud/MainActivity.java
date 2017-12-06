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
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class MainActivity extends AppCompatActivity{
    SpeechRecognizer recognizer;
    Intent recognitionIntent;
    FloatingActionButton fab;
    ListView chatView;
    TextToSpeech tts;
    boolean isListening=false, isVideo=false, isError=false;
    RelativeLayout tutorial, base;
    public ArrayList<String>movies;
    MessageAdapter movieAdapter;
    ProgressBar loop, line;
    List<ChatMessage> messageList;
    Button skip,next;
    //text=https://gentle-ravine-18226.herokuapp.com/mBud/?msg=
    String currentText="", url ="https://gentle-ravine-18226.herokuapp.com/mBud/?msg=", result="";
    String c="";
    RequestQueue queue;
    JsonObjectRequest request;
    int pos=0;
    TextView tutorialText;
    ArrayList<String> videoMap, videoMap2, videoMap3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fab = (FloatingActionButton)findViewById(R.id.floatingActionButton);
        fab.setSize(FloatingActionButton.SIZE_NORMAL);
        chatView = findViewById(R.id.chatView);
        skip=findViewById(R.id.skip_tutorial);
        next=findViewById(R.id.next);
        tutorial=(RelativeLayout)findViewById(R.id.tutorial_layout);
        base=(RelativeLayout)findViewById(R.id.base_relative);
        tutorialText=findViewById(R.id.tutorial_text);
        loop=(ProgressBar) findViewById(R.id.progress_bar);
        line=(ProgressBar) findViewById(R.id.progress_horizontal);

        tutorial.post(new Runnable() {

            public void run() {
                tutorial.setVisibility(View.VISIBLE);
                loop.setVisibility(View.INVISIBLE);
                fab.setVisibility(View.INVISIBLE);
                pos++;
            }
        });

        skip.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                tutorial.setVisibility(View.INVISIBLE);
                fab.setVisibility(View.VISIBLE);
                pos=0;
                String s="Hi! I am MovieBud.@@@I could search for specific movies, movies of a genre or movies similar to a title you like.@@@Click the mic and start exploring.";
                help(s);
            }
        });

        next.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                if(pos>=2){
                    skip.performClick();
                }
                else{
                    pos++;
                    tutorialText.setText(getResources().getString(R.string.tutorial_2));
                }
            }
        });

        queue = Volley.newRequestQueue(this);

        movies=new ArrayList<String>(){{
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
        }};

        videoMap = new ArrayList();
        videoMap.add("Click the link to watch the video!");
        videoMap.add("You know the drill! Just click the link.");
        videoMap.add("To watch the trailer click the link.");
        videoMap.add("Click the link and enjoy the promo!");

        videoMap2 = new ArrayList();
        videoMap2.add("Or I could help you look for other movies.");
        videoMap2.add("Or we could explore some more movies.");
        videoMap2.add("Or if you don't like this, we could just keep looking.");

        videoMap3 = new ArrayList();
        videoMap3.add("Tell me the movie and I will get you the link");
        videoMap3.add("Give me the movie name and a youtube link you shall receive");
        videoMap3.add("Say the movie and get the youtube link");
        videoMap3.add("Name the movie and I will get you it's youtube link.");


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
                if(tts!=null){
                    tts.speak("", TextToSpeech.QUEUE_FLUSH,null,null);
                    tts.stop();
                }
                if(line.getVisibility()==View.INVISIBLE){
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
                speak("I am sorry, I seem to be facing some technical difficulties!");
                line.setVisibility(View.INVISIBLE);
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Add the request to the RequestQueue.
        queue.add(request);
        line.setVisibility(View.VISIBLE);
    }

    public void parseResult(String input, JSONObject response){
        Random rand = new Random();
        try {
            if(response !=null && "Movies,Exact,Similar,Genre,Quest,Video,Help,NoSynopsis".contains(response.get("State").toString())){
                String state=response.get("State").toString();
                if(state.equals("Exact")){
                    c="Here are some results from the search for "+response.get("Input");
                    updateListView(new ChatMessage(2,c.toString(),false));
                    result=response.get("Content").toString();
                    String s=videoMap3.get(rand.nextInt(videoMap3.size()));
                    String t=videoMap2.get(rand.nextInt(videoMap2.size()));
                    speak(c+"@@@"+s+"@@@"+t);
                    updateListView(new ChatMessage(3,result,false));
                    updateListView(new ChatMessage(2,s,false));
                    updateListView(new ChatMessage(2,t,false));
                }
                else if(state.equals("Similar")){
                    c="Here are some movies similar to "+response.get("Input");
                    updateListView(new ChatMessage(2,c.toString(),false));
                    result=response.get("Content").toString();
                    if(result==""){
                        c="I'm sorry I could not find any results for "+input+".";
                        updateListView(new ChatMessage(2,c.toString(),false));
                        speak(c);
                    }
                    else{
                        String s=videoMap3.get(rand.nextInt(videoMap3.size()));
                        speak(c+"@@@"+s);
                        updateListView(new ChatMessage(3,result,false));
                        updateListView(new ChatMessage(2,s,false));
                    }
                }
                else if(state.equals("Genre")){
                    if(response.get("input")=="new")c="Here are some "+response.get("Input")+" movies for you.";
                    else c="Here are some "+response.get("Input")+" movies for you.";
                    updateListView(new ChatMessage(2,c.toString(),false));
                    result=response.get("Content").toString();
                    String s=videoMap3.get(rand.nextInt(videoMap3.size()));
                    String t=videoMap2.get(rand.nextInt(videoMap2.size()));
                    speak(c+"@@@"+s+"@@@"+t);

                    updateListView(new ChatMessage(3,result,false));
                    updateListView(new ChatMessage(2,s,false));
                    updateListView(new ChatMessage(2,t,false));
                }
                else if(state.equals("Video")){
                    c="Here is the link to the trailer of "+response.get("Input");
                    updateListView(new ChatMessage(2,c.toString(),false));
                    result=response.get("Content").toString();

                    String s=videoMap.get(rand.nextInt(videoMap.size()));
                    String t=videoMap2.get(rand.nextInt(videoMap2.size()));
                    speak(c+"@@@"+s+"@@@"+t);
                    updateListView(new ChatMessage(4,result,true));
                    updateListView(new ChatMessage(2,s,false));
                    updateListView(new ChatMessage(2,t,false));
                }
                else if(state.equals("Help")){
                    String t="I could search for specific movies, movies of a genre or movies similar to a title you like.";
                    help(t);
                }
                else if(state.equals("Synopsis")){
                    String s="Here is what I found about "+response.get("Input")+".";
                    result=response.get("Content").toString();
                    updateListView(new ChatMessage(2,s,true));
                    updateListView(new ChatMessage(3,result,true));
                    speak(s+"@@@"+result);
                }
                else if(state.equals("NoSynopsis")){
                    String v="I'm sorry, I could not find a synopsis for "+response.get("Input")+".";
                    updateListView(new ChatMessage(2,v,false));
                    c="Here is the link to its trailer instead.";
                    updateListView(new ChatMessage(2,c.toString(),false));
                    result=response.get("Content").toString();

                    String t=videoMap2.get(rand.nextInt(videoMap2.size()));
                    speak(v+"@@@"+c+"@@@"+t);
                    updateListView(new ChatMessage(4,result,true));
                    updateListView(new ChatMessage(2,t,false));
                }
                else if(state.equals("Movies")){
                    c="Here are some movies for you";
                    updateListView(new ChatMessage(2,c.toString(),false));
                    result=response.get("Content").toString();

                    int i=0;
                    while(i++<5)
                        result+=movies.get(rand.nextInt(movies.size()))+"\n";
                    String s=videoMap3.get(rand.nextInt(videoMap3.size()));
                    String t=videoMap2.get(rand.nextInt(videoMap2.size()));
                    speak(c+"@@@"+s+"@@@"+t);
                    updateListView(new ChatMessage(3,result,false));
                    updateListView(new ChatMessage(2,s,false));
                    updateListView(new ChatMessage(2,t,false));

                }
                else {
                    result=response.get("Content").toString();
                    speak(result);
                    updateListView(new ChatMessage(2,result,false));
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
                for(String ip:input.split("@@@")){
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
            fab.setAlpha((float)1.0);
        }
        else{
            fab.setClickable(true);
            fab.setImageResource(R.drawable.ic_mic_white_24dp);
            loop.setVisibility(View.INVISIBLE);
            fab.bringToFront();
            fab.setClickable(true);
            fab.setAlpha((float)0.5);
        }
    }

    public void help(String s){
        for(String i:s.split("@@@"))
            updateListView(new ChatMessage(2,i,false));
        speak(s);
    }
}