package com.MT.mythictranslator;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.DownloadManager;
import android.app.SearchManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;

import java.security.Permission;
import java.util.ArrayList;
import java.util.Locale;

public class Logic extends AppCompatActivity {

    //Declaration//
    EditText convertFrom, convertTo;
    Button clearbtn,translatebtn;
    ImageButton copybtn, textZoomInbtn, textZoomOutbtn, sharebtn;
    Float s = 22f;
    int turn = 1;
    CardView alterLanguageBtn;
    TextView translateFrom, translateTo, sourceCode;
    ImageView micBtn, speakerBtn, searchBtn;
    String fromLanguageCode, toLanguageCode;
    RelativeLayout transparentFilter;
    LinearLayout facebookBtn;
    TextToSpeech textToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logic);

        //Initialization//
        convertFrom = findViewById(R.id.convertFrom);
        convertTo = findViewById(R.id.convertTo);
        clearbtn = findViewById(R.id.clearBtn);
        copybtn = findViewById(R.id.copyBtn);
        translatebtn = findViewById(R.id.TranslateBtn);
        textZoomInbtn = findViewById(R.id.MaximizeBtn);
        textZoomOutbtn = findViewById(R.id.MinimizeBtn);
        sharebtn = findViewById(R.id.ShareBtn);
        alterLanguageBtn = findViewById(R.id.MiddleIconsHolder);
        translateFrom = findViewById(R.id.TranslateFrom);
        translateTo = findViewById(R.id.TranslateTo);
        micBtn = findViewById(R.id.MicBtn);
        transparentFilter = findViewById(R.id.bac_dim_layout);
        facebookBtn = findViewById(R.id.facebook);
        sourceCode = findViewById(R.id.R8);
        speakerBtn = findViewById(R.id.speakBtn);
        searchBtn = findViewById(R.id.searchBtn);
        // LOGIC//
        fromLanguageCode = TranslateLanguage.URDU;
        toLanguageCode = TranslateLanguage.ENGLISH;

        clearbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                convertFrom.setText("");
                convertTo.setText("");
                convertTo.setHint("Translation");
            }
        });

        textZoomInbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                s+=10f;
                convertFrom.setTextSize(TypedValue.COMPLEX_UNIT_SP, s);
                convertTo.setTextSize(TypedValue.COMPLEX_UNIT_SP, s);
            }
        });

        copybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!convertTo.getText().toString().isEmpty()) {
                ClipboardManager cp = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData c = ClipData.newPlainText("Translation", convertTo.getText());
                cp.setPrimaryClip(c);
                Toast.makeText(getApplicationContext(), "Text Copied!", Toast.LENGTH_SHORT).show();
            }
                else{
                    Toast.makeText(getApplicationContext(), "Nothing to copy!", Toast.LENGTH_LONG).show();
                }
            }
        });

        sourceCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                intent.putExtra(SearchManager.QUERY, "https://github.com/jaffar02/MythicTranslator");
                startActivity(intent);
            }
        });


        facebookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                intent.putExtra(SearchManager.QUERY, "https://www.facebook.com/profile.php?id=100010245871233");
                startActivity(intent);
            }
        });

        textZoomOutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                s-=10f;
                convertFrom.setTextSize(TypedValue.COMPLEX_UNIT_SP, s);
                convertTo.setTextSize(TypedValue.COMPLEX_UNIT_SP, s);
            }
        });

        sharebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!convertTo.getText().toString().isEmpty()) {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Translation");
                    Log.d("translation", convertTo.getText().toString());
                    intent.putExtra(android.content.Intent.EXTRA_TEXT, "Text: " + convertFrom.getText().toString()
                            + "\nTranslation: " + convertTo.getText().toString());
                    startActivity(Intent.createChooser(intent, "Share via"));
                }
                else{
                    Toast.makeText(getApplicationContext(), "Nothing to share!", Toast.LENGTH_LONG).show();
                }
            }
        });

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(convertTo.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Nothing to search!", Toast.LENGTH_LONG).show();
                }
                else{
                    Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                    intent.putExtra(SearchManager.QUERY, convertTo.getText().toString());
                    startActivity(intent);
                }
            }
        });

        speakerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int i) {
                        if(convertTo.getText().toString().isEmpty()){
                            Toast.makeText(getApplicationContext(), "Nothing to speak!", Toast.LENGTH_LONG).show();
                        }
                        else{
                            if(i == TextToSpeech.SUCCESS){
                                int result = 0;
                                if(toLanguageCode.toString().equals("en")) {
                                    result = textToSpeech.setLanguage(Locale.ENGLISH);
                                    textToSpeech.setSpeechRate(1f);
                                    textToSpeech.setPitch(1f);
                                }
                                else if(toLanguageCode.toString().equals("ur")){
                                    result = textToSpeech.setLanguage(new Locale("urd"));
                                    textToSpeech.setSpeechRate(1);
                                    textToSpeech.setPitch(1);
                                }
                                if(result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED){
                                    Toast.makeText(getApplicationContext(), "Language not supported!", Toast.LENGTH_LONG).show();
                                }
                                else {
                                    textToSpeech.speak(convertTo.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
                                }
                            }
                        }
                    }
                });
            }
        });

        alterLanguageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               switch (turn) {
                   case 1:
                   String buffer = translateFrom.getText().toString();
                   translateFrom.setText(translateTo.getText().toString());
                   translateTo.setText(buffer);
                   //fromLanguage = "English";
                   //toLanguage = "Urdu";
                   if(!convertFrom.getText().toString().isEmpty()&&!convertTo.getText().toString().isEmpty()){
                       buffer = convertFrom.getText().toString();
                       convertFrom.setText(convertTo.getText().toString());
                       convertTo.setText(buffer);
                   }
                   fromLanguageCode = TranslateLanguage.ENGLISH;
                   toLanguageCode = TranslateLanguage.URDU;
                   turn = 2;
                   break;
                   case 2:
                   String buffer1 = translateTo.getText().toString();
                   translateTo.setText(translateFrom.getText().toString());
                   translateFrom.setText(buffer1);
                   //fromLanguage = "Urdu";
                   //toLanguage = "English";
                   if(!convertFrom.getText().toString().isEmpty()&&!convertTo.getText().toString().isEmpty()){
                           buffer = convertTo.getText().toString();
                           convertTo.setText(convertFrom.getText().toString());
                           convertFrom.setText(buffer);
                       }
                   fromLanguageCode = TranslateLanguage.URDU;
                   toLanguageCode = TranslateLanguage.ENGLISH;
                   turn = 1;
                   break;
               }
            }
        });

        translatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(convertFrom.getText().toString().isEmpty()){
                  Toast.makeText(getApplicationContext(), "Enter something", Toast.LENGTH_SHORT).show();
                }
                else{
                   Log.d("check1", "Checkpoint 1");
                   translateLanguage(convertFrom.getText().toString());
                }
            }
        });

        micBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                if(fromLanguageCode.toString().equals("ur")) {
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ur");
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "ur");
                    intent.putExtra(RecognizerIntent.EXTRA_ONLY_RETURN_LANGUAGE_PREFERENCE, "ur");
                }
                else if(fromLanguageCode.toString().equals("en")){
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH);
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, Locale.ENGLISH);
                    intent.putExtra(RecognizerIntent.EXTRA_ONLY_RETURN_LANGUAGE_PREFERENCE, Locale.ENGLISH);
                }
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say Something");
                startActivityForResult(intent, 1);
            }
        });

    }

    private void translateLanguage(String sourceText) {
        Log.d("check1", "Checkpoint 2");
        convertTo.setHint("Downloading Model, please wait...");
        TranslatorOptions options1 = new TranslatorOptions.Builder()
                .setSourceLanguage(fromLanguageCode)
                .setTargetLanguage(toLanguageCode)
                .build();

        final Translator translator =
                Translation.getClient(options1);


        DownloadConditions conditions = new DownloadConditions.Builder()
                .requireWifi()
                .build();

        translator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("check1", "Checkpoint 3");
                convertTo.setHint("translating...");
                translator.translate(sourceText).addOnSuccessListener(new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        convertTo.setText(s);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Failed to translate", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Failed to download!! Check Network.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1){
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if(!result.get(0).equals("")) {
                convertFrom.setText(result.get(0));
                translateLanguage(result.get(0));
            }
            else{
                Toast.makeText(getApplicationContext(), "Please speak again!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.about){
            transparentFilter.setVisibility(View.VISIBLE);
            return true;
        }
        else if (item.getItemId() == R.id.exit){
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void filterAction(View view) {
        transparentFilter.setVisibility(View.INVISIBLE);
    }
}