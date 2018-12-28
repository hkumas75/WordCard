package kumasfahn.wordcard;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Logger;

import static android.R.color.holo_blue_bright;
import static android.R.color.holo_purple;
import static android.R.color.holo_red_light;
import static kumasfahn.wordcard.R.drawable.button_damla3;
import static kumasfahn.wordcard.R.drawable.button_damla3y;
import static kumasfahn.wordcard.R.drawable.dugme;
import static kumasfahn.wordcard.R.drawable.dugmey;


public class WordCard extends AppCompatActivity {
    TextView txv_word,  txv_mean, txv_unknw, txv_knwn;
    ScrollTextView txv_exmp;
    Button butn_pas, butn_ppa, butn_prev, butn_next, btn_kydt;
    Integer  top_ks, sb=0, cpos, kntr_mark=0;
    Veritabani1 vt;
    Cursor cursor;
    String type, knwn_State;
    public String [] liste1;
    Toolbar toolbar;

    //herhangi bir butona tıklandıgında Id ile btn tespiti ve switch case ile uygulanacak metoda gitme
    public View.OnClickListener btnClickListener=new View.OnClickListener() {
        @Override
        public void onClick (View v) {
            switch (v.getId()){
                case R.id.tv_word:
                    showall ();
                    break;
                case R.id.btn_ppa:
                    showppa ();
                    break;
                case R.id.tv_mean:
                    showmean ();
                    break;
                case R.id.btn_pas:
                    showpas ();
                    break;
                case R.id.btn_prev:
                    showprev ();
                    break;
                case R.id.btn_next:
                    shownext ();
                    break;
                case R.id.btn_kydt:
                    marknown ();
                   break;

            }
        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wordcard);

        //ana ekrandan verinin alınması
        Intent intent = getIntent();
        type = intent.getStringExtra("tip");
        knwn_State = intent.getStringExtra("drm");

        //textview ve buttonların tanımlanması
        txv_word = (TextView) findViewById(R.id.tv_word);
        txv_word.setOnClickListener(btnClickListener);
        txv_exmp = (ScrollTextView) findViewById(R.id.tv_exmp);
        txv_mean = (TextView) findViewById(R.id.tv_mean);
        txv_mean.setOnClickListener(btnClickListener);
        txv_knwn = (TextView) findViewById(R.id.tv_knwn);
        txv_unknw = (TextView) findViewById(R.id.tv_unknw);

        butn_pas = (Button) findViewById(R.id.btn_pas);
        butn_pas.setOnClickListener(btnClickListener);
        butn_ppa = (Button) findViewById(R.id.btn_ppa);
        butn_ppa.setOnClickListener(btnClickListener);
        butn_prev = (Button) findViewById(R.id.btn_prev);
        butn_prev.setOnClickListener(btnClickListener);
        butn_next = (Button) findViewById(R.id.btn_next);
        butn_next.setOnClickListener(btnClickListener);
        btn_kydt = (Button) findViewById(R.id.btn_kydt);
        btn_kydt.setOnClickListener(btnClickListener);
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Flash Cards");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //seçilen kelime bilgilerinin bulunması ve liste dizisine atanması
        vt = new Veritabani1(this);
        SQLiteDatabase dbOku = vt.getReadableDatabase();

        if (type.equals("All")) {
            switch (knwn_State) {
                case "Known":
                    cursor = dbOku.rawQuery("SELECT * FROM TABLO_WORD WHERE COLOR= 'Y' ORDER BY WORD COLLATE NOCASE ASC", null);
                    top_ks = cursor.getCount();
                    break;
                case "Unknown":
                    cursor = dbOku.rawQuery("SELECT * FROM TABLO_WORD WHERE COLOR= 'N' ORDER BY WORD COLLATE NOCASE ASC", null);
                    top_ks = cursor.getCount();
                    break;
                case "All":
                    cursor = dbOku.rawQuery("SELECT * FROM TABLO_WORD ORDER BY WORD COLLATE NOCASE ASC", null);
                    top_ks = cursor.getCount();
                    break;
            }

        } else {

            switch (knwn_State) {
                case "Known":
                    cursor = dbOku.rawQuery("SELECT * FROM TABLO_WORD WHERE TYPE='" + type + "' AND COLOR= 'Y' ORDER BY WORD COLLATE NOCASE ASC", null);
                    top_ks = cursor.getCount();
                    break;
                case "Unknown":
                    cursor = dbOku.rawQuery("SELECT * FROM TABLO_WORD WHERE TYPE='" + type + "' AND COLOR= 'N' ORDER BY WORD COLLATE NOCASE ASC", null);
                    top_ks = cursor.getCount();
                    break;
                case "All":
                    cursor = dbOku.rawQuery("SELECT * FROM TABLO_WORD WHERE TYPE='" + type + "' ORDER BY WORD COLLATE NOCASE ASC ", null);
                    top_ks = cursor.getCount();
                    break;
            }
        }
        if (cursor.getCount()<1) Toast.makeText(this, "There is no record..", Toast.LENGTH_SHORT).show();
        else {cursor.moveToFirst();
            liste1 = new String[]{cursor.getString(0),  cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7)};

        txv_word.setText(liste1 [2]);
        txv_exmp.setText(cursor.getString(4));
        txv_exmp.startScroll();txv_exmp.setTextColor(Color.RED);

        txv_mean.setText("Meaning?");
        btn_kydt.setText(liste1 [1]);

        switch (liste1 [1]) {
            case "Verb":
                butn_pas.setText("Past Form?");
                butn_ppa.setText("Past Perfect Form?");
                break;
            case "Noun":
                butn_pas.setText("Artikel?");
                butn_ppa.setText("Plural Form?");
                break;
            case "Adj.":
                butn_pas.setText("Synonym?");
                butn_ppa.setText("Antonym?");
                break;
        }

        switch (liste1 [7]) {
            case "N":
                btn_kydt.setBackground(getResources().getDrawable(dugme));
                txv_mean.setBackground(getResources().getDrawable(button_damla3));
                break;
            case "Y":
                btn_kydt.setBackground(getResources().getDrawable(dugmey));
                txv_mean.setBackground(getResources().getDrawable(button_damla3y));
                break;
        }
            sb=sb+1;
            txv_knwn.setText("-"+top_ks+"-");
            txv_unknw.setText("-"+sb+"-");
        // digital font ayarı
        //Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/digi1.ttf");
        //txv_word.setTypeface(custom_font);

    }}
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void refresh_data () {
        vt = new Veritabani1(this);
        SQLiteDatabase dbOku = vt.getReadableDatabase();

        if (type.equals("All")) {
            switch (knwn_State) {
                case "Known":
                    cursor = dbOku.rawQuery("SELECT * FROM TABLO_WORD WHERE COLOR= 'Y' ORDER BY WORD COLLATE NOCASE ASC", null);
                    top_ks = cursor.getCount();
                    break;
                case "Unknown":
                    cursor = dbOku.rawQuery("SELECT * FROM TABLO_WORD WHERE COLOR= 'N' ORDER BY WORD COLLATE NOCASE ASC", null);
                    top_ks = cursor.getCount();
                    break;
                case "All":
                    cursor = dbOku.rawQuery("SELECT * FROM TABLO_WORD ORDER BY WORD COLLATE NOCASE ASC", null);
                    top_ks = cursor.getCount();
                    break;
            }

        } else {

            switch (knwn_State) {
                case "Known":
                    cursor = dbOku.rawQuery("SELECT * FROM TABLO_WORD WHERE TYPE='" + type + "' AND COLOR= 'Y' ORDER BY WORD COLLATE NOCASE ASC", null);
                    top_ks = cursor.getCount();
                    break;
                case "Unknown":
                    cursor = dbOku.rawQuery("SELECT * FROM TABLO_WORD WHERE TYPE='" + type + "' AND COLOR= 'N' ORDER BY WORD COLLATE NOCASE ASC", null);
                    top_ks = cursor.getCount();
                    break;
                case "All":
                    cursor = dbOku.rawQuery("SELECT * FROM TABLO_WORD WHERE TYPE='" + type + "' ORDER BY WORD COLLATE NOCASE ASC ", null);
                    top_ks = cursor.getCount();
                    break;
            }
        }
    }

    public void marknown () {
        if (cursor.getCount()<=0){
            Toast.makeText(this, "There is no record..", Toast.LENGTH_SHORT).show();
            return;
        }
        switch (cursor.getString(7)){
            case "N":
                cpos=cursor.getPosition();
                boolean isUpdate = vt.markData(cursor.getString(0),  "Y");
                if(isUpdate){
                    Toast.makeText(this,"The "+cursor.getString(1)+ " is marked as known..",Toast.LENGTH_LONG).show();
                    btn_kydt.setBackground(getResources().getDrawable(dugmey));
                    txv_mean.setBackground(getResources().getDrawable(button_damla3y));
                    refresh_data();
                    if (knwn_State.equals("Known")|| knwn_State.equals("Unknown")){
                        if (cpos==0){
                            cursor.moveToLast();kntr_mark=1;sb=top_ks+1;txv_unknw.setText("####");txv_knwn.setText("-"+top_ks+"-");
                        }else{
                        cursor.moveToPosition(cpos-1); kntr_mark=1;sb=sb-1;txv_unknw.setText("####");txv_knwn.setText("-"+top_ks+"-");}}
                    else {cursor.moveToPosition(cpos);
                    txv_knwn.setText("-"+top_ks+"-");
                    txv_unknw.setText("-"+sb+"-");}
                }
                else
                    Toast.makeText(this,"The "+cursor.getString(1)+ " is not marked as known..",Toast.LENGTH_LONG).show();break;
            case "Y":
                cpos=cursor.getPosition();
                isUpdate = vt.markData(cursor.getString(0),  "N");
                if(isUpdate){
                    Toast.makeText(this,"The "+cursor.getString(1)+ " is marked as unknown..",Toast.LENGTH_LONG).show();
                    btn_kydt.setBackground(getResources().getDrawable(dugme));
                    txv_mean.setBackground(getResources().getDrawable(button_damla3));
                    refresh_data();
                    if (knwn_State.equals("Known")|| knwn_State.equals("Unknown")){
                        if (cpos==0){
                           cursor.moveToLast();kntr_mark=1; sb=top_ks+1;txv_unknw.setText("####");txv_knwn.setText("-"+top_ks+"-");
                        }else{
                        cursor.moveToPosition(cpos-1); kntr_mark=1;sb=sb-1;txv_unknw.setText("####");txv_knwn.setText("-"+top_ks+"-");}}
                    else {cursor.moveToPosition(cpos);
                    txv_knwn.setText("-"+top_ks+"-");
                    txv_unknw.setText("-"+sb+"-");}
                }
                else
                    Toast.makeText(this,"The "+cursor.getString(1)+ " is not marked as unknown..",Toast.LENGTH_LONG).show();break;

    }}

    public void showall() {
        if (cursor.getCount()<=0){
            Toast.makeText(this, "There is no record..", Toast.LENGTH_SHORT).show();
            return;
        }
        txv_mean.setText(cursor.getString(3));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            txv_mean.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        }
        butn_pas.setText(cursor.getString(5));
        butn_ppa.setText(cursor.getString(6));
    }

    public void showpas() {
        if (cursor.getCount()<=0){
            Toast.makeText(this, "There is no record..", Toast.LENGTH_SHORT).show();
            return;
        }
        butn_pas.setText(cursor.getString(5));
    }

    public void showppa() {
        if (cursor.getCount()<=0){
            Toast.makeText(this, "There is no record..", Toast.LENGTH_SHORT).show();
            return;
        }
        butn_ppa.setText(cursor.getString(6));
    }

    public void showmean() {
        if (cursor.getCount()<=0){
            Toast.makeText(this, "There is no record..", Toast.LENGTH_SHORT).show();
            return;
        }
        txv_mean.setText(cursor.getString(3));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            txv_mean.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        }
    }

   public void showprev() {
       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
           txv_mean.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
       }
       if (cursor.getCount()<=0){
           Toast.makeText(this, "There is no record..", Toast.LENGTH_SHORT).show();
           return;
       }
       if (cursor.isFirst()){
           if (kntr_mark.equals(1)) {kntr_mark=0; sb=2;}
           else {
           sb=top_ks+1; cursor.moveToLast();}
       }
       else {
            if (kntr_mark.equals(1)) kntr_mark=0;
            else cursor.moveToPrevious();}

           txv_word.setText(cursor.getString(2));
           txv_exmp.setText(cursor.getString(4));
           txv_exmp.startScroll();txv_exmp.setTextColor(Color.RED);
           btn_kydt.setText(cursor.getString(1));
           txv_mean.setText("Meaning?");
           btn_kydt.setText(cursor.getString(1));

           switch (cursor.getString(1)) {
               case "Verb":
                   butn_pas.setText("Past Form?");
                   butn_ppa.setText("Past Perfect Form?");
                   break;
               case "Noun":
                   butn_pas.setText("Artikel?");
                   butn_ppa.setText("Plural Form?");
                   break;
               case "Adj.":
                   butn_pas.setText("Synonym?");
                   butn_ppa.setText("Antonym?");
                   break;
           }
       switch (cursor.getString(7)) {
           case "N":
               btn_kydt.setBackground(getResources().getDrawable(dugme));
               txv_mean.setBackground(getResources().getDrawable(button_damla3));
               break;
           case "Y":
               btn_kydt.setBackground(getResources().getDrawable(dugmey));
               txv_mean.setBackground(getResources().getDrawable(button_damla3y));
               break;
       }
       sb=sb-1;
       txv_unknw.setText("-"+sb+"-");
       txv_knwn.setText("-"+top_ks+"-");
   }

    public void shownext(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            txv_mean.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        }
        if (cursor.getCount()<=0){
            Toast.makeText(this, "There is no record..", Toast.LENGTH_SHORT).show();
            return;
        }
        if (cursor.isLast()){
            if (kntr_mark.equals(1)) kntr_mark=0;
            cursor.moveToFirst();
            sb=0;
           }
            else
                cursor.moveToNext();

            txv_word.setText(cursor.getString(2));
            txv_exmp.setText(cursor.getString(4));
            txv_exmp.startScroll();txv_exmp.setTextColor(Color.RED);
            btn_kydt.setText(cursor.getString(1));
            txv_mean.setText("Meaning?");
            btn_kydt.setText(cursor.getString(1));

            switch (cursor.getString(1)) {
                case "Verb":
                    butn_pas.setText("Past Form?");
                    butn_ppa.setText("Past Perfect Form?");
                    break;
                case "Noun":
                    butn_pas.setText("Artikel?");
                    butn_ppa.setText("Plural Form?");
                    break;
                case "Adj.":
                    butn_pas.setText("Synonym?");
                    butn_ppa.setText("Antonym?");
                    break;
            }
        switch (cursor.getString(7)) {
            case "N":
                btn_kydt.setBackground(getResources().getDrawable(dugme));
                txv_mean.setBackground(getResources().getDrawable(button_damla3));
                break;
            case "Y":
                btn_kydt.setBackground(getResources().getDrawable(dugmey));
                txv_mean.setBackground(getResources().getDrawable(button_damla3y));
                break;
        }
        sb=sb+1;
        txv_knwn.setText("-"+top_ks+"-");
        txv_unknw.setText("-"+sb+"-");
    }

    public void showMessage(String title,String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }
}
