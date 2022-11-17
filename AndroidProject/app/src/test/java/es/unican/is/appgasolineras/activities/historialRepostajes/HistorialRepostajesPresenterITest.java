package es.unican.is.appgasolineras.activities.historialRepostajes;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.os.Build;

import androidx.test.core.app.ApplicationProvider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.LinkedList;
import java.util.List;

import es.unican.is.appgasolineras.model.Repostaje;
import es.unican.is.appgasolineras.repository.db.GasolineraDatabase;
import es.unican.is.appgasolineras.repository.db.RepostajeDao;

/**
 * Test de integracion del presentador del historial de repostajes
 * junto con su DAO y la base de datos.
 *
 * DEBEN EJECUTARSE DE UNA EN UNA.
 *
 * @author Ivan Ortiz del Noval
 */
@RunWith(RobolectricTestRunner.class)
@Config(sdk = {Build.VERSION_CODES.O_MR1})
public class HistorialRepostajesPresenterITest {

    private HistorialRepostajesPresenter sut;

    private List<Repostaje> repostajes;

    @Mock // sigue siendo un mock
    private IHistorialRepostajesContract.View viewMock;

    private RepostajeDao dao;

    @Before
    public void setUp() {
        // inicializar mocks siempre
        MockitoAnnotations.openMocks(this);
        // no defino aqui comportamiento porque varia en cada caso
    }

    @Test
    public void initCorrectoTest() {
        sut = new HistorialRepostajesPresenter(viewMock);

        // datos reales en la DAO, asi que no hay mocks para ella
        // definir comportamiento mock
        // obtiene el contexto del mock de la vista
        when(viewMock.getGasolineraDb()).thenReturn(
                GasolineraDatabase.getDB(ApplicationProvider.getApplicationContext(), true));

        // crear el contexto adecuado para hacer el test (meter datos correctos)
        dao = GasolineraDatabase.getDB(ApplicationProvider.getApplicationContext(), true).repostajeDao();
        sut.insertaDatosTemp(dao);

        // repostajes que deberia haber
        repostajes = new LinkedList<>();
        Repostaje r1 = new Repostaje();
        Repostaje r2 = new Repostaje();
        r1.setFechaRepostaje("18/10/2022");
        r1.setPrecio("25.0");
        r1.setLocalizacion("CARRETERA CASTILLO SIETEVILLAS KM, S/N");
        r1.setLitros("13");
        r2.setFechaRepostaje("10/10/2022");
        r2.setPrecio("55.83");
        r2.setLocalizacion("CARRETERA ARGOÑOS SOMO KM. 28,7");
        r2.setLitros("26");
        repostajes.add(r1);
        repostajes.add(r2);

        // ver que funciona
        sut.init();

        assert (sut.shownRepostajes.equals(repostajes));
        verify (viewMock).getGasolineraDb();
        verify (viewMock).showHistorialRepostajes(repostajes);
    }

    @Test
    public void initCorrectoAnomaloTest() {

        sut = new HistorialRepostajesPresenter(viewMock);

        // crear una DAO previa para borrar DB y meter datos incorrectos
        when(viewMock.getGasolineraDb()).thenReturn(
                GasolineraDatabase.getDB(ApplicationProvider.getApplicationContext(), true));
        dao = GasolineraDatabase.getDB(ApplicationProvider.getApplicationContext(),
                true).repostajeDao();
        dao.deleteAll();
        // lista de repostajes con datos anomalos, con 10 repostajes,
        repostajes = new LinkedList<>();
        for (int i = 0; i < 10; i++) {
            Repostaje r = new Repostaje();
            r.setId(0); // poner todos con ID 0 para comparar con los de la BD
            r.setFechaRepostaje(String.format("%d/%d/2023", i, 3+i)); // algunas fechas mal
            if (i != 4) {
                r.setLitros(Double.toString(-23 + i*8)); // algun litro negativo
            } else { // poner un litro nulo
                r.setLitros("");
            }
            r.setPrecio(Double.toString(2 * (-3 + i))); // algun precio negativo y poco realista
            if (i > 2) {
                r.setLocalizacion(String.format("Direccion %d", i)); // alguna direccion vacia
            } else {
                r.setLocalizacion(""); // poner alguna localizacion nula
            }
            repostajes.add(r);
        }
        // meter estos repostajes erroneos en la DB (metodo hecho para los test)
        sut.insertaDatosErroneosTemp(dao);
        // ahora en la DAO estan los repostajes incorrectos

        // ver que funciona
        sut.init();

        // poner ID 0 a todos los repostajes del sut, para que se puedan comparar
        List<Repostaje> sutRepostajes = sut.shownRepostajes;
        for (Repostaje r: sutRepostajes) {
            r.setId(0);
        }
        assert (sutRepostajes.equals(repostajes));
        verify (viewMock).showHistorialRepostajes(repostajes);
    }

    @Test
    public void initListaVaciaTest() {
        sut = new HistorialRepostajesPresenter(viewMock);

        when(viewMock.getGasolineraDb()).thenReturn(
                GasolineraDatabase.getDB(ApplicationProvider.getApplicationContext(), true));
        // crear una DAO previa vacia
        dao = GasolineraDatabase.getDB(ApplicationProvider.getApplicationContext(),
                true).repostajeDao();
        dao.deleteAll();

        // ver que funciona
        sut.init();

        assert (sut.shownRepostajes.size() == 0); // comprobar que se tiene una lista vacia
        verify (viewMock).getGasolineraDb();
        // el metodo del show vacio
        verify (viewMock).showHistorialVacio();
    }

    @Test
    public void onAceptarClickedTest() {
        sut = new HistorialRepostajesPresenter(viewMock);
        // datos reales en la DAO, asi que no hay mocks para ella
        // definir comportamiento mock
        // obtiene el contexto del mock de la vista
        when(viewMock.getGasolineraDb()).thenReturn(
                GasolineraDatabase.getDB(ApplicationProvider.getApplicationContext(), true));
        sut.init();

        // ver que si se llama al metodo se llama a la vista para volver
        sut.onAceptarClicked();
        verify(viewMock).openMainView();
    }

    @Test
    public void onReintentarClickedTest() {
        sut = new HistorialRepostajesPresenter(viewMock);
        // datos reales en la DAO, asi que no hay mocks para ella
        // definir comportamiento mock
        // obtiene el contexto del mock de la vista
        when(viewMock.getGasolineraDb()).thenReturn(
                GasolineraDatabase.getDB(ApplicationProvider.getApplicationContext(), true));
        sut.init();

        // ver que si se llama al metodo se llama otra vez a refresh en la vista
        sut.onReintentarClicked();
        verify(viewMock).refresh();
    }
}