package es.unican.is.appgasolineras.utils;

import android.view.View;
import android.widget.ListView;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class Matchers {

    /**
     * Metodo para comprobar en los test de interfaz si una lista tiene elementos. Para
     * utilizarlo se usa:
     *      onView(withId(R.id.id_de_la_lista)).check(matches(hasElements()))
     * @return Matcher<View>
     */
    public static Matcher<View> hasElements() {
        return new TypeSafeMatcher<View>() {
            @Override public boolean matchesSafely (final View view) {
                return ((ListView) view).getCount () > 0;
            }

            @Override public void describeTo (final Description description) {
                description.appendText ("ListView should not be empty");
            }
        };
    }

    /**
     * Metodo para comprobar en los test de interfaz si una lista tiene N elementos. Para
     * utilizarlo se usa:
     *      onView(withId(R.id.id_de_la_lista)).check(matches(hasNElements(int nElementos)))
     * @return Matcher<View>
     */
    public static Matcher<View> hasNElements(int nElementos) {
        return new TypeSafeMatcher<View>() {
            @Override public boolean matchesSafely (final View view) {
                return ((ListView) view).getCount () == nElementos;
            }

            @Override public void describeTo (final Description description) {
                description.appendText ("ListView should contain %d" + nElementos);
            }
        };
    }

}
