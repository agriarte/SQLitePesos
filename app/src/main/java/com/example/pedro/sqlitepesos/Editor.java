package com.example.pedro.sqlitepesos;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.pedro.sqlitepesos.data.PesoContract.PesoEntry;

public class Editor extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{
    private static final int EXISTING_PET_LOADER=1;
    private Uri mCurrentPesoUri;
    private TextView mTextView;
    private EditText mEditText;
    private Button mBtnEditar;
    private Button mBtnBorrar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        inicializarEditor();
    }

    private void inicializarEditor(){
        mTextView = (TextView) findViewById(R.id.tvFecha1);
        mEditText = (EditText) findViewById(R.id.tvPeso1);
        mBtnEditar = (Button) findViewById(R.id.IDbtnEditar);
        mBtnBorrar = (Button) findViewById(R.id.IDbtnborrar);

        Intent intent = getIntent();
        mCurrentPesoUri = intent.getData();
        getLoaderManager().initLoader(EXISTING_PET_LOADER,null,this);

        mBtnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues values = new ContentValues();
                values.put(PesoEntry.COLUM_PESO_FECHA,mTextView.getText().toString());
                values.put(PesoEntry.COLUM_PESO_PESO,mEditText.getText().toString());
                getContentResolver().update(mCurrentPesoUri,values,null,null);
                Intent intentMain = new Intent(Editor.this,MainActivity.class);
                startActivity(intentMain);
            }
        });

        mBtnBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getContentResolver().delete(mCurrentPesoUri,null,null);
                finish();
            }
        });

    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                PesoEntry._ID,
                PesoEntry.COLUM_PESO_FECHA,
                PesoEntry.COLUM_PESO_PESO
        };

        //Este Loader es donde se ejecutará Content provider query en el Background threat
        return new CursorLoader(this,
                mCurrentPesoUri,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor==null || cursor.getCount()<1){
            return;
        }
        if (cursor.moveToFirst()){
            int fechaColumnIndex = cursor.getColumnIndex(PesoEntry.COLUM_PESO_FECHA);
            int pesoColumnIndex = cursor.getColumnIndex(PesoEntry.COLUM_PESO_PESO);

            String fechaAux = cursor.getString(fechaColumnIndex);
            String pesoAux = cursor.getString(pesoColumnIndex);

            mTextView.setText(fechaAux);
            mEditText.setText(pesoAux);

        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mEditText.setText("");
        mTextView.setText("");

    }
}
