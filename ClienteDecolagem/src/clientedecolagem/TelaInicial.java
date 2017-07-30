package clientedecolagem;

import javafx.application.Application;
import static javafx.application.Application.launch;

import javafx.fxml.FXMLLoader;

import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.stage.Stage;


/**
 * Classe que inicia a interface da tela inicial do sistema do cliente.
 *
 * @author Camille Jesus e Felipe Damasceno
 */
public class TelaInicial extends Application {

    private static Stage stage;

    /** Método que carrega a tela de início do sistema e a mostra.
     * 
     * @param stage
     * 
     * @throws java.lang.Exception
     */
    @Override
    public void start(Stage stage) throws Exception {
        TelaInicial.stage = stage;
        Parent parent = FXMLLoader.load(getClass().getResource("TelaInicial.fxml"));
        Scene scene = new Scene(parent);
        stage.setScene(scene);
        stage.setTitle("Sistema Decolagem");
        stage.show();
    }

    /** Método que retorna o stage da interface da tela de início do sistema.
     *
     * @return stage
     */
    public static Stage getStage() {
        return stage;
    }

    /** Método principal, que inicializa a tela de início do sistema.
     *
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}