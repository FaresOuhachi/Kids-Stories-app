package com.example.kidsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Locale;

public class StoryActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    TextView text;
    TextToSpeech textToSpeech;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);
        text = findViewById(R.id.story_text);
        textToSpeech = new TextToSpeech(this, this);

        Intent intent = getIntent();
        HashMap<Integer, String> story = (HashMap<Integer, String>)intent.getSerializableExtra("views");
        System.out.println( story.get(1));
        String data = intent.getStringExtra("title");
        System.out.println( data);
        System.out.println( "ZEHIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIII");
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = textToSpeech.setLanguage(Locale.FRENCH);

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "Language not supported");
            } else {
                // Text-to-speech is ready
                speakOut(text.getText().toString());
            }
        } else {
            Log.e("TTS", "Initialization failed");
        }
    }

    private void speakOut(String text) {
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (textToSpeech != null) {
            textToSpeech.stop();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (textToSpeech != null && text != null) {
            speakOut(text.getText().toString());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }

}