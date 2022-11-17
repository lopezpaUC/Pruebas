package es.unican.is.appgasolineras.activities.detail;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import es.unican.is.appgasolineras.activities.detail.GasolineraDetailPresenter;
import es.unican.is.appgasolineras.model.Gasolinera;

public class GasolineraDetailPresenterTest {
    private static final String REPSOL = "REPSOL";

    @Test
    public void testGetRotulo() {
        Gasolinera gasolinera = new Gasolinera();
        // Caso 1 → datos correctos
        gasolinera.setDieselA("1.739");
        gasolinera.setNormal95("1.859");
        GasolineraDetailPresenter sumario = new GasolineraDetailPresenter(gasolinera);
        String sumarioS = sumario.getPrecioSumario();
        assertEquals("1,81 €", sumarioS);

        gasolinera.setDieselA("");
        gasolinera.setNormal95("");
        sumarioS=sumario.getPrecioSumario();
        assertEquals("-", sumarioS);

        gasolinera.setDieselA("0.0");
        gasolinera.setNormal95("0.0");
        sumarioS=sumario.getPrecioSumario();
        assertEquals("-", sumarioS);

        gasolinera.setDieselA("0.0");
        gasolinera.setNormal95("1.53");
        sumarioS=sumario.getPrecioSumario();
        assertEquals("1,53 €", sumarioS);

        // Caso 2 → Datos ausentes
        //Dato del diesel ausente
        gasolinera.setDieselA("-");
        gasolinera.setNormal95("1.859");
        sumarioS=sumario.getPrecioSumario();
        assertEquals("1,85 €", sumarioS);
        //Dato de ambos ausente
        gasolinera.setNormal95("-");
        sumarioS=sumario.getPrecioSumario();
        assertEquals("-", sumarioS);

        //Caso 3 → Valores negativos
        gasolinera.setNormal95("1.739");
        gasolinera.setDieselA("-2.14");
        sumarioS=sumario.getPrecioSumario();
        assertEquals("1,73 €", sumarioS);
    }
}