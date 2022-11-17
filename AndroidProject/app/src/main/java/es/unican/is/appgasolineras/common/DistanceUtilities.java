package es.unican.is.appgasolineras.common;

import android.location.Location;

/**
 * Clase estatica para utilidades con distancias.
 */
public class DistanceUtilities {

    // constructor privado para calidad
    private DistanceUtilities(){}

    /**
     * Devuelve la distancia entre dos ubicaciones como string.
     * @param l1
     * @param l2
     * @return La distancia en km, con 2 decimales y coma decimal, entre las 2
     *         "-" si alguna de las ubicaciones es nula
     */
    public static String distanceBetweenLocations(Location l1, Location l2) {
        if (l1 == null || l2 == null) {
            return "-";
        }
        double distance = l1.distanceTo(l2) / 1000; // por defecto es en metros

        return String.format("%.2f", distance).replace('.', ',');

    }


}
