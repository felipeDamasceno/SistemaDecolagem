package clientedecolagem;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Classe que inicializa a interface a tela inicial, apos o login do cliente.
 *
 * @author felipe
 *
 */
public class TelaInicial extends Application {

    private static Stage stage;

    /**
     *
     * Cria tela e a mostra.
     */
    @Override
    public void start(Stage stage) throws Exception {
        TelaInicial.stage = stage;
        Parent parent = FXMLLoader.
                load(getClass().getResource("TelaInicial.fxml"));
        Scene scene = new Scene(parent);
        stage.setScene(scene);
        stage.setTitle("Sistema Decolagem");
        stage.show();

    }

    /**
     * Retorna o Stage da interface.
     *
     * @return Stage
     */
    public static Stage getStage() {
        return stage;
    }

    /**
     * Inicializa a tela inicial.
     *
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }
}

