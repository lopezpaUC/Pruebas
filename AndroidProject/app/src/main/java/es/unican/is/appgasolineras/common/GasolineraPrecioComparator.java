package es.unican.is.appgasolineras.common;

import java.util.Comparator;

import es.unican.is.appgasolineras.activities.detail.GasolineraDetailPresenter;
import es.unican.is.appgasolineras.model.Gasolinera;

public class GasolineraPrecioComparator implements Comparator<Gasolinera> {
    /**
     * Compara gasolineras por su precio sumario.
     * @param g1 Una
     * @param g2 Otra
     * @return -1 si g1 tiene un precio sumario inferior que el de g2
     *          0 si el precio sumario de ambas gasolineras es el mismo
     *          1 si g1 tiene un precio sumario superior que el de g2
     */
    @Override
    public int compare(Gasolinera g1, Gasolinera g2) {
        int compare;
        if (g1.getPrecioSumario().equals("-")) {
            compare = 1;
        } else if (g2.getPrecioSumario().equals("-")) {
            compare = -1;
        } else {
            GasolineraDetailPresenter sumarioA = new GasolineraDetailPresenter(g1);
            GasolineraDetailPresenter sumarioB = new GasolineraDetailPresenter(g2);
            compare = sumarioA.getPrecioSumario().compareToIgnoreCase(sumarioB.getPrecioSumario());
        }
        return compare;
    }
}