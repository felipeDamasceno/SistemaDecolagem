package clientedecolagem;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;


public class TelaInicialController implements Initializable {

    @FXML
    private Text textDestino;
    @FXML
    private Text textOrigem;
    @FXML
    private Button buttonOk;
    @FXML
    private ListView<ArrayList<String>> listRotas;
    @FXML
    private Text textNome;
    @FXML
    private TextField fieldOrigem;
    @FXML
    private TextField fieldDestino;
    @FXML
    private TextArea areaCaminhos;
    @FXML
    private Text textRotas;
    @FXML
    private ComboBox<String> comboInicio;
    @FXML
    private Text textTrechos;
    @FXML
    private ComboBox<String> comboFim;
    private static Conexao conexao = Conexao.getInstancia();
    String recebe;
    
    /** Método que libera o evento para pesquisa de caminhos possíveis a partir da
     * origem e destino especificados.
     * 
     * @param event
     * 
     * @throws java.io.IOException
     */
    @FXML
    public void clicaOk(ActionEvent event) throws IOException {
        String origem = (this.fieldOrigem.getText());
        String destino = (this.fieldDestino.getText());
        System.out.println("evento ok");
        
        if (conexao.conecta()) {
            conexao.envia("caminhos");
            conexao.envia(origem);
            conexao.envia(destino);
            this.recebe = conexao.recebe().replace(",", "").replace("[", "").replace("]", "").replace(" ", "");
            System.out.println("Caminhos: " + recebe);
            conexao.desconecta();       
        }
        char[] caractere = this.recebe.toCharArray();
        int i = 0;
        ArrayList<String> caminho = new ArrayList<>();
        ArrayList<ArrayList<String>> caminhos = new ArrayList<>();
        
        while (i < caractere.length) {
            System.out.println(caractere[i]);
            if (caractere[i] != '@') {
                System.out.println(String.valueOf(caractere[i]));
                caminho.add(String.valueOf(caractere[i]));
            } else {
                System.out.println(String.valueOf(caminho));
                caminhos.add((ArrayList<String>) caminho.clone());
                System.out.println(caminhos);
                caminho.clear();
            }
            i++;
        }        
        this.listRotas.getItems().clear();
        this.listRotas.getItems().addAll(caminhos);
    }
    
    @FXML
    void clicaOrigem(ActionEvent event) {
        
        if (this.comboInicio.isFocusTraversable()) {
            this.comboFim.getItems().clear();
            
            if (conexao.conecta()) {
                conexao.envia("trechos");
                conexao.envia(this.comboInicio.getValue());
                this.recebe = conexao.recebe().replace(",", "").replace("[", "").replace("]", "").replace(" ", "");
                System.out.println(this.recebe);
                this.comboFim.getItems().addAll(recebe.split("@"));
            }
            
            try {       
                conexao.desconecta();
            } catch (IOException ex) {
                Logger.getLogger(TelaInicialController.class.getName()).log(Level.SEVERE, null, ex);
            }
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
        
        if (conexao.conecta()) {
            conexao.envia("cidades");
            this.recebe = conexao.recebe().replace("[", "").replace("]", "").replace(" ", "");
            System.out.println("Cidades: " + recebe);
            this.comboInicio.getItems().addAll(recebe.split(","));
            
            try {       
                conexao.desconecta();
            } catch (IOException ex) {
                Logger.getLogger(TelaInicialController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}