package clientedecolagem;

import java.io.IOException;

import java.net.UnknownHostException;

import javafx.application.Application;

import javafx.fxml.FXMLLoader;

import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.stage.Stage;

import javax.swing.JOptionPane;


/**
 * Classe que inicia a interface da tela de login do cliente.
 *
 * @author Camille Jesus e Felipe Damasceno
 */
public class TelaLogin extends Application {

    private static Stage stage;

    /** Método que carrega a tela de login e a mostra.
     * 
     * @param stage
     * 
     * @throws java.lang.Exception
     */
    @Override
    public void start(Stage stage) throws Exception {
        TelaLogin.stage = stage;
        Parent parent = FXMLLoader.load(getClass().getResource("TelaLogin.fxml"));
        Scene scene = new Scene(parent);
        stage.setScene(scene);
        stage.setTitle("Sistema Decolagem");
        stage.show();

    }

    /** Método que retorna o stage da interface da tela de login.
     *
     * @return stage
     */
    public static Stage getStage() {
        return stage;
    }

    /** Método principal, que inicializa a tela de login, inicia a conexão, recebe
     * o Ip do servidor desejado e seu nome, e estabelece a conexão.
     *
     * @param args
     * 
     * @throws java.net.UnknownHostException
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws UnknownHostException, IOException {
        Conexao.singleton();        
        String ip = JOptionPane.showInputDialog("Informe Ip do Servidor");
        String nome = JOptionPane.showInputDialog("Informe Empresa");
        
        if ((!ip.equals("") && !ip.equals(" ")) && !nome.equals("")  && !nome.equals(" ")) {
            Conexao.setIp(ip);
            Conexao.setNome(nome);
            launch(args);
        }
    }
    
}