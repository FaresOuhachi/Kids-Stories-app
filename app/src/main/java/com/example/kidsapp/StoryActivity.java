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

    private static String getElementValue(Element parentElement, String elementName) {
        NodeList nodeList = parentElement.getElementsByTagName(elementName);
        Element element = (Element) nodeList.item(0);
        return element.getTextContent();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);
        story_text = findViewById(R.id.story_text);
        story_image = findViewById(R.id.story_image);

        textToSpeech = new TextToSpeech(this, this);
        Map<Integer, String> views = new HashMap<>();
        Intent intent = getIntent();
        int[] image = intent.getIntArrayExtra("images");
        String[] text = intent.getStringArrayExtra("texts");

        for (int i = 0; i < image.length; i++) {
            views.put(image[i], text[i]);
        }

        System.out.println(views);

        story_text.setText(views.get(image[0]));

        System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        XmlPullParserFactory parserFactory;

        try {

            parserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserFactory.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            // Load the XML file
            InputStream inputStream = getResources().openRawResource(R.raw.stories);

            // Set the input stream for the parser
            parser.setInput(inputStream, null);

            // Get the event type
            int eventType = parser.getEventType();

            // Initialize variables
            Integer id = 0;
            String title = "";
            String author = "";
            Integer pageNum = 0;
            Integer img = 0;
            String texts = "";
            StoryStructure storyStructure = new StoryStructure(id, title, author);
            Page page = new Page(pageNum, img, texts);
            ArrayList<StoryStructure> stories_list = new ArrayList<>();
            // Start parsing the XML
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagName;

                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        tagName = parser.getName();
                        Log.e("tag Opened", tagName);
                        parser.next();
                        if (tagName.equals("storyStructure")) {
                            // Extract the storyStructure attributes
                            title = "";
                            author = "";

                            // Create a new StoryStructure object
                            storyStructure.setTitle(title);
                            storyStructure.setAuthor(author);
                        }else if (tagName.equals("id")) {
                            id = Integer.parseInt(parser.getText());
                            storyStructure.setId(id);

                        } else if (tagName.equals("title") || tagName.equals("author")) {
                            if (parser.getEventType() == XmlPullParser.TEXT) {
                                if (tagName.equals("title")) {
                                    title = parser.getText();
                                    storyStructure.setTitle(title);
                                } else {
                                    author = parser.getText();
                                    storyStructure.setAuthor(author);
                                }
                            }
                        } else if (tagName.equals("num")){
                            pageNum = Integer.parseInt(parser.getText());
                            page.setPageNum(pageNum);
                        }
                        // Get the pages element
                         else if (tagName.equals("page")) {
                            // Extract the page attributes

                            img = 0;
                            texts = "";
                            // Create a new Page object
                            page.setImage(img);
                            page.setText(texts);

                        } else if (tagName.equals("image")) {
                            // Get the text value of image and text elements
                            if (parser.getEventType() == XmlPullParser.TEXT) {
                                img = Integer.parseInt(parser.getText());
                                page.setImage(img);
                            }
                        }
                        else if (tagName.equals("text")) {
                            // Get the text value of image and text elements
                            if (parser.getEventType() == XmlPullParser.TEXT) {
                                texts = parser.getText();
                                page.setText(texts);
                            }
                        }
                        break;

                    case XmlPullParser.END_TAG:
                        tagName = parser.getName();
                        Log.e("tag Closed", tagName);
                        if (tagName.equals("storyStructure")) {
                            if (storyStructure != null) {
                                stories_list.add(storyStructure);

                                Log.e("Story add", "Story added to stories_list");

                                // Process the completed storyStructure object
                                System.out.println("storystructure: " + storyStructure);
                                // Reset the storyStructure object
                            } else {
                                Log.e("StoryActivity", "Null storyStructure object");
                            }
                        } else if (tagName.equals("page")) {
                            storyStructure.addPage(page);
                            Log.e("Page add", "Page added to storyStructure");
                        }
                        break;

                }

                for (StoryStructure item : stories_list) {
                    // Process each element
                    System.out.println(item.toString());
                }
                // Move to the next event
                eventType = parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
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