package clientedecolagem;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;


public class TelaInicialController implements Initializable {

    @FXML
    private Text textDestino;
    @FXML
    private ComboBox<String> comboDestino;
    @FXML
    private Text textOrigem;
    @FXML
    private Button buttonOk;
    @FXML
    private ListView<ArrayList<String>> listRotas;
    @FXML
    private Text textNome;
    @FXML
    private ComboBox<String> comboOrigem;
    private static Conexao conexao = Conexao.getInstancia();
    
    @FXML
    public void getCidades(ActionEvent event) {
        
        if (conexao.conecta()) {
            conexao.envia("cidades");
            String cidade[] = (conexao.recebe().replace("[", "").replace("]", "").replace(",", "")).split(" ");               
            System.out.println(Arrays.toString(cidade));
            this.comboOrigem.getItems().addAll(Arrays.asList(cidade));
            
            try {       
                conexao.desconecta();
            } catch (IOException ex) {
                Logger.getLogger(TelaInicialController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /** Método que libera o evento para pesquisa de caminhos possíveis a partir da
     * origem e destino especificados.
     * 
     * @param event
     * 
     * @throws java.io.IOException
     */
    @FXML
    public void clicaOk(ActionEvent event) throws IOException {
        String origem = (this.textOrigem.getText());
        String destino = (this.textDestino.getText());
        
        if (conexao.conecta()) {
            conexao.envia("caminhos");
            conexao.envia(origem);
            conexao.envia(destino);
            conexao.desconecta();       
        }
    }
    
    /** Método que prepara a janela.
     * 
     * @param url
     * @param bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle bundle) {
        this.textNome.setText("Bem-vindo(a)!");
    }

}