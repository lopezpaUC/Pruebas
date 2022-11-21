package es.unican.is.appgasolineras.activities.main;


import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static es.unican.is.appgasolineras.utils.Matchers.hasElements;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import es.unican.is.appgasolineras.R;
import es.unican.is.appgasolineras.activities.detail.GasolineraDetailView;
import es.unican.is.appgasolineras.repository.rest.GasolinerasServiceConstants;

public class MostrarGasolinerasUITest {

    @BeforeClass
    public static void setUp() {
        GasolinerasServiceConstants.setStaticURL();
        MainView.inicializaTest();
    }

    @AfterClass
    public static void clean() {
        GasolinerasServiceConstants.setMinecoURL();
        MainView.acabaTest(ApplicationProvider.getApplicationContext());

    }

    @Rule
    public ActivityScenarioRule<MainView> activityRule =
            new ActivityScenarioRule(MainView.class);

    @Test
    public void openDetailViewTest() {
        onView(withId(R.id.lvGasolineras)).check(matches(hasElements()));
    }

}
