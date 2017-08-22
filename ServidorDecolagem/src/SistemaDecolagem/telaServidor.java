/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SistemaDecolagem;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

/**
 *
 * @author felipe
 */
public class telaServidor extends Application {

    private static Stage stage;

    /**
     * Método que carrega a tela de início do sistema e a mostra.
     *
     * @param stage
     *
     * @throws java.lang.Exception
     */
    @Override
    public void start(Stage stage) throws Exception {
        telaServidor.stage = stage;
        Parent parent = FXMLLoader.load(getClass().getResource("telaServidor.fxml"));
        Scene scene = new Scene(parent);
        stage.setScene(scene);
        stage.setTitle("Sistema Decolagem");
        stage.show();
    }

    /**
     * Método que retorna o stage da interface da tela de início do sistema.
     *
     * @return stage
     */
    public static Stage getStage() {
        return stage;
    }

    /**
     * Método principal, que inicializa a tela de início do sistema.
     *
     * @param args
     */
    public static void main(String[] args) throws FileNotFoundException, RemoteException, NotBoundException, MalformedURLException {
        
        
        
        ComunicacaoServidor.singleton();
        ComunicacaoServidor comunicacao = ComunicacaoServidor.getInstance();
        comunicacao.setNome(JOptionPane.showInputDialog(null, "Informe Empresa: "));
        Servidor servidor = new Servidor();
        servidor.carregarTrechos();
        System.out.println("Servidor on...");
        
        launch(args);
        
        try {
            servidor.carregar();
            servidor.executa();
            servidor.fechar();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    
        
    }
}
