package es.unican.is.appgasolineras.activities.convenios;

import static es.unican.is.appgasolineras.activities.toolbar.BarraHerramientasPresenter.ANHADIR;

import android.database.sqlite.SQLiteException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import es.unican.is.appgasolineras.common.prefs.IPrefs;
import es.unican.is.appgasolineras.model.Convenio;
import es.unican.is.appgasolineras.model.Gasolinera;
import es.unican.is.appgasolineras.repository.db.ConvenioDao;
import es.unican.is.appgasolineras.repository.db.GasolineraDatabase;

public class ConveniosPresenter implements IConveniosContract.Presenter {

    private final IConveniosContract.View view;
    private List<Convenio> shownConvenios;
    private List<Gasolinera> gasolineras;
    private IPrefs prefs;

    private ConvenioDao conveniosDao;

    public ConveniosPresenter(IConveniosContract.View view, IPrefs prefs) {
        this.view = view;
        this.prefs = prefs;
    }

    @Override
    public void init() {
        final GasolineraDatabase db = view.getDatabase();
        conveniosDao = db.convenioDao();
        List<Convenio> data = null;

        // Fallo en el acceso a datos
        try {
            data = conveniosDao.getAll();
        } catch (SQLiteException e) {
            view.showLoadError();
        }
        if (data!=null && !data.isEmpty()) {
            // Caso exito
            view.showConvenios(data);
            shownConvenios = data;
        } else {
            // No existen convenios para mostrar
            view.showListaConveniosVacia();
            shownConvenios = null;
        }

        if (prefs.getInt(ANHADIR) == 1) {
            //Extrae las marcas de todas las gasolineras
            Set<String> marcas = new HashSet<>();
            try {
                gasolineras = db.gasolineraDao().getAll();
            } catch (SQLiteException e) {
                // Error en carga de marcas
                view.showLoadError();
            }

            if (gasolineras != null) {
                // Caso exito
                for (Gasolinera g: gasolineras) {
                    marcas.add(g.getRotulo());
                }
                marcas.remove("(SIN RÓTULO)");
                marcas.remove("12241");
                List<String> marcasOrdenadas = new ArrayList<>(marcas);
                Collections.sort(marcasOrdenadas);
                view.setMarcas(marcasOrdenadas);
                view.showAnhadirConvenio();
            }

            prefs.putInt(ANHADIR, 0);
        }
    }

    @Override
    public void onErrorAceptarClicked() {
        view.openMainView();
    }

    @Override
    public void onErrorReintentarClicked() {
        view.refresh();
    }

    @Override
    public void onConvenioAnhadirClicked(String descuento, String marca) {

        // Caso descuento vacio
        if (descuento.equals("")) {
            view.showErrorDescuento();
            return;
        }

        // Caso descuento no numerico
        Integer descuentoParsed = null;
        try {
            descuentoParsed = Integer.parseInt(descuento);
        } catch (NumberFormatException e) {
            view.showErrorDescuento();
            return;
        }

        // Caso descuento fuera de rango
        if (descuentoParsed <= 0 || descuentoParsed >= 100) {
            view.showErrorDescuento();
            return;
        }

        //Crea el convenio
        Convenio convenio = new Convenio();
        convenio.setDescuento(descuentoParsed);
        convenio.setMarca(marca);

        //Comprueba si ya existe un convenio asociado a la marca y persiste el convenio en la BD
        //si no estaba ya insertado
        Convenio convenioAnterior = conveniosDao.buscaConvenioPorMarca(convenio.getMarca());

        if (convenioAnterior != null) {
            // Caso sobrescribir convenio
            view.showSobreescribirConvenio(convenio);
        } else {
            // Caso exito
            insertaConvenio(convenio);
        }
    }

    @Override
    public void onConvenioCancelarClicked() {
        //No hace nada (solo cierra la ventana)
    }

    @Override
    public void onSiSobreescribirClicked(Convenio c) {
        Convenio cAnterior = conveniosDao.buscaConvenioPorMarca(c.getMarca());
        cAnterior.setMarca(c.getMarca());
        cAnterior.setDescuento(c.getDescuento());

        conveniosDao.updateConvenio(cAnterior);
        view.refresh();
        view.showConvenioAnhadido();
    }

    private void insertaConvenio(Convenio c) {
        conveniosDao.insertConvenio(c);
        view.refresh();
        view.showConvenioAnhadido();
    }

    @Override
    public void onNoSobreescribirClicked() {
        //No hace nada (solo cierra la ventana)
    }

    @Override
    public void onErrorDescuentoAceptarClicked() {
        view.showAnhadirConvenio();
    }

    /**
     * Inserta dos convenios en la DAO para poder probar la historia de usuario "Ver
     * convenios de precios".
     * @param conveniosDao la DAO de convenios.
     */
    public void insertaDatosTemp(ConvenioDao conveniosDao) {
        Convenio c1 = new Convenio();
        c1.setMarca("Campsa");
        c1.setDescuento(20);
        Convenio c2 = new Convenio();
        c2.setMarca("Galp");
        c2.setDescuento(5);
        Convenio c3 = new Convenio();
        c3.setMarca("Repsol");
        c3.setDescuento(-10);
        Convenio c4 = new Convenio();
        c4.setMarca("");
        conveniosDao.insertConvenio(c1);
        conveniosDao.insertConvenio(c2);
        conveniosDao.insertConvenio(c3);
        conveniosDao.insertConvenio(c4);
    }


    public List<Convenio> getShownConvenios(){
        return shownConvenios;
    }

    public List<Gasolinera> getGasolineras() {
        return gasolineras;
    }
}