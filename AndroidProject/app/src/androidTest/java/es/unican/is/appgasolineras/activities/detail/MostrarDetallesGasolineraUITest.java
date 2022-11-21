package es.unican.is.appgasolineras.activities.detail;


import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.Espresso.pressBackUnconditionally;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.anyOf;
import static es.unican.is.appgasolineras.utils.Matchers.hasElements;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.util.Log;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.regex.Matcher;

import es.unican.is.appgasolineras.R;
import es.unican.is.appgasolineras.activities.detail.GasolineraDetailView;
import es.unican.is.appgasolineras.activities.main.MainView;
import es.unican.is.appgasolineras.common.prefs.Prefs;
import es.unican.is.appgasolineras.repository.rest.GasolinerasServiceConstants;

@RunWith(AndroidJUnit4.class)
public class MostrarDetallesGasolineraUITest {

    //Pruebas de interfaz realizadas para la muestra de convenios considerando que la base de datos está vacía
    @Rule
    public ActivityScenarioRule<MainView> activityRule =
            new ActivityScenarioRule(MainView.class);

    @BeforeClass
    public static void setUp() {

        GasolinerasServiceConstants.setStaticURL();
        MainView.inicializaTest();
    }

    @AfterClass
    public static void clean() {
        GasolinerasServiceConstants.setMinecoURL();
        MainView.acabaTest( getApplicationContext());
    }

    /**
     * Marcos Fernandez Alonso IGUI464705.1A
     */
    @Test
    public void testMuestraDetalle()  {

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Log.d("sleep", "testMuestraDetalle: 1 ");
        }
        onData(anything()).inAdapterView(withId(R.id.lvGasolineras)).atPosition(0).perform(click());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Log.d("sleep", "testMuestraDetalle: 2 ");
        }

        onView(withId(R.id.tvDistancia)).check(matches(withText("7072,50 km")));
        onView(withId(R.id.tvPrecioDieselA)).check(matches(withText("1,99 €")));
        onView(withId(R.id.tvPrecioGasolina95)).check(matches(withText("1,85 €")));
        onView(withId(R.id.tvPrecioSumario)).check(matches(withText("1,90 €")));
        onView(withId(R.id.tvHorario)).check(matches(withText("L-D: 08:00-21:00")));
        onView(withId(R.id.tvRotulo)).check(matches(withText("CEPSA")));
        onView(withId(R.id.tvMunicipio)).check(matches(withText("Alfoz de Lloredo")));
        onView(withId(R.id.tvtTxtHorario)).check(matches(withText("Horario")));
        onView(withId(R.id.tvTxtDieselA)).check(matches(withText("Diésel Normal")));
        onView(withId(R.id.tvTxtPrecioGasolina95)).check(matches(withText("Gasolina 95")));
        onView(withId(R.id.tvTxtDistancia)).check(matches(withText("Distancia:")));
        pressBack();
        onView(withId(R.id.lvGasolineras)).check(matches(hasElements()));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Log.d("sleep", "testMuestraDetalle: 3 ");
        }


    }

}


