package es.unican.is.appgasolineras.repository.rest;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import es.unican.is.appgasolineras.common.Callback;
import es.unican.is.appgasolineras.model.GasolinerasResponse;
import es.unican.is.appgasolineras.model.IDCCAAs;
import retrofit2.Call;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Retrofit;


/**
 * Class to access the gas stations REST API using Retrofit
 * Usage: https://square.github.io/retrofit/
 */
public class GasolinerasService {

    public static final long TIMEOUT_SECONDS = 60L;

    private static GasolinerasAPI api;

    private static GasolinerasAPI getAPI() {
        if (api == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(GasolinerasServiceConstants.getAPIURL())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            api = retrofit.create(GasolinerasAPI.class);
        }
        return api;
    }

    /**
     * Download gas stations located in Cantabria from the REST API asynchronously
     * @param cb the callback that processes the response asynchronously
     */
    public static void requestGasolineras(Callback<GasolinerasResponse> cb) {
        final Call<GasolinerasResponse> call = getAPI().gasolineras(IDCCAAs.CANTABRIA.id);
        call.enqueue(new CallbackAdapter<>(cb));
    }

    /**
     * Download gas stations located in Cantabria from the REST API synchronously
     * @return the response object that contains the gasolineras located in Cantabria
     */
    public static GasolinerasResponse getGasolineras() {
        final Call<GasolinerasResponse> call = getAPI().gasolineras(IDCCAAs.CANTABRIA.id);


        ExecutorService executor = Executors.newFixedThreadPool(1);
        CallRunnable<GasolinerasResponse> runnable = new CallRunnable<>(call);
        executor.execute(runnable);

        // wait until background task finishes
        executor.shutdown();
        try {
            executor.awaitTermination(TIMEOUT_SECONDS, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // if there was some problem, response is null
        return runnable.getResponse();
    }

    private GasolinerasService() {
        throw new IllegalStateException();
    }
}
