package clientedecolagem;

import java.net.URL;

import java.util.ResourceBundle;

import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;


/**
 * Classe que controla a interface da tela de início do sistema do cliente.
 *
 * @author Camille Jesus e Felipe Damasceno
 */
public class TelaInicialController implements Initializable {

    @FXML
    private TextField tfOrigem;

    @FXML
    private TextField tfDestino;

    @FXML
    private Button btPesquisar;

    @FXML
    private AnchorPane pane;

    /** Método que libera o evento para pesquisa de caminhos possíveis a partir da
     * origem e destino especificados.
     * 
     */
    @FXML
    private void caminhosPossiveis(ActionEvent event) throws Exception {
        String origem = tfOrigem.getText();
        String destino = tfDestino.getText();

        Conexao cliente = Conexao.getInstancia();
        if (cliente.conecta()) {
            cliente.envia("caminhos");
            cliente.envia(origem);
            cliente.envia(destino);
            cliente.desconecta();           
        }
    }   
    
    /** Método que prepara a janela.
     * 
     * @param url
     * @param bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle bundle) {

    }

}