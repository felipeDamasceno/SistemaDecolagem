/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SistemaDecolagem;


import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

/**
 *
 * @author felipe
 */
public class telaServidorController implements Initializable {

    @FXML
    private Button conectar;
    
    @FXML
    private Label label;
    
    @FXML
    private Pane pane;
    
    @FXML
    private void conectar(ActionEvent event) throws NotBoundException, RemoteException, MalformedURLException {
        ComunicacaoServidor comunicacao = ComunicacaoServidor.getInstance();
        comunicacao.conectar();
       
        JOptionPane.showMessageDialog(null, "Conectado com sucesso");
        
    }
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        label.setText("Servidor " + ComunicacaoServidor.getNome());
    }
    
    
}
