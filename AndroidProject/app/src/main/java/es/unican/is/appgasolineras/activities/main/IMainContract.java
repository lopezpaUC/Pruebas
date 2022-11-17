package es.unican.is.appgasolineras.activities.main;

import android.location.Location;

import java.util.List;

import es.unican.is.appgasolineras.common.Callback;
import es.unican.is.appgasolineras.model.Gasolinera;
import es.unican.is.appgasolineras.repository.IGasolinerasRepository;

/**
 * The Main Activity is composed of a Presenter and View, which must expose the methods
 * defined in the following interfaces.
 */
public interface IMainContract {

    /**
     * A Presenter for the Main Activity must implement this functionality
     * These methods (excluding init), are meant to be used by the View.
     */
    public interface Presenter {
        /**
         * Initialization method
         */
        void init();

        /**
         * Refreshes the list without requesting location again.
         */
        void refresh();

        /**
         * This method should be used by the View to notify the Presenter that a Gas Station
         * has been clicked
         * @param index the index of the gas station (position in the list)
         */
        void onGasolineraClicked(int index);

        /**
         * Metodo para cuando se pulsa el boton "reintentar" en la ventana emergente por
         * fallo al acceder a la ubicacion actual.
         */
        void onReintentarGpsClicked();

    }

    /**
     * A View for the Main Activity must implement this functionality
     * These methods (excluding init), are meant to be used by the Presenter.
     */
    public interface View {

        /**
         * Initialization method
         */
        void init();

        /**
         * Returns the Gasolineras Repository object.
         * This object can be used to access gas stations from
         * different sources (REST API, persisted DB, etc.)
         * This method is in the View because it generally requires access to the
         * Android Context, and this is available in the View
         * @return the Repository object to access gas stations
         */
        IGasolinerasRepository getGasolineraRepository();

        /**
         * The View is requested to show a list of gas stations
         * @param gasolineras the list of gas stations
         * @param location the current location
         */
        void showGasolineras(List<Gasolinera> gasolineras, Location location);

        /**
         * The View is requested to show an alert informing that the gas stations were loaded
         * correctly
         * @param gasolinerasCount the number of gas stations that were loaded
         */
        void showLoadCorrect(int gasolinerasCount);

        /**
         * The View is requested to show an alert informing that there was an error while
         * loading the gas stations
         */
        void showLoadError();

        /**
         * The View is requested to show an alert informing that there was an error while
         * getting the current location
         */
        void showGpsError();

        /**
         * View is requested to notify the user when sort by distance is enabled.
         */
        void showDistanceSort();

        /**
         * View is requested to notify the user when sort by increasing price is enabled.
         */
        void showPriceAscSort();

        /**
         * Respuesta para el presenter de la ubicacion del dispositivo.
         * @param cb Callback
         * @return null si no se tienen permisos de ubicacion o hay un fallo raro, la ultima ubicacion
         * del dispositivo si se tienen permisos y to do va bien.
         */
        Location getLocation(Callback<Location> cb);

        /**
         * The View is requested to open a Details view on the given gas station
         * @param gasolinera the gas station
         */
        void openGasolineraDetails(Gasolinera gasolinera);
    }

}
