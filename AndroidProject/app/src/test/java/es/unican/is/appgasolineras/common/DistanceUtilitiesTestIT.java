package es.unican.is.appgasolineras.common;

import android.location.Location;
import android.os.Build;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import es.unican.is.appgasolineras.common.DistanceUtilities;

/**
 * Pruebas de integración de la clase DistanceUtilitiesTest
 * con la clase Location.
 *
 * @author Alberto Moro Carrera
 */
@RunWith(RobolectricTestRunner.class)
@Config(sdk = {Build.VERSION_CODES.O_MR1})
public class DistanceUtilitiesTestIT {

    private DistanceUtilities sut;

    @Before
    public void setUp() {
        //Creamos la ubicacion actual
        Location locActual = new Location("");
        locActual.setLatitude(33.46472);
        locActual.setLongitude(-3.80444);
    }

    /**
     * Prueba en el que ambas distancias son nulas, se espera
     * recibir un "-".
     * INTTE465235.2a
     */
    @Test
    public void distanciasNulasTest() {
        //Debería devolver "-1" puesto que la gasolinera 1 está más cerca.
        Assert.assertEquals("-", DistanceUtilities.distanceBetweenLocations(null, null));
    }

    /**
     * Prueba en el que las latitudes son negativas
     * recibir el valor correcto sin errores.
     * INTTE465235.2b
     */
    @Test
    public void latitudNegativaTest() {
        Location loc1 = new Location("");
        loc1.setLatitude(-33.46472);
        loc1.setLongitude(33.80444);

        Location loc2 = new Location("");
        loc2.setLatitude(-33.46472);
        loc2.setLongitude(3.80444);
        //2775.68
        Assert.assertEquals("2778,94" , DistanceUtilities.distanceBetweenLocations(loc1, loc2));
    }

    /**
     * Prueba en el que las longitudes son negativas
     * recibir el valor correcto sin errores.
     * INTTE465235.2c
     */
    @Test
    public void longitudNegativaTest() {
        Location loc1 = new Location("");
        loc1.setLatitude(33.46472);
        loc1.setLongitude(-33.80444);

        Location loc2 = new Location("");
        loc2.setLatitude(33.46472);
        loc2.setLongitude(-3.80444);
        //2775.68
        Assert.assertEquals("2778,94" , DistanceUtilities.distanceBetweenLocations(loc1, loc2));
    }

    /**
     * Prueba en el que las dos están a la misma distancia
     * o en la misma posición, se espera recibir un 0.
     * INTTE465235.2d
     */
    @Test
    public void distanciaCeroTest() {
        Location loc1 = new Location("");
        loc1.setLatitude(33.46472);
        loc1.setLongitude(-3.80444);

        Location loc2 = new Location("");
        loc2.setLatitude(33.46472);
        loc2.setLongitude(-3.80444);

        Assert.assertEquals("0,00" , DistanceUtilities.distanceBetweenLocations(loc1, loc2));
    }

    /**
     * Prueba en el que las longitudes son positivas pero
     * diferentes se espera recibir el valor correcto sin errores.
     * INTTE465235.2e
     */
    @Test
    public void diferenteLongitudTest() {
        Location loc1 = new Location("");
        loc1.setLatitude(33.46472);
        loc1.setLongitude(33.80444);

        Location loc2 = new Location("");
        loc2.setLatitude(33.46472);
        loc2.setLongitude(3.80444);
        //2775.68
        Assert.assertEquals("2778,94" , DistanceUtilities.distanceBetweenLocations(loc1, loc2));
    }

    /**
     * Prueba en el que las latitudes son positivas pero
     * diferentes se espera recibir el valor correcto sin errores.
     * INTTE465235.2f
     */
    @Test
    public void diferenteLatitudTest() {
        Location loc1 = new Location("");
        loc1.setLatitude(33.46472);
        loc1.setLongitude(-33.80444);

        Location loc2 = new Location("");
        loc2.setLatitude(3.46472);
        loc2.setLongitude(-33.80444);
        //3339.06
        Assert.assertEquals("3321,18" , DistanceUtilities.distanceBetweenLocations(loc1, loc2));
    }

    /**
     * Prueba en el que las longitudes y longtudes
     * diferentes se espera recibir el valor correcto sin errores.
     * INTTE465235.2g
     */
    @Test
    public void diferenteLatitudYLongitudTest() {
        Location loc1 = new Location("");
        loc1.setLatitude(3.46472);
        loc1.setLongitude(-33.80444);

        Location loc2 = new Location("");
        loc2.setLatitude(33.46472);
        loc2.setLongitude(-3.80444);
        //4565.76
        Assert.assertEquals("4553,84" , DistanceUtilities.distanceBetweenLocations(loc1, loc2));
    }

}
