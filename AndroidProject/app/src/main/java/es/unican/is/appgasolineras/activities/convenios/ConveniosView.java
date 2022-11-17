package es.unican.is.appgasolineras.activities.convenios;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import android.content.Intent;

import android.widget.ListView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import es.unican.is.appgasolineras.R;
import es.unican.is.appgasolineras.activities.toolbar.BarraHerramientasView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import es.unican.is.appgasolineras.activities.main.MainView;
import es.unican.is.appgasolineras.common.prefs.IPrefs;
import es.unican.is.appgasolineras.common.prefs.Prefs;
import es.unican.is.appgasolineras.model.Convenio;
import es.unican.is.appgasolineras.repository.db.GasolineraDatabase;

public class ConveniosView extends AppCompatActivity implements IConveniosContract.View {

    private IConveniosContract.Presenter presenter;

    private BarraHerramientasView barraHerramientasView;
    private SwipeRefreshLayout swipeRefreshLayout;

    private IPrefs prefs;

    private List<String> marcas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convenios_view);
        this.prefs = Prefs.from(this);

        // Toolbar
        barraHerramientasView = new BarraHerramientasView(findViewById(R.id.toolbar), this);

        presenter = new ConveniosPresenter(this, prefs);
        presenter.init();
        swipeRefreshLayout = findViewById(R.id.swiperefreshConvenio);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.init();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

    }

    @Override
    public void setMarcas(List<String> marcas) {
        this.marcas = marcas;
    }

    /*
    Metodos referentes a la toolbar
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return barraHerramientasView.onCreateOptionsMenu(menu, false, true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return barraHerramientasView.onOptionsItemSelected(item);
    }

    /*
    Metodos referentes a IConveniosContract.View
     */
    @Override
    public GasolineraDatabase getDatabase() {
        return GasolineraDatabase.getDB(this);
    }

    @Override
    public void showConvenios(List<Convenio> convenios) {
        ConveniosArrayAdapter adapter = new ConveniosArrayAdapter(this, convenios);
        ListView list = findViewById(R.id.lvConvenios);
        list.setAdapter(adapter);
    }

    @Override
    public void showConvenioAnhadido() {
        String text = getResources().getString(R.string.anhadirConvenioExito);
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showAnhadirConvenio() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //Se crea una ventana emergente customizada para el convenio
        View anhadirView = getLayoutInflater().inflate(R.layout.activity_convenios_anhadir, null);
        builder.setView(anhadirView);
        builder.setTitle(R.string.anhadirConvenioTitulo);
        builder.setPositiveButton(R.string.anhadir, (dialogInterface, i) ->  {
            //Lee los campos del usuario
            Spinner s = (Spinner) anhadirView.findViewById(R.id.spMarca);
            EditText e = (EditText) anhadirView.findViewById(R.id.etConvenioDescuento);

            String marca = s.getSelectedItem().toString();
            String descuento = e.getText().toString();

            presenter.onConvenioAnhadirClicked(descuento, marca);
        });
        builder.setNegativeButton(R.string.cancelar, (dialogInterface, i) -> presenter.onConvenioCancelarClicked());

        AlertDialog dialog = builder.create();
        dialog.show();

        Spinner s = (Spinner) anhadirView.findViewById(R.id.spMarca);
        cargaMarcas(s);
    }

    @Override
    public void showSobreescribirConvenio(Convenio c) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.anhadirConvenioSobreescribir);
        builder.setPositiveButton(R.string.sobreescribir, (dialogInterface, i) ->  presenter.onSiSobreescribirClicked(c));
        builder.setNegativeButton(R.string.cancelar, (dialogInterface, i) -> presenter.onNoSobreescribirClicked());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void showErrorDescuento() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.anhadirConvenioErrorDescuento);
        builder.setPositiveButton(R.string.aceptar, (dialogInterface, i) ->  presenter.onErrorDescuentoAceptarClicked());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void cargaMarcas(Spinner s) {
        var marcasArray = new ArrayList();
        marcasArray.addAll(marcas);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, marcasArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);
    }

    @Override
    public void showLoadError() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.conveniosFalloAccesoDatos);
        builder.setPositiveButton(R.string.aceptar, (dialogInterface, i) ->  presenter.onErrorAceptarClicked());
        builder.setNegativeButton(R.string.reintentar, (dialogInterface, i) -> presenter.onErrorReintentarClicked());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void showListaConveniosVacia() {
        TextView tv = findViewById(R.id.tvConveniosVacio);
        tv.setText(getResources().getString(R.string.conveniosListaVacia));
    }

    @Override
    public void openMainView() {
        Intent intent = new Intent(this, MainView.class);
        startActivity(intent);
    }

    @Override
    public void refresh() {
        recreate();
    }
}
