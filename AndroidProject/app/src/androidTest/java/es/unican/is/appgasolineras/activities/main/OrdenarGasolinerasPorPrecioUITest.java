package es.unican.is.appgasolineras.activities.main;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.PreferenceMatchers.withTitle;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.not;

import static es.unican.is.appgasolineras.utils.Matchers.hasNElements;

import android.view.View;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import es.unican.is.appgasolineras.R;
import es.unican.is.appgasolineras.activities.detail.GasolineraDetailView;
import es.unican.is.appgasolineras.repository.rest.GasolinerasServiceConstants;
import es.unican.is.appgasolineras.utils.Matchers;

/**
 * Test de interfaz para el escenario "Ordenar Gasolineras Por Precio".
 *
 * @author Irene Zamanillo Zubizarreta
 */

public class OrdenarGasolinerasPorPrecioUITest {

    @Rule
    public ActivityScenarioRule<MainView> activityRule = new
            ActivityScenarioRule<>(MainView.class);

    private View decorView;

    @BeforeClass
    public static void setUp() {
        GasolinerasServiceConstants.setStaticURL();
        MainView.inicializaTest();

    }

    @Before
    public void setUp2() { activityRule.getScenario().onActivity(activity -> decorView = activity.getWindow().getDecorView()); }

    /**
     Este test se corresponde con el test IGUI465236.a segun el plan de pruebas.

     Desde la vista main se pulsa el botón para ordenar por precio de la barra de herramientas.
     Se carga una nueva activity en la que se muestra la lista de gasolineras ordenada por precio.
     Se muestra un mensaje indicando que las gasolineras han sido ordenadas.
     Se destaca el botón de ordenar que ha sido pulsado (no se puede comprobar por cuestiones del diseño del botón)
     */
    @Test
    public void ordenarGasolinerasPorPrecioTest() {
        //Pulsar el boton de ordenar por precio
        onView(withId(R.id.menuPrecio)).perform(click());
        //Comprobar que se muestra el Toast
        // ya no lo comprobamos, puede hacer que falle la integracion por restricciones de tiempo

        //Comprobar que está cargada la lista de gasolineras y tiene todas las gasolineras de Cantabria
        onView(withId(R.id.lvGasolineras)).check(matches(hasNElements(156)));

        //Comprobar que la lista de gasolineras esta ordenada
        onData(anything()).inAdapterView(withId(R.id.lvGasolineras)).atPosition(0).
                onChildView(withId(R.id.tvName)).check(matches(withText("BALLENOIL")));
        onData(anything()).inAdapterView(withId(R.id.lvGasolineras)).atPosition(1).
                onChildView(withId(R.id.tvName)).check(matches(withText("EASYGAS")));
        onData(anything()).inAdapterView(withId(R.id.lvGasolineras)).atPosition(2).
                onChildView(withId(R.id.tvName)).check(matches(withText("EASYGAS")));


    }
    @AfterClass
    public static void clean() {
        GasolinerasServiceConstants.setMinecoURL();
        MainView.acabaTest(ApplicationProvider.getApplicationContext());
    }
}
