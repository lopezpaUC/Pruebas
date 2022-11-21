package es.unican.is.appgasolineras.activities.main;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.os.Bundle;

import androidx.test.core.app.ApplicationProvider;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.List;

import es.unican.is.appgasolineras.activities.detail.GasolineraDetailPresenter;
import es.unican.is.appgasolineras.activities.detail.GasolineraDetailView;
import es.unican.is.appgasolineras.common.prefs.IPrefs;
import es.unican.is.appgasolineras.common.prefs.Prefs;
import es.unican.is.appgasolineras.model.Gasolinera;
import es.unican.is.appgasolineras.repository.GasolinerasRepository;
import es.unican.is.appgasolineras.repository.IGasolinerasRepository;
import es.unican.is.appgasolineras.repository.db.GasolineraDao;
import es.unican.is.appgasolineras.repository.db.GasolineraDatabase;
import es.unican.is.appgasolineras.repository.rest.GasolinerasServiceConstants;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = {Build.VERSION_CODES.O_MR1})
public class MainPresenterITest {

    private MainPresenter sut;
    private GasolineraDetailPresenter detailPresenter;

    @Mock
    private IMainContract.View viewMock;
    private IPrefs prefs;
    private static GasolineraDatabase db;

    private GasolinerasRepository repMock;

    @Before
    public void setUp() {
        //GasolinerasServiceConstants.setStaticURL();
        //Inicializacion de los mocks
        MockitoAnnotations.openMocks(this);
        prefs = Prefs.from(ApplicationProvider.getApplicationContext());
        //Usar la URL statica
        GasolinerasServiceConstants.setStaticURL();
        //Al ser la vista un mock hay que mockear el comportamiento del repository o sera nulo
        repMock = new GasolinerasRepository(ApplicationProvider.getApplicationContext());
        when(viewMock.getGasolineraRepository()).thenReturn(repMock);
        //Hay que forzar la base de datos
        db =  GasolineraDatabase.getDB(ApplicationProvider.getApplicationContext(),true);
        //Inicializacion generica
        sut = new MainPresenter(viewMock, prefs);


    }

    @AfterClass
    public static void clean() {
        //vOVLER A USAR LA URL NORMAL
        GasolinerasServiceConstants.setMinecoURL();
    }


    /**
     * Marcos IPR464705.4A
     */
    @Test
    public void onGasolineraClickedDatosCorrectosTest() {
        //Comportamiento del repositorio con datos correctos
        sut.init();
        //Comprobamos que el metodo llama correctamente a la vista
        Gasolinera gas = sut.getShownGasolineras().get(0);
        sut.onGasolineraClicked(0);
        verify(viewMock).openGasolineraDetails(gas);
        detailPresenter = new GasolineraDetailPresenter(gas, prefs);
        //Verificamos que efectivamente los datos que se ven son correctos
        assertEquals(detailPresenter.getDieselA(),"1,99 €");
        assertEquals(detailPresenter.getNormal95(),"1,85 €");
        assertEquals(detailPresenter.getPrecioSumario(),"1,90 €");
        assertEquals(detailPresenter.getMunicipio(),"Alfoz de Lloredo");
        assertEquals(detailPresenter.getRotulo(),"CEPSA");
        assertEquals(detailPresenter.getSchedule(),"L-D: 08:00-21:00");
    }

    /**
     * Marcos IPR464705.4B
     */
    @Test
    public void onGasolineraClickedDatosAusentesTest() {
        sut.init();
        //Buscamos la gasolienra anomala
        List<Gasolinera> gasolineras = sut.getShownGasolineras();
        int indexAusente = gasolineras.size() - 1;

        Gasolinera gas = gasolineras.get(indexAusente);
        sut.onGasolineraClicked(indexAusente);
        //Verificamos que abre la vista
        verify(viewMock).openGasolineraDetails(gas);
        detailPresenter = new GasolineraDetailPresenter(gas, prefs);

        //Verificamos que efectivamente los datos que se ven son correctos a pesar de haber ausentes
        assertEquals(detailPresenter.getMunicipio(),"Voto");
        assertEquals(detailPresenter.getNormal95(),"1,79 €");
        assertEquals(detailPresenter.getRotulo(),"-");
        assertEquals(detailPresenter.getDieselA(),"-");
        assertEquals(detailPresenter.getPrecioSumario(),"1,79 €");
        assertEquals(detailPresenter.getSchedule(),"L-D: 09:00-20:30");
    }

    /**
     * Marcos IPR464705.4C
     */
    @Test
    public void onGasolineraClickedDatosAnomalosTest() {
        sut.init();
        //Buscamos la gasolienra anomala
        List<Gasolinera> gasolineras = sut.getShownGasolineras();
        int indexAusente = gasolineras.size() - 2;

        Gasolinera gas = gasolineras.get(indexAusente);
        sut.onGasolineraClicked(indexAusente);
        //Verificamos que abre la vista
        verify(viewMock).openGasolineraDetails(gas);
        detailPresenter = new GasolineraDetailPresenter(gas, prefs);
        //Verificamos que efectivamente los datos que se ven son correctos a pesar de ser anomalos
        assertEquals(detailPresenter.getMunicipio(),"Villafufre");
        assertEquals(detailPresenter.getNormal95(),"1,73 €");
        assertEquals(detailPresenter.getRotulo(),"AVIA");
        assertEquals(detailPresenter.getDieselA(),"-");

        assertEquals(detailPresenter.getPrecioSumario(),"1,73 €");


        assertEquals(detailPresenter.getSchedule(),"L-S: 06:00-22:00; D: 08:00-22:00");
    }

    /**
     * Marcos IPR464705.4D
     */
    @Test
    public void onGasolineraClickedSinDarosTest() {
        //No implementado en la aplicacion
    }

    /**
     * Marcos IPR464705.4E
     */
    @Test
    public void onGasolineraClickedIndiceNegativoTest() {
        sut.init();
        //Comprobamos que el metodo llama correctamente a la vista
        sut.onGasolineraClicked(-1);
        //Como no crashea se supone que se trata el caso del indice negativo

    }

    /**
     * Marcos IPR464705.4F
     */
    @Test
    public void onGasolineraClickedIndiceFueraDeRangoTest() {
        sut.init();
        //Comprobamos que el metodo llama correctamente a la vista
        sut.onGasolineraClicked(100000000);
        //Como no crashea se supone que se trata el caso del indice fuera de rango
    }
}
