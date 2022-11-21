package es.unican.is.appgasolineras.activities.toolbar;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static es.unican.is.appgasolineras.activities.toolbar.BarraHerramientasPresenter.ANHADIR;

import android.os.Build;

import androidx.room.Database;
import androidx.test.core.app.ApplicationProvider;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import es.unican.is.appgasolineras.activities.convenios.ConveniosPresenter;
import es.unican.is.appgasolineras.activities.convenios.IConveniosContract;
import es.unican.is.appgasolineras.common.prefs.IPrefs;
import es.unican.is.appgasolineras.common.prefs.Prefs;
import es.unican.is.appgasolineras.repository.db.ConvenioDao;
import es.unican.is.appgasolineras.repository.db.GasolineraDao;
import es.unican.is.appgasolineras.repository.db.GasolineraDatabase;
import es.unican.is.appgasolineras.repository.rest.GasolinerasServiceConstants;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = {Build.VERSION_CODES.O_MR1})
public class BarraHerramientasPresenterITest {
    private BarraHerramientasPresenter sut;
    private ConveniosPresenter sutConvenio;

    @Mock
    private ConvenioDao daoConvenio;
    @Mock
    private GasolineraDao daoGasolinera;
    @Mock
    private GasolineraDatabase db;
    @Mock
    private IConveniosContract.View viewConvenioMock;

    @Mock
    private IBarraHerramientasContract.View viewMock;

    private IPrefs prefs;

    @Before
    public void setUp() {
        //Inicializacion de los mocks
        MockitoAnnotations.openMocks(this);
        prefs = Prefs.from(ApplicationProvider.getApplicationContext());
        //Inicializacion generica
        sut = new BarraHerramientasPresenter(viewMock, prefs);
        sutConvenio = new ConveniosPresenter(viewConvenioMock,prefs);

        when(viewConvenioMock.getDatabase()).thenReturn(
                GasolineraDatabase.getDB(ApplicationProvider.getApplicationContext(), true));

    }

    /**
     * Marcos IPR464971.3A
     */
    @Test
    public void onAnhadeConvenioClickedConMarcasITest() {
        assertEquals(prefs.getInt(ANHADIR),0);
        sut.onAnhadeConvenioClicked();
        //verificamos que cambia las preferencias
        assertEquals(prefs.getInt(ANHADIR),1);
        //verificamos que abre la vista
        verify(viewMock).openConveniosView();
        sutConvenio.init();
        verify(viewConvenioMock).showAnhadirConvenio();
        //Las preferencias vuelven a 0
        assertEquals(prefs.getInt(ANHADIR),0);
    }


    /**
     * Marcos IPR464971.3B
     */
    @Test
    public void onAnhadeConvenioClickedSinMarcasITest() {
        assertEquals(prefs.getInt(ANHADIR),0);
        sut.onAnhadeConvenioClicked();
        //verificamos que cambia las preferencias
        assertEquals(prefs.getInt(ANHADIR),1);
        //verificamos que abre la vista
        verify(viewMock).openConveniosView();
        when(viewConvenioMock.getDatabase()).thenReturn(db);
        when(db.convenioDao()).thenReturn(daoConvenio);
        when(db.gasolineraDao()).thenReturn(daoGasolinera);
        when(daoConvenio.getAll()).thenReturn(null);
        when(daoGasolinera.getAll()).thenReturn(null);

        sutConvenio.init();
        //Verificamos que no llama al show anhir convenios
        verify(viewConvenioMock,times(0)).showAnhadirConvenio();
        assertEquals(prefs.getInt(ANHADIR),0);
    }
}
