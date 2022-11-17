package es.unican.is.appgasolineras.activities.historialRepostajes;



import java.util.List;


import es.unican.is.appgasolineras.model.Repostaje;
import es.unican.is.appgasolineras.repository.db.GasolineraDatabase;


public interface IHistorialRepostajesContract {

    public interface Presenter {
        /**
         * Metodo de inicializacion. Obtiene los repostajes de la DAO y los muestra.
         * Si hay un fallo en el acceso a datos se muestra una ventana emergente informando
         * del error, y ofreciendo la opcion de reintentar o volver a la vista principal.
         * Si la lista de convenios esta vacia se muestra un fondo vacio (en blanco) con un
         * texto indicandolo.
         */
        void init();

        /**
         * Este metodo debe ser usado por la View para notificar al Presenter que el boton
         * Aceptar de la ventana emergente que notfica el error de acceso a datos ha
         * sido pulsado.
         */
        void onAceptarClicked();

         /**
         * Este metodo debe ser usado por la View para notificar al Presenter que el boton
         * Reintentar de la ventana emergente que notfica el error de acceso a datos ha
         * sido pulsado.
         */
        void onReintentarClicked();

    }

    public interface View {

        /**
         * The View is requested to open the main view
         */
        void openMainView();

        /**
         * Se solicita a la View que muestre una ventana emergente informando de que ha ocurrido
         * un error al acceder a los datos y ofreciendo la opcion de reintentar o volver a la
         * vista principal.
         */
        void showLoadError();

        /**
         * Devuelve la base de datos con su contexto.
         * @return el objeto GasolineraDatabase para acceder a la base de datos.
         */
        GasolineraDatabase getGasolineraDb();

        /**
         * Esta vista muestra los repostajes
         *
         * @param historialRepostajes la lista de repostajes
         */
        void showHistorialRepostajes(List<Repostaje> historialRepostajes);

        /**
         * Muestra un mensaje que avisa al usuario de que no tiene repostajes registrados
         */
        void showHistorialVacio();

        /**
         * Se solicita a la View que vuelva a crear la vista.
         */
        void refresh();

    }
}
