package com.MT.mythictranslator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class settings extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener {

    String fontSettingsList[] = {"Alata","Acme", "Monterey", "Casual"};
    Spinner spin;
    SessionManagerPrefrences session;
    TextView selectFontTag;
    Typeface type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setTitle("Settings");
        session = new SessionManagerPrefrences(this);
        selectFontTag = findViewById(R.id.selectFontTag);
        spin = findViewById(R.id.selectFontSpinner);

        spin.setOnItemSelectedListener(this);

        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, fontSettingsList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter);



    }

    @Override
    protected void onStart() {
        super.onStart();
        if(session.getKeyUserName()!=null){
            switch (session.getKeyUserName()){
                case "alata":
                    session.setKeyUserName("alata");
                    selectFontTag.setTypeface(ResourcesCompat.getFont(this, R.font.alataa));
                    spin.setSelection(0);
                    break;
                case "Acme":
                    session.setKeyUserName("Acme");
                    selectFontTag.setTypeface(ResourcesCompat.getFont(this, R.font.acme));
                    spin.setSelection(1);
                    break;
                case "monterey":
                    session.setKeyUserName("monterey");
                    selectFontTag.setTypeface(ResourcesCompat.getFont(this, R.font.monterey));
                    spin.setSelection(2);
                    break;
                case "Casual":
                    session.setKeyUserName("Casual");
                    selectFontTag.setTypeface(ResourcesCompat.getFont(this, R.font.casual));
                    spin.setSelection(3);
                    break;
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        switch (i){
            case 0:
                session.setKeyUserName("alata");
                selectFontTag.setTypeface(ResourcesCompat.getFont(this, R.font.alataa));
                break;
            case 1:
                session.setKeyUserName("Acme");
                selectFontTag.setTypeface(ResourcesCompat.getFont(this, R.font.acme));
                break;
            case 2:
                session.setKeyUserName("monterey");
                selectFontTag.setTypeface(ResourcesCompat.getFont(this, R.font.monterey));
                break;
            case 3:
                session.setKeyUserName("Casual");
                selectFontTag.setTypeface(ResourcesCompat.getFont(this, R.font.casual));
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}