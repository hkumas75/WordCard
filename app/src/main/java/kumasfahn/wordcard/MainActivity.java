package kumasfahn.wordcard;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
//import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.util.Calendar;

//import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    String type, knwn_State;
    TextView  txv_st;
    Veritabani1 vt;
    Spinner sp, sp_knwn_State;
    Cursor cursor;
    ScrollTextView txv_wod;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txv_st= (TextView) findViewById(R.id.tv_st);
        txv_wod= (ScrollTextView) findViewById(R.id.tv_wod);

        String currentDate = DateFormat.getDateInstance().format(Calendar.getInstance().getTime());
        int dayOfMonth = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);

        toolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("WordCard");


        vt=new Veritabani1(this);
        if (vt.getAllData().getCount()<1){
            txv_wod.setText ("There is no record in your database. Please go to database management and add data..");txv_wod.startScroll();}
        else {
            SQLiteDatabase dbOku = vt.getReadableDatabase();
            cursor = dbOku.rawQuery("SELECT * FROM TABLO_WORD ORDER BY WORD ASC ", null);

            Integer rand = mod(dayOfMonth, cursor.getCount());
            int n = rand;
            cursor.moveToPosition(n);
            txv_wod.setText("The Word of the Day ***  " + currentDate + "  ***  " + cursor.getString(2) + ": " + cursor.getString(3) + "***Example: " + cursor.getString(4));
            txv_wod.setTextColor(Color.GREEN); txv_wod.startScroll();
        }


        // spinner tanımlama
        sp= (Spinner)  findViewById(R.id.spinner);
        sp_knwn_State= (Spinner)  findViewById(R.id.spinner_st);
        spinner_st_refresh();
        spinner_refresh();

        //kelime çalışma ekranına gönderme
        Button butn_go = (Button) findViewById(R.id.btn_go);
        butn_go.setOnClickListener (new View.OnClickListener() {

            @Override
            public void onClick (View v) {
                Intent intent=new Intent(getApplicationContext(),WordCard.class);
                intent.putExtra ("tip", type);
                intent.putExtra ("drm", knwn_State);
                startActivity (intent);
            }

        });

        //veritabanı yönetimi ekranına gönderme
        Button btn_veriynt = (Button) findViewById(R.id.btn_veri);
        btn_veriynt.setOnClickListener (new View.OnClickListener() {

            @Override
            public void onClick (View v) {
                Intent intentv=new Intent(getApplicationContext(),Veritynt.class);
                startActivity (intentv);
            }

        });

    }
    public void spinner_st_refresh(){
        String states []=getResources().getStringArray(R.array.states);
        ArrayAdapter<String>adapter=new ArrayAdapter<>(getApplicationContext(),R.layout.spinner_1, states);
        sp_knwn_State.setAdapter(adapter);
        sp_knwn_State.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 0:
                        knwn_State = "All";
                        break;
                    case 1:
                        knwn_State = "Known";
                        break;
                    case 2:
                        knwn_State = "Unknown";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getApplicationContext(),"Select a state..",Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void spinner_refresh(){
        String types []=getResources().getStringArray(R.array.types);
        ArrayAdapter<String>adapter=new ArrayAdapter<>(getApplicationContext(),R.layout.spinner_1, types);
        sp.setAdapter(adapter);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 0:
                        type = "All";
                        break;
                    case 1:
                        type = "Verb";
                        break;
                    case 2:
                        type = "Noun";
                        break;
                    case 3:
                        type = "Adj.";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getApplicationContext(),"Select a word type..",Toast.LENGTH_SHORT).show();

            }
        });
    }
       public static void setMarqueeSpeed(TextView tv, float speed) {
        if (tv != null) {
            try {
                Field f = null;
                if (tv instanceof AppCompatTextView) {
                    f = tv.getClass().getSuperclass().getDeclaredField("mMarquee");
                } else {
                    f = tv.getClass().getDeclaredField("mMarquee");
                }
                if (f != null) {
                    f.setAccessible(true);
                    Object marquee = f.get(tv);
                    if (marquee != null) {
                        String scrollSpeedFieldName = "mScrollUnit";
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            scrollSpeedFieldName = "mPixelsPerSecond";
                        }
                        Field mf = marquee.getClass().getDeclaredField(scrollSpeedFieldName);
                        mf.setAccessible(true);
                        mf.setFloat(marquee, speed);
                    }
                } else {
                    Log.e("Marquee", "mMarquee object is null.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private int mod(int x, int y)
    {
        int result = x % y;
        if (result < 0)
            result += y;
        return result;
    }

}
