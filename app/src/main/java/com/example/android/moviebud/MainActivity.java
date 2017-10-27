package com.example.android.moviebud;

import android.media.AudioRecord;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {
    AudioRecord recorder;
    ToggleButton recBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recorder=new AudioRecord(6,//MediaRecorder.AudioSource.VOICE_RECOGNITION
                recorder.getSampleRate(),
                16,//CHANNEL_IN_MONO
                2,//ENCODING_PCM_16BIT
                recorder.getMinBufferSize(recorder.getSampleRate(),16,2));
        recBtn=(ToggleButton)findViewById(R.id.recBtn);
        recBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    recorder.startRecording();
                }
                else{
                    recorder.stop();
                    recorder.release();
                }
            }
        });
    }
}