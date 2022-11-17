package es.unican.is.appgasolineras.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;



/**
 * Clase que representa la tabla "convenios" de la base de datos. Cada convenio tiene los siguientes campos:
 * La tabla almacena en cada fila un determinado repostaje, con los siguientes datos:
 *      Marcha
 *      Descuento
 */

@Entity(tableName = "convenios")
public class Convenio {
    @PrimaryKey(autoGenerate = true) @NonNull
    private int id;

    private String marca;
    private int descuento;

    public Convenio() {
        id = 0;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public int getDescuento() {
        return descuento;
    }

    public void setDescuento(int descuento) {
        this.descuento = descuento;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        Boolean result=false;
        if (obj == null || obj.getClass() != this.getClass()) {
            return result;
        } else if (this.marca.equals(((Convenio) obj).marca) && this.descuento == ((Convenio) obj).descuento) {
            result = true;
        }
        return result;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
