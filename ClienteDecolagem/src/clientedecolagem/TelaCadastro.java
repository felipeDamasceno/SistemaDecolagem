package clientedecolagem;

import javafx.application.Application;

import javafx.fxml.FXMLLoader;

import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.stage.Stage;


/**
 * Classe que inicia a interface da tela de cadastro do cliente.
 *
 * @author Camille Jesus e Felipe Damasceno
 */
public class TelaCadastro extends Application {

    private static Stage stage;

    /** Método que carrega a tela de cadastro e a mostra.
     * 
     * @param stage
     * 
     * @throws java.lang.Exception
     */
    @Override
    public void start(Stage stage) throws Exception {
        TelaCadastro.stage = stage;
        Parent parent = FXMLLoader.load(getClass().getResource("TelaCadastro.fxml"));
        stage.setScene(new Scene(parent));
        stage.setTitle("Sistema Decolagem");
        stage.show();
    }

    /** Método que retorna o stage da interface da tela de cadastro.
     *
     * @return stage
     */
    public static Stage getStage() {
        return stage;
    }

    /** Método principal, que inicializa a tela de cadastro.
     *
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}