package es.unican.is.appgasolineras.activities.convenios;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import static es.unican.is.appgasolineras.activities.toolbar.BarraHerramientasPresenter.ANHADIR;

import android.database.sqlite.SQLiteException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import es.unican.is.appgasolineras.common.prefs.IPrefs;
import es.unican.is.appgasolineras.model.Convenio;
import es.unican.is.appgasolineras.model.Gasolinera;
import es.unican.is.appgasolineras.repository.db.ConvenioDao;
import es.unican.is.appgasolineras.repository.db.GasolineraDao;
import es.unican.is.appgasolineras.repository.db.GasolineraDatabase;

/**
 * Test unitarios del presentador de convenios.
 *
 * @author Micaela Camila Patiño Hermosa y Alina Solonaru
 */
public class ConveniosPresenterTest {
    private ConveniosPresenter sut;
    private List<Convenio> convenios;
    private List<Gasolinera> gasolineras;

    @Mock
    private IConveniosContract.View mockConveniosView;
    @Mock
    private ConvenioDao mockConvenioDao;
    @Mock
    private GasolineraDatabase mockDb;
    @Mock
    private GasolineraDao mockGasolineraDao;
    @Mock
    private IPrefs mockPrefs;

    private void llenarDatos(Boolean anomalos){
        Convenio c1 = new Convenio();
        c1.setMarca("Campsa");
        c1.setDescuento(20);
        Convenio c2 = new Convenio();
        c2.setMarca("Galp");
        c2.setDescuento(5);
        convenios.add(c1);
        convenios.add(c2);
        if(anomalos){
            Convenio c3 = new Convenio();
            c3.setMarca("Repsol");
            c3.setDescuento(-10);
            convenios.add(c3);
            Convenio c4 = new Convenio();
            c4.setMarca("");
            convenios.add(c4);
        }
    }
    @Before
    public void setUp()  {

        // Inicializar mocks
        MockitoAnnotations.openMocks(this);
        when(mockPrefs.getInt(ANHADIR)).thenReturn(0);
        sut = new ConveniosPresenter(mockConveniosView, mockPrefs);
    }

    @Test
    public void testInitCorrecto(){
        sut = new ConveniosPresenter(mockConveniosView, mockPrefs);
        convenios = new ArrayList<Convenio>();
        llenarDatos(false);

        when(mockConveniosView.getDatabase()).thenReturn(mockDb);
        when(mockDb.convenioDao()).thenReturn(mockConvenioDao);
        when(mockConvenioDao.getAll()).thenReturn(convenios);

        sut.init();
        assert(sut.getShownConvenios().equals(convenios));
        verify(mockConveniosView).showConvenios(convenios);
        verify(mockConveniosView).getDatabase();
        verify(mockDb).convenioDao();
    }

    @Test
    public void testInitErrorCarga(){
        sut = new ConveniosPresenter(mockConveniosView, mockPrefs);

        when(mockConveniosView.getDatabase()).thenReturn(mockDb);
        when(mockDb.convenioDao()).thenReturn(mockConvenioDao);
        when(mockConvenioDao.getAll()).thenThrow(new SQLiteException());

        try{
            sut.init();
        }
        catch (SQLiteException exception){}
        assert(sut.getShownConvenios()==null);
        verify(mockConveniosView).showLoadError();
        verify(mockConveniosView).getDatabase();
        verify(mockDb).convenioDao();
    }

    @Test
    public void testInitVacio(){
        sut = new ConveniosPresenter(mockConveniosView, mockPrefs);
        convenios = new ArrayList<Convenio>();

        when(mockConveniosView.getDatabase()).thenReturn(mockDb);
        when(mockDb.convenioDao()).thenReturn(mockConvenioDao);
        when(mockConvenioDao.getAll()).thenReturn(convenios);

        sut.init();
        verify(mockConveniosView).showListaConveniosVacia();
        verify(mockConveniosView).getDatabase();
        verify(mockDb).convenioDao();
        verify(mockConvenioDao).getAll();
    }

    @Test
    public void testInitDatosAnomalos(){
        sut = new ConveniosPresenter(mockConveniosView, mockPrefs);
        convenios = new ArrayList<Convenio>();
        llenarDatos(true);

        when(mockConveniosView.getDatabase()).thenReturn(mockDb);
        when(mockDb.convenioDao()).thenReturn(mockConvenioDao);
        when(mockConvenioDao.getAll()).thenReturn(convenios);

        sut.init();
        assert(sut.getShownConvenios().equals(convenios));
        verify(mockConveniosView).showConvenios(convenios);
        verify(mockConveniosView).getDatabase();
        verify(mockDb).convenioDao();
    }

    @Test
    public void testOnErrorAceptarClicked(){
        sut = new ConveniosPresenter(mockConveniosView, mockPrefs);

        when(mockConveniosView.getDatabase()).thenReturn(mockDb);
        when(mockDb.convenioDao()).thenReturn(mockConvenioDao);
        when(mockConvenioDao.getAll()).thenReturn(null);

        sut.init();
        sut.onErrorAceptarClicked();
        verify(mockConveniosView).openMainView();
    }

    @Test
    public void onErrorReintentarClicked(){
        sut = new ConveniosPresenter(mockConveniosView, mockPrefs);

        when(mockConveniosView.getDatabase()).thenReturn(mockDb);
        when(mockDb.convenioDao()).thenReturn(mockConvenioDao);
        when(mockConvenioDao.getAll()).thenReturn(null);

        sut.init();
        sut.onErrorReintentarClicked();
        verify(mockConveniosView).refresh();
    }

    /**
     * Test UPR464971.1a
     * Alina Solonaru
     */
    @Test
    public void testInitErrorCargaMarcas() {
        sut = new ConveniosPresenter(mockConveniosView, mockPrefs);

        // Definir comportamiento correcto de convenios para que no lance NullPointerException
        convenios = new ArrayList<Convenio>();
        when(mockConveniosView.getDatabase()).thenReturn(mockDb);
        when(mockDb.convenioDao()).thenReturn(mockConvenioDao);
        when(mockConvenioDao.getAll()).thenReturn(convenios);

        // Definir comportamiento gasolineras, si hay error lanza excepcion
        when(mockPrefs.getInt(ANHADIR)).thenReturn(1);
        when(mockDb.gasolineraDao()).thenReturn(mockGasolineraDao);
        when(mockGasolineraDao.getAll()).thenThrow(new SQLiteException());

        // Comprobar funcionamiento
        sut.init(); // Recoge SQLiteException
        assert (sut.getGasolineras() == null);
        verify(mockConveniosView).getDatabase();
        verify(mockConveniosView).showLoadError();
    }

    /**
     * Test UPR464971.2a
     * Alina Solonaru
     */
    @Test
    public void testOnConvenioAnhadirClickedCorrecto() {
        sut = new ConveniosPresenter(mockConveniosView, mockPrefs);

        // Definir comportamiento correcto para el init
        convenios = new ArrayList<>();
        gasolineras = new ArrayList<>();
        when(mockConveniosView.getDatabase()).thenReturn(mockDb);
        when(mockDb.convenioDao()).thenReturn(mockConvenioDao);
        when(mockConvenioDao.getAll()).thenReturn(convenios);
        when(mockPrefs.getInt(ANHADIR)).thenReturn(1);
        when(mockDb.gasolineraDao()).thenReturn(mockGasolineraDao);
        when(mockGasolineraDao.getAll()).thenReturn(gasolineras);

        // Preparar entradas del test
        Convenio convenioAnhadir = new Convenio();
        convenioAnhadir.setMarca("Campsa");
        convenioAnhadir.setDescuento(20);
        sut.init();

        // Comprobar funcionamiento
        sut.onConvenioAnhadirClicked(String.valueOf(convenioAnhadir.getDescuento()),
                convenioAnhadir.getMarca());
        verify(mockConvenioDao).insertConvenio(convenioAnhadir);
        verify(mockConveniosView).showConvenioAnhadido();
    }

    /**
     * Test UPR464971.2b
     * Alina Solonaru
     */
    @Test
    public void testOnConvenioAnhadirClickedSobrescribir() {
        sut = new ConveniosPresenter(mockConveniosView, mockPrefs);

        // Definir comportamiento correcto para el init
        convenios = new ArrayList<>();
        gasolineras = new ArrayList<>();
        when(mockConveniosView.getDatabase()).thenReturn(mockDb);
        when(mockDb.convenioDao()).thenReturn(mockConvenioDao);
        when(mockConvenioDao.getAll()).thenReturn(convenios);
        when(mockPrefs.getInt(ANHADIR)).thenReturn(1);
        when(mockDb.gasolineraDao()).thenReturn(mockGasolineraDao);
        when(mockGasolineraDao.getAll()).thenReturn(gasolineras);

        // Preparar entradas del test y definir comportamiento mocks
        Convenio convenioAnterior = new Convenio();
        convenioAnterior.setMarca("Campsa");
        convenioAnterior.setDescuento(5);

        Convenio convenioAnhadir = new Convenio();
        convenioAnhadir.setMarca("Campsa");
        convenioAnhadir.setDescuento(20);

        when(mockConvenioDao.buscaConvenioPorMarca
                (convenioAnhadir.getMarca())).thenReturn(convenioAnterior);
        sut.init();

        // Comprobar funcionamiento
        sut.onConvenioAnhadirClicked(String.valueOf(convenioAnhadir.getDescuento()),
                convenioAnhadir.getMarca());
        verify(mockConvenioDao).buscaConvenioPorMarca(convenioAnhadir.getMarca());
        verify(mockConveniosView).showSobreescribirConvenio(convenioAnhadir);
    }

    /**
     * Test UPR464971.2c
     * Alina Solonaru
     */
    @Test
    public void testOnConvenioAnhadirClickedDescuentoNoNumerico() {
        sut = new ConveniosPresenter(mockConveniosView, mockPrefs);

        // Definir comportamiento correcto para el init
        convenios = new ArrayList<>();
        gasolineras = new ArrayList<>();
        when(mockConveniosView.getDatabase()).thenReturn(mockDb);
        when(mockDb.convenioDao()).thenReturn(mockConvenioDao);
        when(mockConvenioDao.getAll()).thenReturn(convenios);
        when(mockPrefs.getInt(ANHADIR)).thenReturn(1);
        when(mockDb.gasolineraDao()).thenReturn(mockGasolineraDao);
        when(mockGasolineraDao.getAll()).thenReturn(gasolineras);
        sut.init();

        // Comprobar funcionamiento
        sut.onConvenioAnhadirClicked("#34+.5", "Campsa"); // Recoge NumberFormatException
        verify(mockConveniosView).showErrorDescuento();
    }

    /**
     * Test UPR464971.2d
     * Alina Solonaru
     */
    @Test
    public void testOnConvenioAnhadirClickedDescuentoNegativo() {
        sut = new ConveniosPresenter(mockConveniosView, mockPrefs);

        // Definir comportamiento correcto para el init
        convenios = new ArrayList<>();
        gasolineras = new ArrayList<>();
        when(mockConveniosView.getDatabase()).thenReturn(mockDb);
        when(mockDb.convenioDao()).thenReturn(mockConvenioDao);
        when(mockConvenioDao.getAll()).thenReturn(convenios);
        when(mockPrefs.getInt(ANHADIR)).thenReturn(1);
        when(mockDb.gasolineraDao()).thenReturn(mockGasolineraDao);
        when(mockGasolineraDao.getAll()).thenReturn(gasolineras);
        sut.init();

        // Comprobar funcionamiento
        sut.onConvenioAnhadirClicked("-15", "Campsa");
        verify(mockConveniosView).showErrorDescuento();
    }

    /**
     * Test UPR464971.2e
     * Alina Solonaru
     */
    @Test
    public void testOnConvenioAnhadirClickedDescuentoCero() {
        sut = new ConveniosPresenter(mockConveniosView, mockPrefs);

        // Definir comportamiento correcto para el init
        convenios = new ArrayList<>();
        gasolineras = new ArrayList<>();
        when(mockConveniosView.getDatabase()).thenReturn(mockDb);
        when(mockDb.convenioDao()).thenReturn(mockConvenioDao);
        when(mockConvenioDao.getAll()).thenReturn(convenios);
        when(mockPrefs.getInt(ANHADIR)).thenReturn(1);
        when(mockDb.gasolineraDao()).thenReturn(mockGasolineraDao);
        when(mockGasolineraDao.getAll()).thenReturn(gasolineras);
        sut.init();

        // Comprobar funcionamiento
        sut.onConvenioAnhadirClicked("0", "Campsa");
        verify(mockConveniosView).showErrorDescuento();
    }

    /**
     * Test UPR464971.2f
     * Alina Solonaru
     */
    @Test
    public void testOnConvenioAnhadirClickedDescuentoCien() {
        sut = new ConveniosPresenter(mockConveniosView, mockPrefs);

        // Definir comportamiento correcto para el init
        convenios = new ArrayList<>();
        gasolineras = new ArrayList<>();
        when(mockConveniosView.getDatabase()).thenReturn(mockDb);
        when(mockDb.convenioDao()).thenReturn(mockConvenioDao);
        when(mockConvenioDao.getAll()).thenReturn(convenios);
        when(mockPrefs.getInt(ANHADIR)).thenReturn(1);
        when(mockDb.gasolineraDao()).thenReturn(mockGasolineraDao);
        when(mockGasolineraDao.getAll()).thenReturn(gasolineras);
        sut.init();

        // Comprobar funcionamiento
        sut.onConvenioAnhadirClicked("100", "Campsa");
        verify(mockConveniosView).showErrorDescuento();
    }

    /**
     * Test UPR464971.2g
     * Alina Solonaru
     */
    @Test
    public void testOnConvenioAnhadirClickedDescuentoMayor100() {
        sut = new ConveniosPresenter(mockConveniosView, mockPrefs);

        // Definir comportamiento correcto para el init
        convenios = new ArrayList<>();
        gasolineras = new ArrayList<>();
        when(mockConveniosView.getDatabase()).thenReturn(mockDb);
        when(mockDb.convenioDao()).thenReturn(mockConvenioDao);
        when(mockConvenioDao.getAll()).thenReturn(convenios);
        when(mockPrefs.getInt(ANHADIR)).thenReturn(1);
        when(mockDb.gasolineraDao()).thenReturn(mockGasolineraDao);
        when(mockGasolineraDao.getAll()).thenReturn(gasolineras);
        sut.init();

        // Comprobar funcionamiento
        sut.onConvenioAnhadirClicked("140", "Campsa");
        verify(mockConveniosView).showErrorDescuento();
    }

    /**
     * Test UPR464971.2h
     * Alina Solonaru
     */
    @Test
    public void testOnConvenioAnhadirClickedDescuentoVacio() {
        sut = new ConveniosPresenter(mockConveniosView, mockPrefs);

        // Definir comportamiento correcto para el init
        convenios = new ArrayList<>();
        gasolineras = new ArrayList<>();
        when(mockConveniosView.getDatabase()).thenReturn(mockDb);
        when(mockDb.convenioDao()).thenReturn(mockConvenioDao);
        when(mockConvenioDao.getAll()).thenReturn(convenios);
        when(mockPrefs.getInt(ANHADIR)).thenReturn(1);
        when(mockDb.gasolineraDao()).thenReturn(mockGasolineraDao);
        when(mockGasolineraDao.getAll()).thenReturn(gasolineras);
        sut.init();

        // Comprobar funcionamiento
        sut.onConvenioAnhadirClicked("", "Campsa");
        verify(mockConveniosView).showErrorDescuento();
    }
    /**
     * Test UPR464971.1A
     * Marcos Fernadnez Alonso
     */
    @Test
    public void onSiSobrescribirClickedTest(){
        convenios = new ArrayList<Convenio>();
        llenarDatos(false);
        //Iniciamos el convenio que vamos a modificar
        Convenio cFinal = new Convenio();
        cFinal.setMarca("Campsa");
        cFinal.setDescuento(80);
        //programamos el comportamiento de los mocks
        when(mockConveniosView.getDatabase()).thenReturn(mockDb);
        when(mockDb.convenioDao()).thenReturn(mockConvenioDao);
        when(mockConvenioDao.getAll()).thenReturn(convenios);
        when(mockConvenioDao.buscaConvenioPorMarca(cFinal.getMarca())).thenReturn(convenios.get(0));


        sut.init();
        sut.onSiSobreescribirClicked(cFinal);
        //Nos aseguramos de que llama a los metodos adecaudos
        verify(mockConvenioDao).updateConvenio(cFinal);
        verify(mockConveniosView).refresh();
        verify(mockConveniosView).showConvenioAnhadido();

    }
    /**
     * Test UPR464971.2A
     * Marcos Fernadnez Alonso
     */
    @Test
    public void onNoSobrescribirClickedTest(){
        convenios = new ArrayList<Convenio>();
        llenarDatos(false);

        when(mockConveniosView.getDatabase()).thenReturn(mockDb);
        when(mockDb.convenioDao()).thenReturn(mockConvenioDao);
        when(mockConvenioDao.getAll()).thenReturn(convenios);
        sut.init();
        sut.onNoSobreescribirClicked();
        //No hay nada que llamar a nada
        verify(mockConveniosView,times(0)).refresh();
        verify(mockConveniosView,times(0)).showConvenioAnhadido();
    }
}