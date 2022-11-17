package es.unican.is.appgasolineras.activities.historialRepostajes;


import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;

import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.not;

import static es.unican.is.appgasolineras.utils.Matchers.hasElements;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import es.unican.is.appgasolineras.R;
import es.unican.is.appgasolineras.activities.detail.GasolineraDetailView;
import es.unican.is.appgasolineras.activities.main.MainView;
import es.unican.is.appgasolineras.repository.rest.GasolinerasServiceConstants;


/**
 * Instrumented test, which will execute on an Android device.
 * See link if necessary.
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 * @author Ivan Ortiz del Noval
 */
@RunWith(AndroidJUnit4.class)
public class VerHistorialRepostajesVacioUITest {

    @Rule // se empieza desde la actividad Main
    public ActivityScenarioRule<MainView> activityRule = new
            ActivityScenarioRule<>(MainView.class);

    @BeforeClass
    public static void setUp() {
        // usar URL estatica para controlar el entorno
        GasolinerasServiceConstants.setStaticURL();
        MainView.inicializaTest();
    }

    /**
     Este test se corresponde con el test IGUI464877.g segun el plan de pruebas.

     Desde la vista main se va a la actividad de ver historial repostajes, se obtiene una
     lista vacia y se indica por pantalla. Si se pulsa el boton de atras, se vuelve a la
     actividad principal.
     */
    @Test
    public void verHistorialRepostajesVacioTest() {

        // abrir el menu de los 3 puntos desde Main, empieza el test en si
        openActionBarOverflowOrOptionsMenu(ApplicationProvider.getApplicationContext());
        // cuando se vea el desplegable
        onView(anyOf(withText("Historial Repostajes"),
                withId(R.id.menuHistorialRepostajes))).perform(click());

        // ver que se esta en la actividad nueva, con el texto de lista de repostajes vacio
        // si se ve este mensaje es que se esta en esa actividad
        onView(withId(R.id.tvRepostajesVacios)).check(matches(isDisplayed()));
        // la lista de gasolineras esta vacia
        onView(withId(R.id.lvHistoricoGasolineras)).check(matches(not(hasElements())));

        // volver a la vista principal con la accion de atras de Android
        pressBack();

        // comprobar que se esta en la pantalla principal y carga correctamente
        onData(anything()).inAdapterView(withId(R.id.lvGasolineras)).atPosition(0).
                onChildView(withId(R.id.tvName)).check(matches(withText("CEPSA")));
        onData(anything()).inAdapterView(withId(R.id.lvGasolineras)).atPosition(1).
                onChildView(withId(R.id.tvName)).check(matches(withText("REPSOL")));
    }

    @AfterClass
    public static void tearDown() {
        // volver URL real
        GasolinerasServiceConstants.setMinecoURL();
        MainView.acabaTest(ApplicationProvider.getApplicationContext());
    }
}
