/*package com.example.android.moviebud;

/**
 * Created by Siddhesh on 11/23/2017.
 */

/*public class MyRecognitionListener implements RecognitionListener {

    public MyRecognitionListener(){

    }
    @Override
    public void onReadyForSpeech(Bundle bundle) {

    }

    @Override
    public void onBeginningOfSpeech() {
        speechBox.setText("");
    }

    @Override
    public void onRmsChanged(float v) {

    }

    @Override
    public void onBufferReceived(byte[] bytes) {

    }

    @Override
    public void onEndOfSpeech() {
        //tv.setText(speechBox.getText());
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
        try{
            Thread.sleep(700);
        }
        catch (Exception e){

        }
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
}*/
