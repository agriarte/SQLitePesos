package com.example.pedro.sqlitepesos.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Pedro on 14/09/2017.
 */

public class PesoContract {
    public PesoContract() {
    }

    //content authority
    public static final String CONTENT_AUTHORITY = "com.example.pedro.sqlitepesos";
    //Base content authority
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    //añadimos el nombre del path(nombre de la base de datos)
    public static final String PATCH_PESO = "pesoDB";

    public static final class PesoEntry implements BaseColumns{

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATCH_PESO;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATCH_PESO;

        //content intent para acceder a la base de datos del provider
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI,PATCH_PESO);

        public static final String TABLE_NAME = "pesoDB";
        public static final String _ID = BaseColumns._ID;
        public static final String COLUM_PESO_FECHA = "fecha";
        public static final String COLUM_PESO_PESO = "peso";
    }
}
