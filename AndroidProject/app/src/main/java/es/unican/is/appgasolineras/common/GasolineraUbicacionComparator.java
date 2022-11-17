package es.unican.is.appgasolineras.common;

import android.location.Location;

import java.util.Comparator;

import es.unican.is.appgasolineras.model.Gasolinera;

/**
 * Clase para comparar gasolineras por su ubicacion respecto a una dada.
 */
public class GasolineraUbicacionComparator implements Comparator<Gasolinera> {

    // ubicacion por la que compara, cuanto mas cerca esta de esta, mas prioridad tiene
    private Location ubicacionBase;

    public void setUbicacionBase(Location base) { this.ubicacionBase = base; }

    public GasolineraUbicacionComparator(Location ubicacionBase) {
        this.ubicacionBase = ubicacionBase;
    }

    /**
     * Compara gasolineras por su distancia a la base.
     * @param gasolinera Una
     * @param otra Otra
     * @return  <0 si Una esta mas cerca que la otra de la ubicacion base
     *           0 si estan a la misma distancia
     *          >0 si otra esta mas cerca que una de la ubicacion base
     */
    @Override
    public int compare(Gasolinera gasolinera, Gasolinera otra) {

        double d1 = ubicacionBase.distanceTo(gasolinera.getLocation());
        double d2 = ubicacionBase.distanceTo(otra.getLocation());
        return Double.compare(d1, d2);
    }
}
