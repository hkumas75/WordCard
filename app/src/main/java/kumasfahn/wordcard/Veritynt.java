package kumasfahn.wordcard;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Locale;


import jxl.Cell;
import jxl.CellType;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

/**
 * Created by ${PACKAGE_NAME} on 9.7.2017.
 */

public class Veritynt extends AppCompatActivity {
    EditText edt_word, edt_exmp, edt_pas, edt_ppa;
    TextView txv_id;
    Button butn_ekle, butn_sil, butn_goster, butn_güncelle, butn_next, butn_prev, butn_tmzl, butn_export, butn_import;
    Spinner spType;
    Veritabani1 vt;
    ListView lv;
    int sıra, id_pos;
    String type, id2;
    boolean isInserted, isImported;
    Cursor cursor;
    LimitedEditText edt_mean;
    Toolbar toolbar;
    LinearLayout layout_fonks, layout_veri;

    //herhangi bir butona tıklandıgında Id ile btn tespiti ve switch case ile uygulanacak metoda gitme
    public View.OnClickListener btnClickListener1=new View.OnClickListener() {
        @Override
        public void onClick (View v) {
            switch (v.getId()){
                case R.id.btn_ekle:
                    veri_ekle ();
                    break;
                case R.id.btn_sil:
                    veri_sil ();
                    break;
                case R.id.btn_goster:
                    veri_göster ();
                    break;
                case R.id.btn_güncelle:
                    veri_güncelle ();
                    break;
                case R.id.btn_tmzl:
                    txv_id.setText("");
                    spType.setSelection(0);
                    edt_word.setText("");
                    edt_mean.setText("");
                    edt_exmp.setText("");
                    edt_pas.setText("");
                    edt_ppa.setText("");
                    break;
                case R.id.btn_export:
                    veri_aktar ();
                    break;
                case R.id.btn_import:
                    veri_al ();
                    break;
            }
        }

    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vrtbynt);

        //edittext ve buttonların tanımlanması
        //veri tabanı oluşturma

        vt=new Veritabani1(this);
        txv_id = (TextView) findViewById(R.id.tv_id);
        spinner_refresh();
        edt_word = (EditText) findViewById(R.id.et_word);
        edt_mean = new LimitedEditText (this);
        edt_mean = (LimitedEditText) findViewById(R.id.et_mean);
        edt_exmp = (EditText) findViewById(R.id.et_exmp);
        edt_pas = (EditText) findViewById(R.id.et_pas);
        edt_ppa = (EditText) findViewById(R.id.et_ppa);
        edt_mean.setMaxLines(5);
        edt_mean.setMaxCharacters(100);
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        lv= (ListView) findViewById(R.id.listview);
        layout_fonks= (LinearLayout) findViewById(R.id.lay_fonks);
        layout_veri= (LinearLayout) findViewById(R.id.lay_veri);



        butn_export = (Button) findViewById(R.id.btn_export);
        butn_export.setOnClickListener(btnClickListener1);
        butn_import = (Button) findViewById(R.id.btn_import);
        butn_import.setOnClickListener(btnClickListener1);
        butn_ekle = (Button) findViewById(R.id.btn_ekle);
        butn_ekle.setOnClickListener(btnClickListener1);
        butn_sil = (Button) findViewById(R.id.btn_sil);
        butn_sil.setOnClickListener(btnClickListener1);
        butn_goster = (Button) findViewById(R.id.btn_goster);
        butn_goster.setOnClickListener(btnClickListener1);
        butn_güncelle = (Button) findViewById(R.id.btn_güncelle);
        butn_güncelle.setOnClickListener(btnClickListener1);
        butn_next = (Button) findViewById(R.id.btn_next);
        butn_prev = (Button) findViewById(R.id.btn_prev);
        butn_tmzl = (Button) findViewById(R.id.btn_tmzl);
        butn_tmzl.setOnClickListener(btnClickListener1);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Database Management");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void veri_ekle () {
        if (type.equals("All"))
            Toast.makeText(this, "Select a word type..", Toast.LENGTH_SHORT).show();
        else {
            isInserted = vt.insertData(type, edt_word.getText().toString(), edt_mean.getText().toString(), edt_exmp.getText().toString(), edt_pas.getText().toString(), edt_ppa.getText().toString());
            if(isInserted)
                Toast.makeText(this,"Data Inserted",Toast.LENGTH_LONG).show();
            else
                Toast.makeText(this,"Data not Inserted",Toast.LENGTH_LONG).show();}
    }

    public void veri_sil () {
       Integer deletedRows = vt.deleteData(id2);
        if(deletedRows > 0) {
            Toast.makeText(this, "Data Deleted", Toast.LENGTH_LONG).show();
            txv_id.setText("");
            spType.setSelection(0);
            edt_word.setText("");
            edt_mean.setText("");
            edt_exmp.setText("");
            edt_pas.setText("");
            edt_ppa.setText("");
        }else
            Toast.makeText(this,"Search the Data to be Deleted",Toast.LENGTH_LONG).show();

    }



    public void veri_göster () {

        vt=new Veritabani1(this);
        if (vt.getAllData().getCount()<1){
            showMessage("WordCard Database", "There is no record in this Database..");return;}

        SQLiteDatabase dbOku = vt.getReadableDatabase();

        if (type.equals("All") && edt_word.getText().toString().equals("")){
            cursor = dbOku.rawQuery("SELECT * FROM TABLO_WORD  ORDER BY WORD COLLATE NOCASE ASC ", null);

        } else if (type.trim().length() > 0 && edt_word.getText().toString().equals("")) {
            cursor = dbOku.rawQuery("SELECT * FROM TABLO_WORD WHERE TYPE='" + type + "' ORDER BY WORD COLLATE NOCASE ASC ", null);

        } else if (edt_word.getText().toString().trim().length() > 0 && type.equals("All")) {
            cursor = dbOku.rawQuery("SELECT * FROM TABLO_WORD WHERE WORD='" + edt_word.getText().toString() + "' ORDER BY WORD COLLATE NOCASE ASC ", null);

        } else {
            cursor = dbOku.rawQuery("SELECT * FROM TABLO_WORD WHERE TYPE='" + type + "' AND WORD='" + edt_word.getText().toString() + "' ORDER BY WORD COLLATE NOCASE ASC ", null);

        }

        final ArrayList<String> liste=new ArrayList<>();
        final ArrayList<String> liste2=new ArrayList<>();
        int i=0;
        while (cursor.moveToNext()){
            i=i+1;
            String  j=String.valueOf(i);
            liste.add(j+ ".  ("+ cursor.getString(1)+ ")  "+ cursor.getString(2)+ "-"+ cursor.getString(5)+ "-"+ cursor.getString(6));
            liste2.add(cursor.getString(0));
        }

        if (liste.isEmpty()){
            Toast.makeText(this,"There is no word according to your criterias..",Toast.LENGTH_LONG).show();
            }
        else {
               final String[] liste1;
                cursor.moveToFirst();
        //while (cursor.moveToNext())
                liste1 = new String[]{cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6)};
                sıra=1;
                txv_id.setText(String.valueOf(sıra) );
                id2=liste1 [0];
                switch (liste1 [1]) {
                    case "All" :
                        id_pos=0; break;
                    case "Verb" :
                        id_pos=1; break;
                    case "Noun" :
                        id_pos=2; break;
                    case "Adj." :
                        id_pos=3; break;
                }

                spType.setSelection(id_pos);

                edt_word.setText(liste1 [2]);
           /* if (k==0){
                showMessage("Error", "Nothing found");return;}*/
                edt_mean.setText(liste1 [3]);
                edt_exmp.setText(liste1 [4]);
                edt_pas.setText(liste1 [5]);
                edt_ppa.setText(liste1 [6]);

            View.OnClickListener btnClickListener2=new View.OnClickListener() {

                @Override
                public void onClick (View v) {
                    switch (v.getId()) {

                        case R.id.btn_next:
                            if (cursor.isClosed()) return;
                            if (cursor.isLast()){
                                cursor.moveToFirst();
                                sıra=0;}
                            else
                            cursor.moveToNext();
                            sıra= sıra+1;
                            txv_id.setText(String.valueOf(sıra) );
                            id2=cursor.getString(0);

                            switch (cursor.getString(1)) {
                                case "All" :
                                    id_pos=0; break;
                                case "Verb" :
                                    id_pos=1; break;
                                case "Noun" :
                                    id_pos=2; break;
                                case "Adj." :
                                    id_pos=3; break;
                            }
                            spType.setSelection(id_pos);
                            edt_word.setText(cursor.getString(2));
                            edt_mean.setText(cursor.getString(3));
                            edt_exmp.setText(cursor.getString(4));
                            edt_pas.setText(cursor.getString(5));
                            edt_ppa.setText(cursor.getString(6));
                            break;

                        case R.id.btn_prev:
                            if (cursor.isClosed()) return;
                            if (cursor.isFirst()){
                                cursor.moveToLast();
                                sıra=liste.size()+1;}
                            else
                            cursor.moveToPrevious();
                            sıra= sıra-1;
                            txv_id.setText(String.valueOf(sıra) );
                            id2=cursor.getString(0);

                            switch (cursor.getString(1)) {
                                case "All" :
                                    id_pos=0; break;
                                case "Verb" :
                                    id_pos=1; break;
                                case "Noun" :
                                    id_pos=2; break;
                                case "Adj." :
                                    id_pos=3; break;
                            }
                            spType.setSelection(id_pos);
                            edt_word.setText(cursor.getString(2));
                            edt_mean.setText(cursor.getString(3));
                            edt_exmp.setText(cursor.getString(4));
                            edt_pas.setText(cursor.getString(5));
                            edt_ppa.setText(cursor.getString(6));
                            break;
                    }}};
            butn_next.setOnClickListener(btnClickListener2);
            butn_prev.setOnClickListener(btnClickListener2);
        }

         final ArrayAdapter<String> adapter=new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_multiple_choice, liste){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view =super.getView(position, convertView, parent);

                TextView textView=(TextView) view.findViewById(android.R.id.text1);
                textView.setTextColor(Color.GREEN);

                return view;
            }
        };

        lv.setAdapter(adapter);

         lv.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
         lv.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {


                @Override
                public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                    int checkedItems = lv.getCheckedItemCount();
                    getSupportActionBar().hide();
                    if (lv.getCount() == checkedItems)
                        mode.setTitle("All items selected");
                    else    mode.setTitle(""+lv.getCheckedItemCount()+" items selected");
                    layout_fonks.setVisibility(View.INVISIBLE);
                    LinearLayout.LayoutParams params2 = (LinearLayout.LayoutParams) layout_veri.getLayoutParams();
                    params2.weight = 0.0f; params2.height=0;
                    layout_veri.setLayoutParams(params2);
                    LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) layout_fonks.getLayoutParams();
                    params1.weight = 0.0f; params1.height=0;
                    layout_fonks.setLayoutParams(params1);
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) lv.getLayoutParams();
                    params.weight = 1.0f; params.height=0;
                    lv.setLayoutParams(params);
                }


                @Override
                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    MenuInflater menuInflater = mode.getMenuInflater();
                    menuInflater.inflate(R.menu.menu_contextual_actionbar,menu);
                    return true;
                }

                @Override
                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    return false;
                }

                @Override
                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    int checkedItems = lv.getCheckedItemCount(), count=0;
                    switch (item.getItemId()){

                        case R.id.menu_delete:

//action on clicking contextual action bar menu item
                            for(int i =0;i<(lv.getCount());i++){

                                if(lv.isItemChecked(i)&&(count<=(checkedItems-1)) ){
                                    vt.deleteData(liste2.get(i));
                                    count=count+1;
                                }
                            }

                            rfrsh();
                            //adapter.notifyDataSetChanged();
                             mode.finish();break;

                        case R.id.menu_delete_all:

//action on clicking contextual action bar menu item
                            for(int i =0;i<(lv.getCount());i++) {

                                if (lv.getCount() == checkedItems)
                                    lv.setItemChecked(i, false);
                                else {lv.setItemChecked(i, true);mode.setTitle("All items selected");}
                            }

                            break;

                    }
                    return true;
                }

                @Override
                public void onDestroyActionMode(ActionMode mode) {
                    layout_fonks.setVisibility(View.VISIBLE);
                    LinearLayout.LayoutParams params2 = (LinearLayout.LayoutParams) layout_veri.getLayoutParams();
                    params2.weight = 0.5f; params2.height=0;
                    layout_veri.setLayoutParams(params2);
                    LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) layout_fonks.getLayoutParams();
                    params1.weight = 0.2f; params1.height=0;
                    layout_fonks.setLayoutParams(params1);
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) lv.getLayoutParams();
                    params.weight = 0.3f;params.height=0;
                    lv.setLayoutParams(params);
                    getSupportActionBar().show();
                }
            });
    }
    public void rfrsh () {

        vt=new Veritabani1(this);
        if (vt.getAllData().getCount()<1){
            showMessage("WordCard Database", "There is no record in this Database..");return;}

        SQLiteDatabase dbOku = vt.getReadableDatabase();


            cursor = dbOku.rawQuery("SELECT * FROM TABLO_WORD  ORDER BY WORD COLLATE NOCASE ASC ", null);


        final ArrayList<String> liste=new ArrayList<>();
        final ArrayList<String> liste2=new ArrayList<>();
        int i=0;
        while (cursor.moveToNext()){
            i=i+1;
            String  j=String.valueOf(i);
            liste.add(j+ ".  ("+ cursor.getString(1)+ ")  "+ cursor.getString(2)+ "-"+ cursor.getString(5)+ "-"+ cursor.getString(6));
            liste2.add(cursor.getString(0));
        }

        if (liste.isEmpty()){
            Toast.makeText(this,"There is no word according to your criterias..",Toast.LENGTH_LONG).show();
        }
        else {
            final String[] liste1;
            cursor.moveToFirst();
            //while (cursor.moveToNext())
            liste1 = new String[]{cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6)};
            sıra=1;
            txv_id.setText(String.valueOf(sıra) );
            id2=liste1 [0];
            switch (liste1 [1]) {
                case "All" :
                    id_pos=0; break;
                case "Verb" :
                    id_pos=1; break;
                case "Noun" :
                    id_pos=2; break;
                case "Adj." :
                    id_pos=3; break;
            }

            spType.setSelection(id_pos);

            edt_word.setText(liste1 [2]);
           /* if (k==0){
                showMessage("Error", "Nothing found");return;}*/
            edt_mean.setText(liste1 [3]);
            edt_exmp.setText(liste1 [4]);
            edt_pas.setText(liste1 [5]);
            edt_ppa.setText(liste1 [6]);}
        final ArrayAdapter<String> adapter=new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_multiple_choice, liste){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view =super.getView(position, convertView, parent);

                TextView textView=(TextView) view.findViewById(android.R.id.text1);
                textView.setTextColor(Color.GREEN);

                return view;
            }
        };
        lv.setAdapter(adapter);
    }

    public void showMessage(String title,String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }
    public void veri_güncelle () {
        //vt=new Veritabani1(this);
        if (edt_word.getText().toString().equals("")){
            Toast.makeText(this,"Enter the word to be updated..",Toast.LENGTH_LONG).show();
            return;}
        boolean isUpdate = vt.updateData(id2, type, edt_word.getText().toString(), edt_mean.getText().toString(), edt_exmp.getText().toString(), edt_pas.getText().toString(), edt_ppa.getText().toString());
        if(isUpdate)
            Toast.makeText(this,"Data Updated",Toast.LENGTH_LONG).show();
        else
            Toast.makeText(this,"Data not Updated",Toast.LENGTH_LONG).show();
    }
    public void veri_aktar () {
        vt=new Veritabani1(this);
        if (vt.getAllData().getCount()<1){
            showMessage("WordCard Database", "There is no record in this Database..");return;}
        SQLiteDatabase dbOku = vt.getReadableDatabase();
        cursor = dbOku.rawQuery("SELECT * FROM TABLO_WORD ORDER BY WORD ASC ", null);
        File sd = Environment.getExternalStoragePublicDirectory("worddb");
        String csvFile = "myData.xls";


        File directory = new File(sd.getAbsolutePath());
        //create directory if not exist
        if (!directory.isDirectory()) {
            directory.mkdirs();
        }
        try {

            //file path
            File file = new File(directory, csvFile);
            WorkbookSettings wbSettings = new WorkbookSettings();
            wbSettings.setLocale(new Locale("en", "EN"));
            WritableWorkbook workbook;
            workbook = Workbook.createWorkbook(file, wbSettings);
            //Excel sheet name. 0 represents first sheet
            WritableSheet sheet = workbook.createSheet("wordList", 0);

            // column and row
            sheet.addCell(new Label(0, 0, "TYPE"));
            sheet.addCell(new Label(1, 0, "WORD"));
            sheet.addCell(new Label(2, 0, "MEANING"));
            sheet.addCell(new Label(3, 0, "EXAMPLE"));
            sheet.addCell(new Label(4, 0, "PST_ART_SYN"));
            sheet.addCell(new Label(5, 0, "PRF_PLR_AYN"));
            sheet.addCell(new Label(6, 0, "COLOR"));

            if (cursor.moveToFirst()) {
                do {
                    String w_type = cursor.getString(cursor.getColumnIndex("TYPE"));
                    String w_word = cursor.getString(cursor.getColumnIndex("WORD"));
                    String w_meaning = cursor.getString(cursor.getColumnIndex("MEANING"));
                    String w_example = cursor.getString(cursor.getColumnIndex("EXAMPLE"));
                    String w_pst = cursor.getString(cursor.getColumnIndex("PST_ART_SYN"));
                    String w_prf = cursor.getString(cursor.getColumnIndex("PRF_PLR_AYN"));
                    String w_color = cursor.getString(cursor.getColumnIndex("COLOR"));

                    int i = cursor.getPosition() + 1;
                    sheet.addCell(new Label(0, i, w_type));
                    sheet.addCell(new Label(1, i, w_word));
                    sheet.addCell(new Label(2, i, w_meaning));
                    sheet.addCell(new Label(3, i, w_example));
                    sheet.addCell(new Label(4, i, w_pst));
                    sheet.addCell(new Label(5, i, w_prf));
                    sheet.addCell(new Label(6, i, w_color));
                } while (cursor.moveToNext());
            }

            //closing cursor
            cursor.close();
            workbook.write();
            workbook.close();
            Toast.makeText(getApplication(),
                    "Data Exported in a Excel Sheet", Toast.LENGTH_SHORT).show();
        } catch(Exception e){
            e.printStackTrace();
        }

    }

    public void veri_al ()  {
        File sd = Environment.getExternalStoragePublicDirectory("worddb");
        String csvFile = "myData.xls";
        String iType = null, iWord = null, iMean= null, iExample= null,iArtkl= null, iPlrl= null, iColor= null;
        Integer imp=0;
        String inputFile = (sd.getAbsolutePath());
        File inputWorkbook = new File(inputFile, csvFile);

        Workbook w;
        try {
            w = Workbook.getWorkbook(inputWorkbook);
                    // Get the first sheet
            Sheet sheet = w.getSheet(0);
                    // Loop over column and lines

            for (int j = 1; j < sheet.getRows(); j++) {
                for (int i = 0; i < sheet.getColumns(); i++) {
                            Cell cell = sheet.getCell(i, j);
                            //CellType type = cell.getType();
                            switch (i) {
                                case 0:
                                     iType=cell.getContents();break;
                                case 1:
                                     iWord=cell.getContents();break;
                                case 2:
                                     iMean=cell.getContents(); break;
                                case 3:
                                     iExample=cell.getContents();break;
                                case 4:
                                     iArtkl=cell.getContents();break;
                                case 5:
                                     iPlrl=cell.getContents();break;
                                case 6:
                                     iColor=cell.getContents();break;
                            }

                }
                    isImported = vt.importData(iType, iWord, iMean, iExample, iArtkl, iPlrl, iColor);
                    if(isImported)
                       imp++;
                    else
                        Toast.makeText(this,iWord+"Data not Imported",Toast.LENGTH_LONG).show();

            }
            Toast.makeText(this,imp+"Data Imported",Toast.LENGTH_LONG).show();
        } catch (IOException e) {
                    e.printStackTrace();
                } catch (BiffException e) {
            e.printStackTrace();
        }
    }
    public void spinner_refresh(){
        spType= (Spinner)  findViewById(R.id.spinner2);
        String types []=getResources().getStringArray(R.array.types);
        ArrayAdapter<String>adapter=new ArrayAdapter<>(getApplicationContext(),R.layout.spinner_1, types);
        spType.setAdapter(adapter);
        spType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

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
                // Toast.makeText(getApplicationContext(),"BİR TEMA SEÇİNİZ..",Toast.LENGTH_SHORT).show();

            }

    });}


}
