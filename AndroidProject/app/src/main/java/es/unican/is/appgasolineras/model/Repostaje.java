package es.unican.is.appgasolineras.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Clase que representa la tabla "repostaje" en la BD de la app.
 * La tabla almacena en cada fila un determinado repostaje, con los siguientes datos:
 *      Fecha del repostaje
 *      Localizacion
 *      Precio
 *      Litros
 */
@Entity(tableName = "repostajes")
public class Repostaje {

    @PrimaryKey(autoGenerate = true)    @NonNull
    private int id;

    private String fechaRepostaje;
    private String localizacion;
    private String precio;
    private String litros;

    public Repostaje() {
        id = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFechaRepostaje() {
        return fechaRepostaje;
    }

    public void setFechaRepostaje(String fechaRepostaje) {
        this.fechaRepostaje = fechaRepostaje;
    }

    public String getLocalizacion() {
        return localizacion;
    }

    public void setLocalizacion(String localizacion) {
        this.localizacion = localizacion;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getLitros() {
        return litros;
    }

    public void setLitros(String litros) {
        this.litros = litros;
    }

    /**
     * Compara que dos repostajes son iguales.
     * Precondicion: No tiene atributos nulos (salvo su ID)
     * @param obj otro repostaje
     * @return True si son iguales, false en caso contrario
     */
    @Override
    public boolean equals(Object obj) {
        Boolean result = false;
        if (obj == null || obj.getClass() != this.getClass()) {
            return result;
        }
        Repostaje o = (Repostaje) obj;
        if (this.fechaRepostaje.equals(o.fechaRepostaje) && this.localizacion.equals(o.localizacion)
                && this.precio.equals(o.precio) && this.litros.equals(o.litros)) {
            result=true;
        }
        return result;
    }

    @Override
    public int hashCode() {
        return id;
    }

}