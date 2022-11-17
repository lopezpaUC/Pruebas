package es.unican.is.appgasolineras.activities.toolbar;

import androidx.appcompat.app.AppCompatActivity;

/**
 * La Activity BarraHerramientas esta compuesta por un Presenter y una View, los cuales deben
 * implementar los metodos definidos en las siguientes interfaces.
 */
public interface IBarraHerramientasContract {

    public interface Presenter {
        /**
         * Este metodo debe ser usado por la View para notificar al Presenter que el boton
         * Info ha sido pulsado.
         */
        void onInfoClicked();

        /**
         * Este metodo debe ser usado por la View para notificar al Presenter que el boton
         * Convenios ha sido pulsado.
         */
        void onConveniosClicked();

        /**
         * Este metodo debe ser usado por la View para notificar al Presenter que el boton
         * Historial Repostajes ha sido pulsado.
         */
        void onHistorialRepostajesClicked();

        /**
         * Este metodo debe ser usado por la View para notificar al Presenter que el Logo
         * ha sido pulsado.
         */
        void onLogoClicked();

        /**
         * Este metodo debe ser usado por la View para notificar al Presenter que el icono
         * de ordenar gasolineras por distancia ha sido pulsado.
         */
        void onOrdenarDistanciaClicked();

        /**
         * Este metodo debe ser usado por la View para notificar al Presenter que el icono
         * de ordenar gasolineras por precio ha sido pulsado.
         */
        void onOrdenarPrecioAscClicked();

        /**
         * Este metodo debe ser usado por la View para notificar al Presenter que el icono
         * de crear convenio ha sido pulsado.
         */
        void onAnhadeConvenioClicked();
    }

    /**
     * Una vista para la Activity BarraHerramientas debe implementar esta funcionalidad.
     * Estos metodos estan destinados a ser utilizados por el Presenter.
     */
    public interface View {
        /**
         * Se solicita a la View que abra la vista Info.
         */
        void openInfoView();

        /**
         * Se solicia a la View que abra la vista Convenios.
         */
        void openConveniosView();

        /**
         * Se solicita a la View que abra la vista Historial Repostajes.
         */
        void openHistorialRepostajeView();

        /**
         * Se solicita a la View que abra la vista principal.
         */
        void openMainView();

        /**
         * Devuelve la Activity con la que la View esta ligada.
         * @return la Activity con la que la toolbar esta ligada.
         */
        AppCompatActivity getActivity();

        // estos metodos solo actualizan los logos, no hacen mas
        /**
         * Se solicita a la View que muestre un mensaje informativo de que la ordenacion por
         * distancia ha sido aplicada. Así como destacar su respectivo boton.
         */
        void showOrdenarDistanciaSelected();

        /**
         * Se solicita a la View que muestre un mensaje informativo de que la ordenacion por
         * precio ha sido aplicada. Así como destacar su respectivo boton.
         */
        void showOrdenarPrecioAscSelected();

        /**
         * Se solicita a la View que muestre el icono de ordenar por distancia deseleccionado.
         */
        void showOrdenarDistanciaDeselected();

        /**
         * Se solicita a la View que muestre el icono de ordenar por precio deseleccionado.
         */
        void showOrdenarPrecioAscDeselected();
    }
}
