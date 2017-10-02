package com.example.pedro.sqlitepesos;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;

import java.text.SimpleDateFormat;

import java.util.Calendar;

import android.net.Uri;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;

import android.widget.EditText;
import android.widget.ListView;


import com.example.pedro.sqlitepesos.data.PesoContract.PesoEntry;
import com.example.pedro.sqlitepesos.data.PesoCursorAdapter;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{
    private static final int PESO_LOADER=0;
    private String fecha;
    private String peso;
    private EditText introPeso;
    private Button btnGuardar;
    private Button btnBorrarTodo;

    private ListView listView;
    private PesoCursorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inicializar();
        mostrarInfoDataBase();
    }

    private void inicializar() {
        //para acceder a base de datos, instanciamos a nuestra subclase de SQLiteOpenHelper

        introPeso = (EditText) findViewById(R.id.IDintropeso);
        btnGuardar = (Button) findViewById(R.id.IDbtnGuardar);
        btnBorrarTodo = (Button) findViewById(R.id.IDbtnBorrarTodo);


        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                peso = introPeso.getText().toString();
                fecha = obtenerFecha();
                //llamara bd para guardar texto;
                insertarPeso();
                mostrarInfoDataBase();
            }
        });

        btnBorrarTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getContentResolver().delete(PesoEntry.CONTENT_URI,null,null);
            }
        });
    }


    private String obtenerFecha(){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String fechaActual = format.format(cal.getTime());

        return fechaActual;
    }
    private void insertarPeso(){
        ContentValues values = new ContentValues();
        values.put(PesoEntry.COLUM_PESO_FECHA, fecha);
        values.put(PesoEntry.COLUM_PESO_PESO, peso);
        Uri newUri = getContentResolver().insert(PesoEntry.CONTENT_URI,values);
    }
    private void mostrarInfoDataBase() {

        listView = (ListView) findViewById(R.id.lvLista);
        adapter = new PesoCursorAdapter(this,null);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this,Editor.class);
                Uri currentPesoUri = ContentUris.withAppendedId(PesoEntry.CONTENT_URI,id);
                intent.setData(currentPesoUri);
                startActivity(intent);

            }
        });


        getLoaderManager().initLoader(PESO_LOADER,null,this);
    }


    @Override
    public android.content.Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                PesoEntry._ID,
                PesoEntry.COLUM_PESO_FECHA,
                PesoEntry.COLUM_PESO_PESO
        };

        //Este Loader es donde se ejecutará Content provider query en el Background threat
        return new CursorLoader(this,
                PesoEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor cursor) {
        adapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }
}


