package com.example.kidsapp;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class StoryActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    TextView story_text;
    TextToSpeech textToSpeech;

    ImageView story_image;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);
        story_text = findViewById(R.id.story_text);
        story_image = findViewById(R.id.story_image);

        textToSpeech = new TextToSpeech(this, this);

        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("Pages");
        ArrayList<Page> pages = (ArrayList<Page>) args.getSerializable("Array");


            story_text.setText(pages.get(0).getText());
            story_image.setImageResource(pages.get(0).getImage());

    }
    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = textToSpeech.setLanguage(Locale.FRENCH);

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "Language not supported");
            } else {
                // Text-to-speech is ready
                speakOut(story_text.getText().toString());
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
        if (textToSpeech != null && story_text != null) {
            speakOut(story_text.getText().toString());
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