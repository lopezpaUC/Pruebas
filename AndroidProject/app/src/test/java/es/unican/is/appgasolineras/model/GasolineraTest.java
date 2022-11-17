package es.unican.is.appgasolineras.model;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

public class GasolineraTest extends TestCase {

    private static final String REPSOL = "REPSOL";

    @Test
    public void testGetRotulo() {
        Gasolinera gasolinera = new Gasolinera();
        gasolinera.setRotulo(REPSOL);
        assertEquals(gasolinera.getRotulo(), REPSOL);
    }


}