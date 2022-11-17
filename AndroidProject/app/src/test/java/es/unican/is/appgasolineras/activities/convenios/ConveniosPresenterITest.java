package es.unican.is.appgasolineras.activities.convenios;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static es.unican.is.appgasolineras.activities.toolbar.BarraHerramientasPresenter.ANHADIR;

import android.os.Build;

import androidx.test.core.app.ApplicationProvider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import es.unican.is.appgasolineras.common.prefs.IPrefs;
import es.unican.is.appgasolineras.common.prefs.Prefs;
import es.unican.is.appgasolineras.model.Convenio;
import es.unican.is.appgasolineras.repository.db.ConvenioDao;
import es.unican.is.appgasolineras.repository.db.GasolineraDatabase;
@RunWith(RobolectricTestRunner.class)
@Config(sdk = {Build.VERSION_CODES.O_MR1})
public class ConveniosPresenterITest {
    //Tests de integración para el presentador de los Convenios
    private ConveniosPresenter sut;
    private ConvenioDao dao;
    @Mock
    private IConveniosContract.View mockView;
    private List<Convenio> convenios_;
    private static GasolineraDatabase db;
    @Mock
    private IPrefs mockPrefs;
    @Before
    public void set(){
        MockitoAnnotations.openMocks(this);
        when(mockPrefs.getInt(ANHADIR)).thenReturn(0);
    }
    public void llenarDatos(){
        Convenio c1 = new Convenio();
        c1.setId(1);
        c1.setMarca("Campsa");
        c1.setDescuento(20);
        Convenio c2 = new Convenio();
        c2.setId(2);
        c2.setMarca("Galp");
        c2.setDescuento(5);
        Convenio c3 = new Convenio();
        c3.setId(3);
        c3.setMarca("Repsol");
        c3.setDescuento(-10);
        Convenio c4 = new Convenio();
        c4.setId(4);
        c4.setMarca("");
        convenios_.add(c1);
        convenios_.add(c2);
        convenios_.add(c3);
        convenios_.add(c4);
    }
    public boolean verifyEquals(List<Convenio> sut, List<Convenio> test){
        boolean ans = false;
        if(sut.size()==test.size()){
            int items = sut.size();
            int count=0;
            for(int i=0;i<items;i++){
                Convenio sutC = sut.get(i);
                Convenio testC = test.get(i);
                if( (sutC.getId()==testC.getId()) && (sutC.getMarca().equals(testC.getMarca())) &&  (sutC.getDescuento()==testC.getDescuento())){
                    count++;
                }
            }
            if (count==items){
                ans=true;
            }
        }
        return  ans;
    }
    @Test
    public void testInitCorrecto(){
        sut= new ConveniosPresenter(mockView, mockPrefs);
        db = GasolineraDatabase.getDB(ApplicationProvider.getApplicationContext());
        when(mockView.getDatabase()).thenReturn(GasolineraDatabase.getDB(ApplicationProvider.getApplicationContext(),true));
        dao = db.convenioDao();
        sut.insertaDatosTemp(dao);
        convenios_ = new ArrayList<Convenio>();
        llenarDatos();

        sut.init();
        assertTrue(verifyEquals(sut.getShownConvenios(),convenios_));
        verify(mockView).getDatabase();
        verify(mockView).showConvenios(sut.getShownConvenios());
    }
    @Test
    public void testInitVacio(){
        sut= new ConveniosPresenter(mockView, mockPrefs);
        db = GasolineraDatabase.getDB(ApplicationProvider.getApplicationContext());
        when(mockView.getDatabase()).thenReturn(GasolineraDatabase.getDB(ApplicationProvider.getApplicationContext(),true));
        sut.init();
        verify(mockView).showListaConveniosVacia();
        verify(mockView).getDatabase();
    }
}