package es.unican.is.appgasolineras.activities.toolbar;

import static es.unican.is.appgasolineras.activities.toolbar.BarraHerramientasPresenter.ORDENAR;

import android.content.Intent;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import es.unican.is.appgasolineras.R;
import es.unican.is.appgasolineras.activities.convenios.ConveniosView;
import es.unican.is.appgasolineras.activities.historialRepostajes.HistorialRepostajesView;
import es.unican.is.appgasolineras.activities.info.InfoView;
import es.unican.is.appgasolineras.activities.main.MainView;
import es.unican.is.appgasolineras.common.prefs.Prefs;

public class BarraHerramientasView extends AppCompatActivity implements IBarraHerramientasContract.View {
    //añadir el pref para guardar
    private IBarraHerramientasContract.Presenter presenter;
    private Toolbar toolbar;
    private AppCompatActivity activity;
    private Prefs prefs;
    Menu menu;

    public BarraHerramientasView(Toolbar toolbar, AppCompatActivity activity) {
        this.toolbar = toolbar;
        this.activity = activity;
        this.prefs = Prefs.from(activity);

        presenter = new BarraHerramientasPresenter(this,this.prefs);
        setUpToolBar();

    }

    /**
     * Establece una Toolbar como ActionBar de la activity.
     * Establece el logo de la aplicacion y le da la funcionalidad de volver a
     * la ventana principal al pulsarlo.
     */
    private void setUpToolBar() {
        activity.setSupportActionBar(toolbar);

        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayOptions(actionBar.getDisplayOptions()
                    | ActionBar.DISPLAY_SHOW_CUSTOM);
        }
        assert actionBar != null;
        ImageView imageView = new ImageView(actionBar.getThemedContext());
        imageView.setScaleType(ImageView.ScaleType.CENTER);
        imageView.setImageResource(R.drawable.letras_repost_app_230);
        ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.WRAP_CONTENT, Gravity.LEFT
                | Gravity.CENTER_VERTICAL);
        layoutParams.rightMargin = 40;
        imageView.setLayoutParams(layoutParams);
        actionBar.setCustomView(imageView);

        imageView.setOnClickListener(view -> presenter.onLogoClicked());
    }

    /**
     * Crear un menu en la activity (menu de tres puntos arriba a la izquierda).
     * @param menu
     * @return
     */
    public boolean onCreateOptionsMenu(Menu menu, boolean mostrarOrdenacion, boolean mostrarConvenio) {
        MenuInflater menuInflater = activity.getMenuInflater();
        this.menu = menu;
        if (mostrarOrdenacion) {
            menuInflater.inflate(R.menu.main_menu_order, menu);
            // poner iconos ordenacion correctamente
            int ordenacion = this.prefs.getInt(ORDENAR);
            switch (ordenacion) {
                case 1: // distancia
                    showOrdenarDistanciaSelected();
                    showOrdenarPrecioAscDeselected();
                    break;
                case 2: // precio
                    showOrdenarDistanciaDeselected();
                    showOrdenarPrecioAscSelected();
                    break;
                default: // sin
                    showOrdenarPrecioAscDeselected();
                    showOrdenarDistanciaDeselected();
                    break;
            }
        } else if (mostrarConvenio) {
            menuInflater.inflate(R.menu.main_menu_convenio, menu);
        } else {
            menuInflater.inflate(R.menu.main_menu, menu);
        }

        return true;
    }

    /**
     * Listener para el menu de tres puntos arriba a la izquierda.
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuInfo:
                presenter.onInfoClicked();
                return true;
            case R.id.menuConvenios:
                presenter.onConveniosClicked();
                return true;
            case R.id.menuHistorialRepostajes:
                presenter.onHistorialRepostajesClicked();
                return true;
            case R.id.menuDistancia:
                presenter.onOrdenarDistanciaClicked();
                return true;
            case R.id.menuPrecio:
                presenter.onOrdenarPrecioAscClicked();
                return true;
            case R.id.menuAnadeConvenio:
                presenter.onAnhadeConvenioClicked();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void openInfoView() {
        Intent intent = new Intent(activity, InfoView.class);
        activity.startActivity(intent);
    }

    @Override
    public void openConveniosView() {
        Intent intent = new Intent(activity, ConveniosView.class);
        activity.startActivity(intent);
    }

    @Override
    public void openHistorialRepostajeView() {
        Intent intent = new Intent(activity, HistorialRepostajesView.class);
        activity.startActivity(intent);
    }

    @Override
    public void openMainView() {
        Intent intent = new Intent(activity, MainView.class);
        activity.startActivity(intent);
    }

    @Override
    public AppCompatActivity getActivity() {
        return activity;
    }

    @Override
    public void showOrdenarDistanciaSelected() {
        menu.getItem(3).setIcon(activity.getDrawable(R.drawable.location_selected_32));
        menu.getItem(3).setTitle("DistanciaMarcada");

    }

    @Override
    public void showOrdenarPrecioAscSelected() {
        menu.getItem(4).setIcon(activity.getDrawable(R.drawable.low_price_selected_57));
        menu.getItem(4).setTitle("PrecioMarcado");
    }

    @Override
    public void showOrdenarDistanciaDeselected() {
        menu.getItem(3).setIcon(activity.getDrawable(R.drawable.location_32));
        menu.getItem(3).setTitle("DistanciaSinMarcar");
    }

    @Override
    public void showOrdenarPrecioAscDeselected() {
        menu.getItem(4).setIcon(activity.getDrawable(R.drawable.low_price_57));
        menu.getItem(4).setTitle("PrecioNoMarcado");
    }
}
