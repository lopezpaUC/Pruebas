package es.unican.is.appgasolineras.activities.toolbar;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static es.unican.is.appgasolineras.activities.toolbar.BarraHerramientasPresenter.ANHADIR;

import es.unican.is.appgasolineras.activities.convenios.ConveniosPresenter;
import es.unican.is.appgasolineras.activities.convenios.IConveniosContract;
import es.unican.is.appgasolineras.activities.main.MainPresenter;
import es.unican.is.appgasolineras.activities.toolbar.BarraHerramientasPresenter;
import es.unican.is.appgasolineras.common.prefs.IPrefs;

/**
 * Test unitario del presentador de la barra de herramientas.
 *
 * Casos de prueba implementados: UBHPR465236.1a-UBHPR465236.1c
 * y UOGD465235.1-3
 * @author Irene Zamanillo Zubizarreta y Alberto Moro Carrera
 */
public class BarraHerramientasPresenterTest {

    private BarraHerramientasPresenter sut;

    @Mock
    private IBarraHerramientasContract.View viewMock;

    @Mock
    private IPrefs prefsMock;

    @Before
    public void setUp() {
        //Inicializacion de los mocks
        MockitoAnnotations.openMocks(this);

        //Inicializacion generica
        sut = new BarraHerramientasPresenter(viewMock, prefsMock);
    }

    /**
     * Alberto Moro Carrera
     * Primer test de onOrdenardDistanciaClicked
     * Comprueba el caso base en el que se selecciona
     * sin una selección previa.
     * UOGD465235.1
     */
    @Test
    public void onOrdenarDistanciaClicked() {
        when(prefsMock.getInt(BarraHerramientasPresenter.ORDENAR)).thenReturn(0);
        sut.onOrdenarDistanciaClicked();

        verify(prefsMock).putInt(BarraHerramientasPresenter.ORDENAR, 1);
        verify(viewMock).openMainView();
    }

    /**
     * Alberto Moro Carrera
     * Segundo test de onOrdenardDistanciaClicked
     * Comprueba el caso base de en el que se selecciona
     * nuevamente, volviendo a la ordenación por defecto.
     * UOGD465235.2
     */
    @Test
    public void onOrdenarDistanciaClickedDesmarcar() {
        when(prefsMock.getInt(BarraHerramientasPresenter.ORDENAR)).thenReturn(1);
        sut.onOrdenarDistanciaClicked();

        verify(prefsMock).putInt(BarraHerramientasPresenter.ORDENAR, 0);
        verify(viewMock).openMainView();
    }

    /**
     * Alberto Moro Carrera
     * Tercer test de onOrdenardDistanciaClicked
     * Comprueba el caso base de en el que está seleccionada
     * la opción de ordenar por precio y se pasa a ordenar por
     * distancia.
     * UOGD465235.3
     */
    @Test
    public void onOrdenarDistanciaClickedTrasOrdenarPorPrecio() {
        when(prefsMock.getInt(BarraHerramientasPresenter.ORDENAR)).thenReturn(2);
        sut.onOrdenarDistanciaClicked();

        verify(prefsMock).putInt(BarraHerramientasPresenter.ORDENAR, 1);
        verify(viewMock).openMainView();
    }

    //Corresponde a UBHPR465236.1a
    //Se pulsa el boton de ordenar por precio sin que hubiese pulsado ninguno anteriormente
    @Test
    public void onOrdenarPrecioAscClickedInicial() {
        //Comportamiento de los mocks
        when(prefsMock.getInt(BarraHerramientasPresenter.ORDENAR)).thenReturn(0);
        sut.onOrdenarPrecioAscClicked();

        verify(prefsMock).putInt(BarraHerramientasPresenter.ORDENAR, 2);
        verify(viewMock).openMainView();
    }

    //Corresponde a UBHPR465236.1b
    //Se pulsa el boton de ordenar por precio tras pulsar el de ordenar por distancia
    @Test
    public void onOrdenarPrecioAscClickedDistanciaPrevia() {
        //Comportamiento de los mocks
        when(prefsMock.getInt(BarraHerramientasPresenter.ORDENAR)).thenReturn(1);
        sut.onOrdenarPrecioAscClicked();

        verify(prefsMock).putInt(BarraHerramientasPresenter.ORDENAR, 2);
        verify(viewMock).openMainView();
    }

    //Corresponde a UBHPR465236.1c
    //Se pulsa el boton de ordenar por precio tras haberlo pulsado previamente
    @Test
    public void onOrdenarPrecioAscClickedPrecioPrevio() {
        //Comportamiento de los mocks
        when(prefsMock.getInt(BarraHerramientasPresenter.ORDENAR)).thenReturn(2);
        sut.onOrdenarPrecioAscClicked();

        verify(prefsMock).putInt(BarraHerramientasPresenter.ORDENAR, 0);
        verify(viewMock).openMainView();
    }

    /**
     * Marcos UPR464971.3A
     */
    @Test
    public void onAnhadeConvenioClickedTest() {
        //Iniciamos la app
        sut.onAnhadeConvenioClicked();
        //verificamos que cambia las preferencias
        verify(prefsMock).putInt(ANHADIR,1);;
        //verificamos que abre la vista
        verify(viewMock).openConveniosView();


    }
}
