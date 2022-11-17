package es.unican.is.appgasolineras.activities.historialRepostajes;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.database.sqlite.SQLiteException;

import java.util.LinkedList;
import java.util.List;

import es.unican.is.appgasolineras.model.Repostaje;
import es.unican.is.appgasolineras.repository.db.GasolineraDatabase;
import es.unican.is.appgasolineras.repository.db.RepostajeDao;

/**
 * Test unitario del presentador del historial de repostajes.
 *
 * @author Ivan Ortiz del Noval
 */
public class HistorialRepostajesPresenterTest {
    private HistorialRepostajesPresenter sut;

    private List<Repostaje> repostajes;

    @Mock
    private IHistorialRepostajesContract.View viewMock;
    @Mock
    private RepostajeDao daoMock;
    @Mock
    private GasolineraDatabase dbMock;

    @Before
    public void setUp() {
        // inicializar mocks siempre
        MockitoAnnotations.openMocks(this);
        // no defino aqui comportamiento porque varia en cada caso
    }

    @Test
    public void initCorrectoTest() {
        sut = new HistorialRepostajesPresenter(viewMock);

        // lista de repostajes modelo, con 10 repostajes
        repostajes = new LinkedList<>();
        for (int i = 0; i < 10; i++) {
            Repostaje r = new Repostaje();
            r.setId(i+100);
            r.setFechaRepostaje(String.format("%d/10/2022", i));
            r.setLitros(Double.toString(10 + i));
            r.setPrecio(Double.toString(2 * (10 + i)));
            r.setLocalizacion(String.format("Direccion %d", i));
            repostajes.add(r);
        }
        // definir comportamiento mock
        when(viewMock.getGasolineraDb()).thenReturn(dbMock);
        when(dbMock.repostajeDao()).thenReturn(daoMock);
        when(daoMock.getAll()).thenReturn(repostajes);

        // ver que funciona
        sut.init();
        assert (sut.shownRepostajes.equals(repostajes));
        verify (viewMock).getGasolineraDb();
        verify (viewMock).showHistorialRepostajes(repostajes);
    }

    @Test
    public void initCorrectoAnomaloTest() {
        sut = new HistorialRepostajesPresenter(viewMock);

        // lista de repostajes con datos anomalos, con 10 repostajes
        repostajes = new LinkedList<>();
        for (int i = 0; i < 10; i++) {
            Repostaje r = new Repostaje();
            r.setId(i+100);
            r.setFechaRepostaje(String.format("%d/%d/2023", i, 3+i)); // algunas fechas mal
            r.setLitros(Double.toString(-23 + i*8)); // algun litro negativo
            r.setPrecio(Double.toString(2 * (-3 + i))); // algun precio negativo y poco realista
            if (i > 2) {
                r.setLocalizacion(String.format("Direccion %d", i)); // alguna direccion vacia
            }
            repostajes.add(r);
        }
        // definir comportamiento mock
        when(viewMock.getGasolineraDb()).thenReturn(dbMock);
        when(dbMock.repostajeDao()).thenReturn(daoMock);
        when(daoMock.getAll()).thenReturn(repostajes);

        // ver que funciona
        sut.init();
        assert (sut.shownRepostajes.equals(repostajes));
        verify (viewMock).getGasolineraDb();
        verify (viewMock).showHistorialRepostajes(repostajes);
    }

    @Test
    public void initListaVaciaTest() {
        sut = new HistorialRepostajesPresenter(viewMock);
        // definir comportamiento mock
        when(viewMock.getGasolineraDb()).thenReturn(dbMock);
        when(dbMock.repostajeDao()).thenReturn(daoMock);
        when(daoMock.getAll()).thenReturn(new LinkedList<Repostaje>()); // devuelve lista vacia

        // ver que funciona
        sut.init();

        assert (sut.shownRepostajes.size() == 0);
        verify (viewMock).getGasolineraDb();
        // el metodo del show vacio
        verify (viewMock).showHistorialVacio();
    }

    @Test
    public void initErrorCargaTest() {
        sut = new HistorialRepostajesPresenter(viewMock);
        // definir comportamiento mock, si hay error lanza excepcion
        when(viewMock.getGasolineraDb()).thenReturn(dbMock);
        when(dbMock.repostajeDao()).thenReturn(daoMock);
        when(daoMock.getAll()).thenThrow(new SQLiteException());
        // ver que funciona correctamente
        sut.init(); // esto deberia haber recogido SQLiteException y llamado a showLoadError
        assert (sut.shownRepostajes == null);
        verify (viewMock).getGasolineraDb();
        verify (viewMock).showLoadError();
    }

    @Test
    public void onAceptarClickedTest() {
        sut = new HistorialRepostajesPresenter(viewMock);
        // definir mocks para que el init salga bien
        when(viewMock.getGasolineraDb()).thenReturn(dbMock);
        when(dbMock.repostajeDao()).thenReturn(daoMock);
        when(daoMock.getAll()).thenReturn(null);
        sut.init();
        // ver que si se llama al metodo se llama a la vista para volver
        sut.onAceptarClicked();
        verify(viewMock).openMainView();
    }

    @Test
    public void onReintentarClickedTest() {
        sut = new HistorialRepostajesPresenter(viewMock);
        // definir mocks para que el init salga bien
        when(viewMock.getGasolineraDb()).thenReturn(dbMock);
        when(dbMock.repostajeDao()).thenReturn(daoMock);
        when(daoMock.getAll()).thenReturn(null);
        sut.init();
        // ver que si se llama a view.refresh
        sut.onReintentarClicked();
        verify(viewMock).refresh();
    }
}