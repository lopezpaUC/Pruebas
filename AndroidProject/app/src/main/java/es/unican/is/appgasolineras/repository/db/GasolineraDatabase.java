package es.unican.is.appgasolineras.repository.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import es.unican.is.appgasolineras.model.Convenio;
import es.unican.is.appgasolineras.model.Gasolinera;
import es.unican.is.appgasolineras.model.Repostaje;

/**
 * Class to persist gas stations into a local DB using Room
 * Usage: https://developer.android.com/training/data-storage/room
 */
@Database(entities = {Gasolinera.class, Convenio.class, Repostaje.class}, version = 1, exportSchema = false)
public abstract class GasolineraDatabase extends RoomDatabase {

    public static final String GASOLINERAS_DB_NAME = "gasolineras-database";

    private static GasolineraDatabase db;
    public static GasolineraDatabase getDB(Context context){
        return getDB(context,false);
    }

    // variable booleana anadida para ejecutar los test de integracion sin fallos de Threads
    public static GasolineraDatabase getDB(Context context, Boolean force) {
        if (Boolean.TRUE.equals(force) || db == null || !db.isOpen()) {
            db = Room
                    .databaseBuilder(context, GasolineraDatabase.class, GasolineraDatabase.GASOLINERAS_DB_NAME)
                    .allowMainThreadQueries()
                    .build();
        }
        return db;
    }

    public abstract GasolineraDao gasolineraDao();

    public abstract RepostajeDao repostajeDao();
    public abstract ConvenioDao convenioDao();
}
