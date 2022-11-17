package es.unican.is.appgasolineras.repository.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;


import java.util.List;

import es.unican.is.appgasolineras.model.Convenio;

@Dao
public interface ConvenioDao {

    @Query("SELECT * FROM convenios")
    List<Convenio> getAll();

    @Insert
    void insertConvenio(Convenio r);

    @Update
    void updateConvenio(Convenio c);

    @Delete
    void deleteConvenio(Convenio c);

    @Insert
    void insertAll(Convenio... convenios);

    @Query("DELETE FROM convenios")
    void deleteAll();

    @Query("SELECT * FROM convenios WHERE marca = :marca")
    Convenio buscaConvenioPorMarca(String marca);
}
