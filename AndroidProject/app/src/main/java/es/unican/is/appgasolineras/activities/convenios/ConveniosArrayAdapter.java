package es.unican.is.appgasolineras.activities.convenios;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import es.unican.is.appgasolineras.R;
import es.unican.is.appgasolineras.model.Convenio;

public class ConveniosArrayAdapter extends ArrayAdapter<Convenio> {

    public ConveniosArrayAdapter(@NonNull Context context, @NonNull List<Convenio> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Convenio convenio = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.activity_convenios_item, parent, false);
        }

        // marca
        showInfo(convertView, R.id.tvMarcaConvenio, convenio.getMarca());

        // descuento
        showInfo(convertView, R.id.tvDescuentoConvenio, String.valueOf(convenio.getDescuento()));

        return convertView;
    }

    private void showInfo(View convertView, int p, String info) {
        TextView tv = convertView.findViewById(p);
        tv.setText(info);
    }
}
