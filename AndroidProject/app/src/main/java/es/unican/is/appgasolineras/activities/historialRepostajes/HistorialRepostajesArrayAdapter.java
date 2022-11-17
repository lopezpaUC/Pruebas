package es.unican.is.appgasolineras.activities.historialRepostajes;

import static es.unican.is.appgasolineras.model.Gasolinera.DIVISA;

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
import es.unican.is.appgasolineras.model.Repostaje;

public class HistorialRepostajesArrayAdapter extends ArrayAdapter<Repostaje> {
    public HistorialRepostajesArrayAdapter(@NonNull Context context, @NonNull List<Repostaje> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Repostaje repostajes = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.activity_historial_repostajes_item, parent, false);
            //check
        }

        // localizacion
        showInfo(convertView, R.id.tvAdress, repostajes.getLocalizacion());

        // fecha repostaje
        showInfo(convertView, R.id.tvDate, repostajes.getFechaRepostaje());

        //Litros
        showInfo(convertView, R.id.tvLiters, repostajes.getLitros());

        // Precio
        showInfo(convertView, R.id.tvPrice, repostajes.getPrecio()+DIVISA);

        return convertView;
    }

    private void showInfo(View convertView, int p, String info) {
        TextView tv = convertView.findViewById(p);
        tv.setText(info);
    }


}
