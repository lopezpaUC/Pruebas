package es.unican.is.appgasolineras.activities.main;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
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
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import es.unican.is.appgasolineras.R;
import es.unican.is.appgasolineras.activities.detail.GasolineraDetailView;
import es.unican.is.appgasolineras.repository.rest.GasolinerasServiceConstants;

/**
 *
 * En este test se comprueba que tras clickar el botón de ordenar por
 * distancia, las gasolineras que se muestren esten en efecto ordenadas por distancia.
 *
 * @author Alberto Moro
 */
public class OrdenarGasolinerasPorDistanciaUITest {
    @Rule
    public ActivityScenarioRule<MainView> activityRule = new
            ActivityScenarioRule<>(MainView.class);

    private View vista;

    @BeforeClass
    public static void setUp() {
        GasolinerasServiceConstants.setStaticURL();
        MainView.inicializaTest();
    }

    @Before
    public void setUp2() {
        activityRule.getScenario().onActivity(activity -> vista = activity.getWindow().getDecorView());
        MainView.inicializaTest();
    }

    /**
     * Test de interfaz de interfaz identificado como IGUI465235.a.
     */
    @Test
    public void ordenarGasolinerasPorDistanciaTest() {

        onView(withId(R.id.menuDistancia)).perform(click());

        //Comprobamos tanto la localización como el nombre de las gasolineras para que sean
        //uniequivocas

        onData(anything()).inAdapterView(withId(R.id.lvGasolineras)).atPosition(0).
                onChildView(withId(R.id.tvName)).check(matches(withText("CEPSA")));
        onData(anything()).inAdapterView(withId(R.id.lvGasolineras)).atPosition(1).
                onChildView(withId(R.id.tvName)).check(matches(withText("CEPSA")));
        onData(anything()).inAdapterView(withId(R.id.lvGasolineras)).atPosition(2).
                onChildView(withId(R.id.tvName)).check(matches(withText("EASYGAS")));
        onData(anything()).inAdapterView(withId(R.id.lvGasolineras)).atPosition(3).
                onChildView(withId(R.id.tvName)).check(matches(withText("BP CASTRO URDIALES")));
        onData(anything()).inAdapterView(withId(R.id.lvGasolineras)).atPosition(0).
                onChildView(withId(R.id.tvAddress)).check(matches(withText("CARRETERA N-634 KM. 136,400")));
        onData(anything()).inAdapterView(withId(R.id.lvGasolineras)).atPosition(1).
                onChildView(withId(R.id.tvAddress)).check(matches(withText("CARRETERA N-634 KM. 136,4")));
        onData(anything()).inAdapterView(withId(R.id.lvGasolineras)).atPosition(2).
                onChildView(withId(R.id.tvAddress)).check(matches(withText("AUTV.A-8 (AUTOV. CANTABRICO) km 141")));
        onData(anything()).inAdapterView(withId(R.id.lvGasolineras)).atPosition(3).
                onChildView(withId(R.id.tvAddress)).check(matches(withText("BARRIO BRAZOMAR S/N, S/N")));
    }

    @AfterClass
    public static void clean() {
        GasolinerasServiceConstants.setMinecoURL();
        MainView.acabaTest(ApplicationProvider.getApplicationContext());
    }
}
