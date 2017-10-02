package com.example.pedro.sqlitepesos.data;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;
import com.example.pedro.sqlitepesos.data.PesoContract.PesoEntry;

import com.example.pedro.sqlitepesos.R;

/**
 * Created by Pedro on 27/09/2017.
 */

public class PesoCursorAdapter extends CursorAdapter {

    public PesoCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item,parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView pesoTextView = (TextView) view.findViewById(R.id.tvPeso);
        TextView fechaTextView = (TextView) view.findViewById(R.id.tvFecha);

        int pesoColumnIndex = cursor.getColumnIndex(PesoEntry.COLUM_PESO_PESO);
        int fechaColumnIndex = cursor.getColumnIndex(PesoEntry.COLUM_PESO_FECHA);

        String pesoPeso = cursor.getString(pesoColumnIndex);
        String pesoFecha = cursor.getString(fechaColumnIndex);

        pesoTextView.setText(pesoPeso);
        fechaTextView.setText(pesoFecha);

    }
}
