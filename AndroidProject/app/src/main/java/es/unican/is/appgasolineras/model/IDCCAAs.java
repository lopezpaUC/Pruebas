package es.unican.is.appgasolineras.model;

/**
 * Static collection of Comunidades Autonomas ID's, as used by the RESt API
 * Alternatively, these ID's can also be fetched from the REST API itself,
 * using this endpoint: https://sedeaplicaciones.minetur.gob.es/ServiciosRESTCarburantes/PreciosCarburantes/Listados/ComunidadesAutonomas/
 */
public enum IDCCAAs {

    CANTABRIA("06");

    public final String id;

    private IDCCAAs(String id) {
        this.id = id;
    }

}
