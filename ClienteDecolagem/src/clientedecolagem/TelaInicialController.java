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
import javax.swing.JOptionPane;


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
    private Text textTrecho;
    @FXML
    private ComboBox<String> comboFim;
    @FXML
    private Text textInicio;
    @FXML
    private Text textFim;
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
    
    @FXML
    public void clicaComprar(ActionEvent event) throws IOException {
        String inicio = this.comboInicio.getValue();
        String comboF = this.comboFim.getValue();
        String fim = String.valueOf(comboF.charAt(0));
        String servidor = String.valueOf(comboF.charAt(comboF.length() - 1));
                
        if (conexao.conecta()) {
            conexao.envia("compra");
            System.out.println(Conexao.getCliente());
            conexao.envia(Conexao.getCliente());
            System.out.println(inicio);
            conexao.envia(inicio);
            System.out.println(fim);
            conexao.envia(fim);
            System.out.println(servidor);
            conexao.envia(servidor);
            this.recebe = conexao.recebe();
            System.out.println(this.recebe);
            
            if (this.recebe.equals("1")) {
                this.textTrecho.setText(this.textTrecho.getText().substring(0, this.textTrecho.getText().length() - 1));
                this.textTrecho.setText(this.textTrecho.getText() + inicio + "->" + fim);
                this.comboInicio.getItems().clear();
                this.comboInicio.getItems().add(fim);
                this.comboFim.getItems().clear();
                JOptionPane.showMessageDialog(null, "Compra realizada!", "Confirmação", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Compra indisponível!", "Erro!", JOptionPane.ERROR_MESSAGE);
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
        this.textNome.setText("Bem-vindo(a), " + Conexao.getCliente() + "!");
        this.textTrecho.setText("Trecho:  ");
        
        if (conexao.conecta()) {
            conexao.envia("reserva");
            conexao.envia(Conexao.getCliente());
            this.recebe = conexao.recebe();
            System.out.println("Reserva: " + this.recebe);
            this.textTrecho.setText(this.textTrecho.getText() + this.recebe);
        }  
        
        try {       
            conexao.desconecta();
        } catch (IOException ex) {
            Logger.getLogger(TelaInicialController.class.getName()).log(Level.SEVERE, null, ex);
        }
            
        if (conexao.conecta()) {

            if (this.recebe.equals("")) {
                conexao.envia("cidades");
                this.recebe = conexao.recebe().replace("[", "").replace("]", "").replace(" ", "");
                System.out.println("Cidades: " + recebe);
                this.comboInicio.getItems().addAll(recebe.split(","));
            } else {
                this.comboInicio.getItems().add(String.valueOf(this.textTrecho.getText().charAt(this.textTrecho.getText().length() - 1)));
            }
        }
            
        try {       
            conexao.desconecta();
        } catch (IOException ex) {
            Logger.getLogger(TelaInicialController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}