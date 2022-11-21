package es.unican.is.appgasolineras.activities.main;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import android.os.Build;

import java.util.ArrayList;
import java.util.List;

import es.unican.is.appgasolineras.activities.toolbar.BarraHerramientasPresenter;
import es.unican.is.appgasolineras.common.prefs.IPrefs;
import es.unican.is.appgasolineras.model.Gasolinera;
import es.unican.is.appgasolineras.repository.IGasolinerasRepository;
import es.unican.is.appgasolineras.repository.rest.GasolinerasServiceConstants;

/**
 * Test unitario del presentador general de la aplicación.
 *
 * Casos de prueba implementados: UPR465236.1a-UPR465236.1e
 *
 * @author Irene Zamanillo Zubizarreta
 */

@RunWith(RobolectricTestRunner.class)
@Config(sdk = {Build.VERSION_CODES.O_MR1})
public class MainPresenterTest {
    private MainPresenter sut;
    private List<Gasolinera> gasolineras;
    private List<Gasolinera> gasolinerasPrecio;
    private List<Gasolinera> gasolinerasDistancia;
    private List<Gasolinera> gasolinerasSinPrecio;
    private List<Gasolinera> gasolinerasSinPrecioOrdenadas;
    private List<Gasolinera> gasolinerasAnomalas;

    @Mock
    private IGasolinerasRepository repositoryMock;

    @Mock
    private IMainContract.View viewMock;

    @Mock
    private IPrefs prefsMock;

    @Before
    public void setUp() {
        //Inicializacion de los mocks
        MockitoAnnotations.openMocks(this);
        //GasolinerasServiceConstants.setStaticURL();
        //Inicializacion generica
        sut = new MainPresenter(viewMock, prefsMock);
        gasolineras = new ArrayList<Gasolinera>();
        gasolinerasPrecio = new ArrayList<Gasolinera>();
        gasolinerasDistancia = new ArrayList<Gasolinera>();
        gasolinerasSinPrecio = new ArrayList<Gasolinera>();
        gasolinerasSinPrecioOrdenadas = new ArrayList<Gasolinera>();
        gasolinerasAnomalas = new ArrayList<Gasolinera>();

        //Solo se da valor a los campos utilizados durante el test (precio, distancia, id)

        //Lista generica de gasolineras (precio mas alto a mas bajo; distancia mas cercana a mas lejana)
        double j = 0.0;
        for (int i = 0; i < 4; i++) {
            Gasolinera g = new Gasolinera();
            g.setId(String.valueOf(i));
            g.setNormal95(String.valueOf(200-i));
            g.setDieselA(String.valueOf(100-i));
            g.setLatitud(String.valueOf(43.4635088652 + j));
            g.setLongitud(String.valueOf(-3.83251340152 + j));
            j += 10.0;
            gasolineras.add(g);
        }

        //Para conseguir algo de variacion en el test, se introducen valores manualmente en la ultima gasolinera
        //De esta forma ordenar la lista no implica solo darle la vuelta
        Gasolinera g = new Gasolinera();
        g.setId(String.valueOf(4));
        g.setNormal95(String.valueOf(200-4));
        g.setDieselA(String.valueOf(100-4));
        //Coordenadas en Australia
        g.setLatitud(String.valueOf(-25.274398));
        g.setLongitud(String.valueOf(133.775136));
        gasolineras.add(0, g);

        //Lista de gasolineras ordenada por precio
        gasolinerasPrecio.add(gasolineras.get(0));
        gasolinerasPrecio.add(gasolineras.get(4));
        gasolinerasPrecio.add(gasolineras.get(3));
        gasolinerasPrecio.add(gasolineras.get(2));
        gasolinerasPrecio.add(gasolineras.get(1));

        //Lista de gasolineras sin precio
        for (int i = 0; i < 4; i++) {
            g = new Gasolinera();
            g.setId(String.valueOf(i));
            g.setNormal95(String.valueOf(200-i));
            g.setDieselA(String.valueOf(i-100));
            g.getPrecioSumario();
            gasolinerasSinPrecio.add(g);
        }

        //Gasolineras sin campo de precio sumario (por tener precios negativos)
        Gasolinera g2 = new Gasolinera();
        g2.setId(String.valueOf(4));
        g2.setNormal95(String.valueOf(-200));
        g2.setDieselA(String.valueOf(-100));
        g2.getPrecioSumario();
        gasolinerasSinPrecio.add(0, g2);


        gasolinerasSinPrecioOrdenadas.add(gasolinerasSinPrecio.get(4));
        gasolinerasSinPrecioOrdenadas.add(gasolinerasSinPrecio.get(3));
        gasolinerasSinPrecioOrdenadas.add(gasolinerasSinPrecio.get(2));
        gasolinerasSinPrecioOrdenadas.add(gasolinerasSinPrecio.get(1));
        gasolinerasSinPrecioOrdenadas.add(gasolinerasSinPrecio.get(0));

        //Lista de gasolineras ordenada por distancia
        gasolinerasDistancia.add(gasolineras.get(1));
        gasolinerasDistancia.add(gasolineras.get(2));
        gasolinerasDistancia.add(gasolineras.get(3));
        gasolinerasDistancia.add(gasolineras.get(4));
        gasolinerasDistancia.add(gasolineras.get(0));

        //Lista con unas gasolienras anomalas
        for (int i = 0; i < 2; i++) {
            g = new Gasolinera();
            g.setId(String.valueOf(i));
            if( i == 0) {
                g.setNormal95("-");
                g.setDieselA("2");
            } else {
                g.setNormal95(String.valueOf(2));
                g.setDieselA("-");
            }
            gasolinerasAnomalas.add(g);
        }

        //Comportamiento de los mocks
        when(viewMock.getGasolineraRepository()).thenReturn(repositoryMock);
        when(repositoryMock.getGasolineras()).thenReturn(gasolineras); //en la mayoria de los casos

        //Gestion de la distancia
        //Las coordenadas estan tomadas de las que se usan manualmente en el emulador
        // y corresponden a Santander
        when(prefsMock.getString("latitud")).thenReturn("43.4635088652");
        when(prefsMock.getString("longitud")).thenReturn("-3.83251340152");
    }

    @AfterClass
    public static void clean() {
        GasolinerasServiceConstants.setMinecoURL();
    }

    //Como doSyncInit() es un método privado, se llama al método init() en los tests
    // porque es el método público que lo contiene


    //Corresponde a UPR465236.1a
    @Test
    public void doSyncInitOrdenarDistancia() {
        //Comportamiento de los mocks
        when(prefsMock.getInt(BarraHerramientasPresenter.ORDENAR)).thenReturn(1);
        sut.init();

        assert(sut.getShownGasolineras().equals(gasolinerasDistancia));
        verify(repositoryMock).getGasolineras();
    }

    //Corresponde a UPR465236.1b
    @Test
    public void doSyncInitOrdenarPrecio() {
        //Comportamiento de los mocks
        when(prefsMock.getInt(BarraHerramientasPresenter.ORDENAR)).thenReturn(2);
        sut.init();

        assert(sut.getShownGasolineras().equals(gasolinerasPrecio));
        verify(repositoryMock).getGasolineras();
    }

    //Corresponde a UPR465236.1c
    @Test
    public void doSyncInitNoOrdenar() {
        //Comportamiento de los mocks
        when(prefsMock.getInt(BarraHerramientasPresenter.ORDENAR)).thenReturn(0);
        sut.init();

        assert(sut.getShownGasolineras().equals(gasolineras));
        verify(repositoryMock).getGasolineras();
    }

    //Corresponde a UPR465236.1d
    @Test
    public void doSyncInitOrdenarErrorPref() {
        //Comportamiento de los mocks
        when(prefsMock.getInt(BarraHerramientasPresenter.ORDENAR)).thenReturn(5);
        sut.init();

        assert(sut.getShownGasolineras().equals(gasolineras));
        verify(repositoryMock).getGasolineras();
    }

    //Corresponde a UPR465236.1e
    @Test
    public void doSyncInitErrorCarga() {
        //Comportamiento de los mocks
        when(repositoryMock.getGasolineras()).thenReturn(null);
        sut.init();

        verify(repositoryMock).getGasolineras();
        verify(viewMock).showLoadError();
    }

    //Corresponde a PR465236.1f
    @Test
    public void doSyncInitCargaSinPrecio() {
        //Comportamiento de los mocks
        when(prefsMock.getInt(BarraHerramientasPresenter.ORDENAR)).thenReturn(2);
        when(repositoryMock.getGasolineras()).thenReturn(gasolinerasSinPrecio);
        sut.init();

        assert(sut.getShownGasolineras().equals(gasolinerasSinPrecioOrdenadas));
        verify(repositoryMock).getGasolineras();
    }

    /**
     * Marcos UPR464705.4A
     */
    @Test
    public void onGasolineraClickedDatosCorrectosTest() {
        //Comportamiento del repositorio con datos correctos
        when(repositoryMock.getGasolineras()).thenReturn(gasolineras);
        sut.init();


        //Comprobamos que el metodo llama correctamente a la vista
        sut.onGasolineraClicked(0);
        Gasolinera gas = sut.getShownGasolineras().get(0);
        //Verificamos que abre la vista
        verify(viewMock).openGasolineraDetails(gas);
        //Verificamos que efectivamente los datos que se ven son correctos PREGUNTAR
        assert(sut.getShownGasolineras().equals(gasolineras));
    }

    /**
     * Marcos UPR464705.4B
     */
    @Test
    public void onGasolineraClickedDatosAusentesTest() {
        //Comportamiento del repositorio con datos correctos
        when(repositoryMock.getGasolineras()).thenReturn(gasolinerasAnomalas);
        sut.init();
        List<Gasolinera> gasolineras = sut.getShownGasolineras();
        int indexAusente = gasolineras.size() - 1;

        //Comprobamos que el metodo llama correctamente a la vista
        sut.onGasolineraClicked(indexAusente);
        Gasolinera gas = sut.getShownGasolineras().get(indexAusente);
        //Verificamos que abre la vista
        verify(viewMock).openGasolineraDetails(gas);

        //Verificamos que efectivamente los datos que se ven son correctos a pesar de ser anomalos
        assert(sut.getShownGasolineras().equals(gasolinerasAnomalas));
    }

    /**
     * Marcos UPR464705.4C
     */
    @Test
    public void onGasolineraClickedDatosIncorrectosTest() {
        //Comportamiento del repositorio con datos correctos
        when(repositoryMock.getGasolineras()).thenReturn(gasolinerasSinPrecio);
        sut.init();

        //Comprobamos que el metodo llama correctamente a la vista
        sut.onGasolineraClicked(0);
        Gasolinera gas = sut.getShownGasolineras().get(0);
        //Verificamos que abre la vista
        verify(viewMock).openGasolineraDetails(gas);
        //Verificamos que efectivamente los datos que se ven son correctos a pesar de ser anomalos
        assert(sut.getShownGasolineras().equals(gasolinerasSinPrecio));
    }

    /**
     * Marcos UPR464705.4D
     */
    @Test
    public void onGasolineraClickedSinDarosTest() {
        //No implementado
    }

    /**
     * Marcos UPR464705.4E
     */
    @Test
    public void onGasolineraClickedIndiceNegativoTest() {
        //Comportamiento del repositorio con datos correctos
        when(repositoryMock.getGasolineras()).thenReturn(gasolinerasAnomalas);
        sut.init();

        //Comprobamos que el metodo llama correctamente a la vista
        sut.onGasolineraClicked(-1);
        //Como no crashea se supone que se trata el caso del indice negativo
    }

    /**
     * Marcos UPR464705.4F
     */
    @Test
    public void onGasolineraClickedIndiceFueraDeRangoTest() {
        //Comportamiento del repositorio con datos correctos
        when(repositoryMock.getGasolineras()).thenReturn(gasolinerasAnomalas);
        sut.init();

        //Comprobamos que el metodo llama correctamente a la vista
        sut.onGasolineraClicked(100000000);
        //Como no crashea se supone que se trata el caso del indice fuera de rango
    }

}
