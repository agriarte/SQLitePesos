package com.example.pedro.sqlitepesos.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.pedro.sqlitepesos.data.PesoContract.PesoEntry;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.FileNotFoundException;


/**
 * Created by Pedro on 20/09/2017.
 */

public class PesoProvider extends ContentProvider {
    //Uri Matcher Code para la tabla de peso
    public static final int PESO = 0;
    //Uri Matcher Code para un único resultado de la table de peso
    public static final int PESO_ID = 1;
    //Objeto UriMatcher para comprobar el Content Uri
    public static final UriMatcher sUrimatcher = new UriMatcher(UriMatcher.NO_MATCH);

    //static se ejecuta solo la primera vez que algo es llamado desde la clase
    static {
        sUrimatcher.addURI(PesoContract.CONTENT_AUTHORITY,PesoContract.PATCH_PESO, PESO);
        sUrimatcher.addURI(PesoContract.CONTENT_AUTHORITY,PesoContract.PATCH_PESO +"/#", PESO_ID);
    }

    /**********
    Inicializa el Provider y el objeto database helper
     *********/

    private PesoDbHelper mDbHelper;
    @Override
    public boolean onCreate() {
        mDbHelper = new PesoDbHelper(getContext());
        return true;
    }

    /**********
     Realiza la solicitud para la Uri. Necesita uri,projection,selection,selection arguments and sort order
     *********/
    @Override
    public Cursor query(Uri uri,String[] projection,String selection,String[] selectionArgs,String sortOrder) {
        //base de datos en modo lectura
        SQLiteDatabase database = mDbHelper.getReadableDatabase();
        //Cursor con el resultado de la solicitud
        Cursor cursor;
        //match Uri
        int match = sUrimatcher.match(uri);
        switch (match){
            case PESO:
                cursor = database.query(PesoEntry.TABLE_NAME,projection, selection, selectionArgs,null,null,sortOrder);
                break;
            case PESO_ID:
                selection = PesoEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(PesoEntry.TABLE_NAME,projection, selection, selectionArgs,null,null,sortOrder);
                break;
            default:
                throw new IllegalArgumentException("URI no conocida " + uri);

        }
        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return cursor;
    }

    /**********
     Insertar nuevos datos en conten provider
     *********/

    @Override
    public Uri insert(Uri uri, ContentValues contentValues){
        final int match = sUrimatcher.match(uri);
        switch (match) {
            case PESO:
                return insertPeso(uri, contentValues);
            default:
                throw new IllegalArgumentException("Datos no validos "+ uri);
        }
    }
    private Uri insertPeso(Uri uri,ContentValues contentValues){
        //base datos modo escritura
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        //insertamos el nuevo peso con los datos
        long id = database.insert(PesoEntry.TABLE_NAME,null,contentValues);
        //Si id == -1 ha habido un error al insertar
        if (id==-1){
            Log.e(PesoProvider.class.getSimpleName(),"fallo al insertar datos" + uri);
            return null;
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return ContentUris.withAppendedId(uri,id);
    }
    /**********
     actualiza los datos con la selection y selectionArgs, con el new ContenValues
     *********/
    @Override
    public int update(Uri uri,ContentValues contentValues,String selection,String[] selectionArgs) {
        final int match = sUrimatcher.match(uri);
        switch (match){
            case PESO:
                return updatePeso(uri,contentValues,selection,selectionArgs);
            case PESO_ID:
                selection = PesoEntry._ID +"=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                return updatePeso(uri,contentValues,selection,selectionArgs);
            default:
                throw new IllegalArgumentException("Update no soportado para " + uri);
        }
    }
    private int updatePeso(Uri uri,ContentValues values,String selection,String[] selectionArgs) {
        if (values.containsKey(PesoEntry.COLUM_PESO_PESO)){
            String pesoAux = values.getAsString(PesoEntry.COLUM_PESO_PESO);
            if (pesoAux==null){
                throw new IllegalArgumentException("Peso requiere Peso");
            }
        }
        if (values.containsKey(PesoEntry.COLUM_PESO_FECHA)){
            String fechaAux = values.getAsString(PesoEntry.COLUM_PESO_FECHA);
            if (fechaAux==null){
                throw new IllegalArgumentException("Peso requiere Fecha");
            }
        }
        // si esto va bien obtener db en modo escritura para updatear
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        int rowsupdated = database.update(PesoEntry.TABLE_NAME,values,selection,selectionArgs);
        if (rowsupdated!=0){
            getContext().getContentResolver().notifyChange(uri,null);
        }

        // devolver row rellenado
        return rowsupdated;
    }
    /**********
     borra los datos con la selection y selectionArgs
     *********/
    @Override
    public int delete(Uri uri,String selection,String[] selectionArgs) {
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        int rowsDeleted;
        final int match = sUrimatcher.match(uri);
        switch (match){
            case PESO:
                //borrar todos los rows
                rowsDeleted = database.delete(PesoEntry.TABLE_NAME,selection,selectionArgs);
                break;
            case PESO_ID:
                //borrar solo un row por su ID
                selection = PesoEntry._ID+"=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(PesoEntry.TABLE_NAME,selection,selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Delete no soportado para " + uri);
        }
        if (rowsDeleted!=0){
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return rowsDeleted;
    }
    /**********
     devuelve de MIME type of data for teh content uri
     *********/
    @Override
    public String getType(Uri uri){
        final int match = sUrimatcher.match(uri);
        switch (match){
            case PESO:
                return PesoEntry.CONTENT_LIST_TYPE;
            case  PESO_ID:
                return PesoEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Uri desconocida: " + uri + " con match: " +match);
        }
    }

}
