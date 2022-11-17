package es.unican.is.appgasolineras.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Model that represents the response obtained from the Gasolineras REST API:
 * https://sedeaplicaciones.minetur.gob.es/ServiciosRESTCarburantes/PreciosCarburantes/help/operations/PreciosEESSTerrestres#response-json
 *
 * The API returns an object with these 4 properties: "date", "note", "stations", "query".
 * The actual list of gas stations is the property "stations".
 *
 * The #SerializedName annotation is a GSON annotation that defines the name of the property
 * in the json file, as obtained from the REST API.
 */
public class GasolinerasResponse {

    @SerializedName(value="Fecha")              private String date;
    @SerializedName(value="Nota")               private String note;
    @SerializedName(value="ListaEESSPrecio")    private List<Gasolinera> stations;
    @SerializedName(value="ResultadoConsulta")  private String query;

    public List<Gasolinera> getStations() {
        return stations;
    }

    public String getDate() {
        return date;
    }

    public String getNote() {
        return note;
    }

    public String getQuery() {
        return query;
    }
}
