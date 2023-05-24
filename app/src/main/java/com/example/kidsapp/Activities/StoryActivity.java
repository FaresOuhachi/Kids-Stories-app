package com.example.kidsapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.kidsapp.Adapters.viewPagerAdapter;
import com.example.kidsapp.Classes.Page;
import com.example.kidsapp.R;

import java.util.ArrayList;
import java.util.Locale;

public class StoryActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    TextView story_text;
    TextToSpeech textToSpeech;
    ViewPager viewPager;

    ImageView previousBtn,nextBtn;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);
        viewPager = findViewById(R.id.viewPager);
        nextBtn = findViewById(R.id.story_nextbtn);
        previousBtn = findViewById(R.id.story_previousbtn);
        textToSpeech = new TextToSpeech(this, this);
        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("Pages");
        ArrayList<Page> pages = (ArrayList<Page>) args.getSerializable("Array");
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if(viewPager.getCurrentItem() == 0){
                    previousBtn.setVisibility(ViewPager.GONE);
                }
                else {
                    previousBtn.setVisibility(ViewPager.VISIBLE);
                }

                if(viewPager.getCurrentItem() == pages.size()-1){
                    nextBtn.setVisibility(ViewPager.GONE);
                }
                else {
                    nextBtn.setVisibility(ViewPager.VISIBLE);
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {
                //Do Nothing
            }
        });
        previousBtn.setOnClickListener(v -> {
            viewPager.setCurrentItem(getItem(-1), true);
        });

        nextBtn.setOnClickListener(v -> {
            viewPager.setCurrentItem(getItem(+1), true);
        });

        viewPagerAdapter viewAdapter = new viewPagerAdapter(StoryActivity.this, pages);
        viewPager.setAdapter(viewAdapter);
        viewPager.setCurrentItem(0);

    }
    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = textToSpeech.setLanguage(Locale.FRENCH);

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "Language not supported");
            } else {
                // Text-to-speech is ready
                speakOut("nothing");
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

    private int getItem(int i){
        return viewPager.getCurrentItem() + i;
    }
}