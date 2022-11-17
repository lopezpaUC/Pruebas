package es.unican.is.appgasolineras.activities.main;

import static es.unican.is.appgasolineras.activities.toolbar.BarraHerramientasPresenter.ORDENAR;

import android.location.Location;

import java.util.Collections;
import java.util.List;

import es.unican.is.appgasolineras.common.Callback;
import es.unican.is.appgasolineras.common.GasolineraPrecioComparator;
import es.unican.is.appgasolineras.common.GasolineraUbicacionComparator;
import es.unican.is.appgasolineras.common.prefs.IPrefs;
import es.unican.is.appgasolineras.model.Gasolinera;
import es.unican.is.appgasolineras.repository.IGasolinerasRepository;

public class MainPresenter implements IMainContract.Presenter {

    private final IMainContract.View view;
    private IGasolinerasRepository repository;
    private List<Gasolinera> shownGasolineras;
    private final IPrefs prefs;
    private Location location;
    private static final String LONGITUD = "longitud";
    private static final String LATITUD = "latitud";

    public MainPresenter(IMainContract.View view, IPrefs prefs) {

        this.view = view;
        this.prefs = prefs;
    }

    @Override
    public void init() {
        if (repository == null) {
            repository = view.getGasolineraRepository();
        }
        if (repository != null) {
            doSyncInit();
        }
        // obtener ubicacion si no se ordena por precio
        if (prefs.getInt(ORDENAR) != 2) {
            view.getLocation(new Callback<>() {
                @Override
                public void onSuccess(Location data) { // en data recibe Location
                    // guardar ubicacion en preferencias para el resto de actividades
                    location = data;
                    prefs.putString(LONGITUD, Double.toString(data.getLongitude()));
                    prefs.putString(LATITUD, Double.toString(data.getLatitude()));

                    // recargar las gasolineras con la distancia
                    MainPresenter.this.refresh();
                }
                @Override
                public void onFailure() {
                    if(MainView.getDebug() != 1){
                        view.showGpsError(); // mostrar error ubicacion
                    }

                }
            });
        }
    }

    @Override
    public void refresh() {
        this.doSyncInit();
    }

    private void doAsyncInit() {
        repository.requestGasolineras(new Callback<List<Gasolinera>>() {
            @Override
            public void onSuccess(List<Gasolinera> data) {
                view.showGasolineras(data, location);
                shownGasolineras = data;
                view.showLoadCorrect(data.size());
            }

            @Override
            public void onFailure() {
                shownGasolineras = null;
                view.showLoadError();
            }
        });
    }

    private void doSyncInit() {
        List<Gasolinera> data = repository.getGasolineras();

        if (data != null) {
            shownGasolineras = data;
            // hacer la ordenacion de las gasolineras usando las preferencias
            sortGasolineras();

        } else {
            shownGasolineras = null;
            view.showLoadError();
        }
    }

    public void sortGasolineras() {
        int sort = prefs.getInt(ORDENAR);
        switch (sort) {
            case 1: // por distancia
                // recoger la distancia (es un nuevo presenter, quizas no lo tiene)
                if (location == null) {
                    // no hay nada guardado en preferencias, error
                    if (prefs.getString(LONGITUD).equals("") ||
                            prefs.getString(LATITUD).equals("")) {
                        view.showGpsError();
                        return;
                    }
                    // si se puede obtener la ubicacion
                    location = new Location("");
                    location.setLongitude(Double.parseDouble(prefs.getString(LONGITUD)));
                    location.setLatitude(Double.parseDouble(prefs.getString(LATITUD)));
                }
                Collections.sort(shownGasolineras, new GasolineraUbicacionComparator(location));
                view.showDistanceSort();
                break;
            case 2: // por precio
                // si se puede obtener la ubicacion
                if (!prefs.getString(LONGITUD).equals("") &&
                        !prefs.getString(LATITUD).equals("")) {
                    location = new Location("");
                    location.setLongitude(Double.parseDouble(prefs.getString(LONGITUD)));
                    location.setLatitude(Double.parseDouble(prefs.getString(LATITUD)));
                }
                Collections.sort(shownGasolineras, new GasolineraPrecioComparator());
                view.showPriceAscSort();
                break;
            default: // si es 0 u otro valor, no ordenar
                view.showLoadCorrect(shownGasolineras.size());
        }
        // mostrar las gasolineras ya ordenadas
        view.showGasolineras(shownGasolineras, location);
    }

    @Override
    public void onGasolineraClicked(int index) {
        if (shownGasolineras != null && index < shownGasolineras.size()) {
            Gasolinera gasolinera = shownGasolineras.get(index);
            view.openGasolineraDetails(gasolinera);
        }
    }

    // no hay metodo de aceptar, porque solo cierra la ventana y eso se hace mejor desde la vista
    @Override
    public void onReintentarGpsClicked() {
        view.init();
    }

    public List<Gasolinera> getShownGasolineras() {
        return shownGasolineras;
    }
}
