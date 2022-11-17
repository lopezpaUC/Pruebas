package es.unican.is.appgasolineras.repository.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import es.unican.is.appgasolineras.model.Repostaje;

/**
 * DAO defined with Room
 * Usage: https://developer.android.com/training/data-storage/room
 */
@Dao
public interface RepostajeDao {

    @Query("SELECT * FROM repostajes")
    List<Repostaje> getAll();

    @Insert
    void insertRepostaje(Repostaje r);

    @Update
    void updateRepostaje(Repostaje r);

    @Delete
    void deleteRepostaje(Repostaje r);

    @Insert
    void insertAll(Repostaje... repostajes);

    @Query("DELETE FROM repostajes")
    void deleteAll();

}
