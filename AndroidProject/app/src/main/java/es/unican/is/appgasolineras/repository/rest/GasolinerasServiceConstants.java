package es.unican.is.appgasolineras.repository.rest;

public class GasolinerasServiceConstants {

    private static final String MINECO_API_URL =
            "https://sedeaplicaciones.minetur.gob.es/ServiciosRESTCarburantes/PreciosCarburantes/";

    // para pruebas cambiar master por develop o feature
    private static final String STATIC_API_URL =
            "https://raw.githubusercontent.com/isunican/App-Gasolineras-Grupo3/master/StaticREST/ServiciosRESTCarburantes/PrecioCarburantes/";

    private static String apiUrl = MINECO_API_URL;

    public static void setStaticURL() {
        apiUrl = STATIC_API_URL;
    }

    public static void setMinecoURL() {
        apiUrl = MINECO_API_URL;
    }

    public static final String getAPIURL() {
        return apiUrl;
    }

    private GasolinerasServiceConstants() {
        throw new IllegalStateException();
    }
}
