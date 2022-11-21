package es.unican.is.appgasolineras.activities.convenios;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;

import static androidx.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.assertion.ViewAssertions.matches;

import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import es.unican.is.appgasolineras.R;
import es.unican.is.appgasolineras.activities.main.MainView;
import es.unican.is.appgasolineras.repository.db.GasolineraDatabase;
import es.unican.is.appgasolineras.repository.rest.GasolinerasServiceConstants;

/**
 * Test de interfaz grafica para el escenario "Añadir convenio".
 * @author Alina Solonaru
 */
@RunWith(AndroidJUnit4.class)
public class AnhadirConvenioUITest {

    // Se empieza desde la activity Main
    @Rule
    public ActivityScenarioRule<MainView> activityRule = new
            ActivityScenarioRule<>(MainView.class);

    @BeforeClass
    public static void setUp() {
        // Usar URL estatica para controlar el entorno
        GasolinerasServiceConstants.setStaticURL();
        // Situacion inicial: 0 convenios
        GasolineraDatabase.getDB(getApplicationContext(), true).convenioDao().deleteAll();
    }

    /**
     * Test IGUI464971.a
     * Alina Solonaru
     */
    @Test
    public void testAnhadirConvenioCorrecto() {
        // Abrir el menu de los 3 puntos desde Main
        openActionBarOverflowOrOptionsMenu(getApplicationContext());

        // Cuando se vea el desplegable
        onView(anyOf(withText("Convenios"), withId(R.id.menuConvenios))).perform(click());

        // Pulsar el boton "+" para añadir un convenio
        onView(withId(R.id.menuAnadeConvenio)).perform(click());

        // Comprobar que se muestra la ventana emergente de añadir convenio
        onView(withId(R.id.tvConvenioMarca)).check(matches(isDisplayed()));
        onView(withId(R.id.tvConvenioDescuento)).check(matches(isDisplayed()));

        // Introducir datos
        onView(withId(R.id.spMarca)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("CAMPSA"))).inRoot(isPlatformPopup()).perform(click());
        onView(withId(R.id.etConvenioDescuento)).perform(typeText("20"), closeSoftKeyboard());
        onView(withText(R.string.anhadir)).perform(click());

        // Comprobar que el convenio nuevo aparece en la lista de convenios
        onData(anything()).inAdapterView(withId(R.id.lvConvenios)).atPosition(0).
                onChildView(withId(R.id.tvMarcaConvenio)).check(matches(withText("CAMPSA")));
        onData(anything()).inAdapterView(withId(R.id.lvConvenios)).atPosition(0).
                onChildView(withId(R.id.tvDescuentoConvenio)).check(matches(withText("20")));

        // NOTA: no comprobamos el Toast porque puede hacer que falle la integracion por restricciones de tiempo

    }

    @AfterClass
    public static void tearDown() {
        // Volver URL real
        GasolinerasServiceConstants.setMinecoURL();
        // Borrar el convenio añadido
        GasolineraDatabase.getDB(getApplicationContext(), true).convenioDao().deleteAll();
    }
}
