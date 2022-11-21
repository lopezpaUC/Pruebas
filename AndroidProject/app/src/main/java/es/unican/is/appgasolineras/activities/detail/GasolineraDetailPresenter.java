package es.unican.is.appgasolineras.activities.detail;

import static es.unican.is.appgasolineras.model.Gasolinera.DIVISA;

import android.content.Context;
import android.location.Location;
import android.text.TextUtils;

import es.unican.is.appgasolineras.common.DistanceUtilities;
import es.unican.is.appgasolineras.common.prefs.IPrefs;
import es.unican.is.appgasolineras.model.Gasolinera;

/**
 * Clase que se encarga de hacer el calculo del precio sumario de la gasolina y
 * pasarselo a la vista, para que esta no haga calculos, y verificar datos erroneos.
 */
public class GasolineraDetailPresenter {

    private Gasolinera gasolinera;
    private IPrefs prefs;

    /**
     * Constructor, pasa la gasolinera.
     * @param gasolinera la gasolinera
     * @param prefs Preferencias de la aplicacion para recoger la ubicacion
     */
    public GasolineraDetailPresenter(Gasolinera gasolinera, IPrefs prefs) {
        this.gasolinera = gasolinera;
        this.prefs = prefs;
    }
    /**
     * Constructor, pasa la gasolinera.
     * @param gasolinera la gasolinera
     */
    public GasolineraDetailPresenter(Gasolinera gasolinera) {
        this.gasolinera = gasolinera;
    }

    /**
     * Retorna el precio sumario de una gasolinera (media del diesel A y gasolina 95).
     * @return String con el precio sumario en dos decimales, - si un precio es negativo
     */
    public String getPrecioSumario() {
        if (gasolinera.getPrecioSumario() == null || gasolinera.getPrecioSumario().equals("-")) {
            return "-";
        }
        return gasolinera.getPrecioSumario() + DIVISA;
    }

    public String getRotulo() {
        String rotulo = gasolinera.getRotulo();
        if (!rotulo.equals("")) {
            return rotulo;
        }
        return "-";
    }

    public String getMunicipio() {
        String txt = "-";
        String municipio = gasolinera.getMunicipio();
        if (!municipio.equals("")) {
            txt = municipio;
        }
        return txt;
    }

    public String getDieselA() {
        String txt = "-";
        String diesel;
        try {
            diesel = gasolinera.getDieselA().substring(0, 4);
            if (!diesel.equals("") && Double.parseDouble(diesel.replace(',', '.')) > 0) {
                txt = diesel + DIVISA;
            }
        } catch (IndexOutOfBoundsException e){
        }
        
        return txt;
    }

    public String getNormal95() {
        String txt = "-";

        String gasolina;
        try {
            gasolina  = gasolinera.getNormal95().substring(0, 4);
            if (!gasolina.equals("")  && Double.parseDouble(gasolina.replace(',', '.')) > 0) {
                txt = gasolina + DIVISA;
            }
        } catch (IndexOutOfBoundsException e){

        }

        return txt;
    }

    public String getSchedule() {
        String txt = "-";
        String schedule = gasolinera.getSchedule();
        if (!schedule.equals("")) {
            txt = schedule;
        }
        return txt;
    }

    public int getLogoId(Context context) {
        String rotulo = gasolinera.getRotulo().toLowerCase();
        int imageID = context.getResources().getIdentifier(rotulo, "drawable", context.getPackageName());
        // Si el rotulo son sólo numeros, el método getIdentifier simplemente devuelve
        // como imageID esos números, pero eso va a fallar porque no tendré ningún recurso
        // que coincida con esos números
        if (imageID == 0 || TextUtils.isDigitsOnly(rotulo)) {
            imageID = context.getResources()
                    .getIdentifier("generic", "drawable", context.getPackageName());
        }
        return imageID;
    }

    public String getDistancia() {
        String txt = "-";
        Location actual;
        try {
            actual = new Location("");
            actual.setLongitude(Double.parseDouble(prefs.getString("longitud")));
            actual.setLatitude(Double.parseDouble(prefs.getString("latitud")));
        } catch (NumberFormatException e) {
            // si no recoge un numero (la preferencia no existe)
            return txt;
        }
        Location gasLoc = gasolinera.getLocation();
        if (gasLoc != null) {
            txt = DistanceUtilities.distanceBetweenLocations(gasLoc, actual) + " km";
        }
        return txt;
    }
}