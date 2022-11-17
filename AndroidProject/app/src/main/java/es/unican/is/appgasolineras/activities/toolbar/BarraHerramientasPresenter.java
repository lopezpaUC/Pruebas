package es.unican.is.appgasolineras.activities.toolbar;

import es.unican.is.appgasolineras.common.prefs.IPrefs;

public class BarraHerramientasPresenter implements IBarraHerramientasContract.Presenter {
    private final IBarraHerramientasContract.View view;
    private final IPrefs prefs;
    public static final  String ORDENAR = "Ordenar";
    public static final String ANHADIR = "Añadir convenio";
    public BarraHerramientasPresenter(IBarraHerramientasContract.View view, final IPrefs  prefs ) {
        this.view = view;
        this.prefs = prefs;
    }

    @Override
    public void onInfoClicked() {
        view.openInfoView();
    }

    @Override
    public void onConveniosClicked() {
        view.openConveniosView();
    }

    @Override
    public void onHistorialRepostajesClicked() {
        view.openHistorialRepostajeView();
    }

    @Override
    public void onLogoClicked() {
        view.openMainView();
        this.prefs.putInt(ORDENAR,0);
    }


    @Override
    public void onOrdenarDistanciaClicked() {
        if (this.prefs.getInt(ORDENAR) != 1){ // actualmente no ordena por distancia
            this.prefs.putInt(ORDENAR, 1);
        }
        else {
            if (this.prefs.getInt(ORDENAR) == 1){ // desmarcar, que no ordene
                this.prefs.putInt(ORDENAR, 0);
            }
        }
        view.openMainView();
    }

    @Override
    public void onOrdenarPrecioAscClicked() {
        if (this.prefs.getInt(ORDENAR) != 2) { // actualmente no ordena por precio
            this.prefs.putInt(ORDENAR,2);
            view.openMainView();
        }
        else{
            if(this.prefs.getInt(ORDENAR)==2){ // desmarcar, que no ordene
                this.prefs.putInt(ORDENAR,0);
                view.openMainView();
            }
        }
    }

    @Override
    public void onAnhadeConvenioClicked() {
        this.prefs.putInt(ANHADIR,1);
        view.openConveniosView();
    }
}
