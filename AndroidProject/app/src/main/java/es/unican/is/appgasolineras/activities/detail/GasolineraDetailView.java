package es.unican.is.appgasolineras.activities.detail;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import es.unican.is.appgasolineras.R;
import es.unican.is.appgasolineras.common.prefs.IPrefs;
import es.unican.is.appgasolineras.common.prefs.Prefs;
import es.unican.is.appgasolineras.model.Gasolinera;

public class GasolineraDetailView extends AppCompatActivity {

    public static final String INTENT_GASOLINERA = "INTENT_GASOLINERA";
    IPrefs prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gasolinera_detail_view);

        // Link to view elements
        ImageView ivRotulo = findViewById(R.id.ivRotulo);
        TextView tvRotulo = findViewById(R.id.tvRotulo);
        TextView tvMunicipio = findViewById(R.id.tvMunicipio);
        TextView tvHorario = findViewById(R.id.tvHorario);
        TextView tvPrecioDieselA = findViewById(R.id.tvPrecioDieselA);
        TextView tvPrecioGasolina95 = findViewById(R.id.tvPrecioGasolina95);
        TextView tvPrecioSumario =findViewById(R.id.tvPrecioSumario);
        TextView tvDistancia = findViewById(R.id.tvDistancia);

        // Get Gas Station from the intent that triggered this activity
        Gasolinera gasolinera = getIntent().getExtras().getParcelable(INTENT_GASOLINERA);
        //Create item from class gasolineraDetailPresenter in order to get the added price
        prefs = new Prefs(this);
        GasolineraDetailPresenter gs = new GasolineraDetailPresenter(gasolinera, prefs);

        // Set logo
        ivRotulo.setImageResource(gs.getLogoId(this));

        // Set Texts
        tvRotulo.setText(gs.getRotulo());
        tvMunicipio.setText(gs.getMunicipio());
        tvHorario.setText(gs.getSchedule());
        tvPrecioDieselA.setText(gs.getDieselA());
        tvPrecioGasolina95.setText(gs.getNormal95());
        tvPrecioSumario.setText(gs.getPrecioSumario());
        tvDistancia.setText(gs.getDistancia());

    }

}