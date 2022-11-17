package es.unican.is.appgasolineras.activities.historialRepostajes;


import android.database.sqlite.SQLiteException;

import java.util.List;

import es.unican.is.appgasolineras.model.Repostaje;
import es.unican.is.appgasolineras.repository.db.GasolineraDatabase;
import es.unican.is.appgasolineras.repository.db.RepostajeDao;

public class HistorialRepostajesPresenter implements IHistorialRepostajesContract.Presenter{

    private final IHistorialRepostajesContract.View view;

    List<Repostaje> shownRepostajes;


    public HistorialRepostajesPresenter(IHistorialRepostajesContract.View view) {
        this.view = view;
    }


    @Override
    public void init() {
        //Instanciamos la base de datos
        final GasolineraDatabase db = view.getGasolineraDb();
        //a partir de la base de datos podemos obtener la dao de repostajes con laqueinteractuar
        final RepostajeDao repDao = db.repostajeDao();

        //Estructurapara sacarelhistorial de repostajes de nuestra base de datos
        try {
            shownRepostajes = repDao.getAll();
        } catch (SQLiteException e)
        {
            view.showLoadError();
        }
        
        //Si esta vacía mostramos se lo indicamos al usuario
        if(shownRepostajes == null ||  shownRepostajes.isEmpty()) {
            view.showHistorialVacio();
        }else {
            view.showHistorialRepostajes(shownRepostajes);
        }
    }


    /**
     * Inserta dos repostajes en la DAO para poder probar la historia de usuario "Ver
     * convenios de precios".
     * @param repostajeDao la DAO de repostajes.
     */
    public void insertaDatosTemp(RepostajeDao repostajeDao) {

        Repostaje r1 = new Repostaje();
        r1.setFechaRepostaje("18/10/2022");
        r1.setPrecio("25.0");
        r1.setLocalizacion("CARRETERA CASTILLO SIETEVILLAS KM, S/N");
        r1.setLitros("13");
        Repostaje r2 = new Repostaje();
        r2.setFechaRepostaje("10/10/2022");
        r2.setPrecio("55.83");
        r2.setLocalizacion("CARRETERA ARGOÑOS SOMO KM. 28,7");
        r2.setLitros("26");
        repostajeDao.insertRepostaje(r1);
        repostajeDao.insertRepostaje(r2);
    }

    /**
     * Inserta  repostajes con datos supuestamente erroeneos en la DAO para poder probar la historia de usuario "Ver
     * convenios de precios".
     * @param repostajeDao la DAO de repostajes.
     */
    public void insertaDatosErroneosTemp(RepostajeDao repostajeDao) {
        for (int i = 0; i < 10; i++) {
            Repostaje r = new Repostaje();
            r.setFechaRepostaje(String.format("%d/%d/2023", i, 3+i)); // algunas fechas mal
            if (i != 4) {
                r.setLitros(Double.toString(-23 + (double)i*8)); // algun litro negativo
            } else { // poner un litro nulo
                r.setLitros("");
            }
            r.setPrecio(Double.toString(2 * (-3 + (double)i))); // algun precio negativo y poco realista
            if (i > 2) {
                r.setLocalizacion(String.format("Direccion %d", i)); // alguna direccion vacia
            } else {
                r.setLocalizacion(""); // poner alguna localizacion nula
            }
            repostajeDao.insertRepostaje(r); // meter repostaje incorrecto en la DAO
        }
    }

    @Override
    public void onAceptarClicked() {
        view.openMainView();
    }

    @Override
    public void onReintentarClicked() {
        view.refresh();
    }


}
