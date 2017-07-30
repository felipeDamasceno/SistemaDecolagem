package clientedecolagem;

import javafx.event.ActionEvent;

import java.net.URL;

import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;

import javafx.stage.Stage;

import javax.swing.JOptionPane;


/**
 * Classe que controla a interface da tela de cadastro do cliente.
 *
 * @author Camille Jesus e Felipe Damasceno
 */
public class TelaCadastroController implements Initializable {

    @FXML
    private TitledPane telaCadastro;
    @FXML
    private Hyperlink voltar;
    @FXML
    private PasswordField fieldSenha;
    @FXML
    private AnchorPane pane;
    @FXML
    private TextField fieldNickname;
    @FXML
    private Button cadastrar;
    @FXML
    private Label labelNickname;
    @FXML
    private Label labelSenha;
    private static Conexao conexao = Conexao.getInstancia();

    /** Método que dispara o evento do botão, voltando à tela de login.
     * 
     * @param event
     * 
     * @throws java.lang.Exception
     */
    @FXML
    private void clicaVoltar(ActionEvent event) throws Exception {
        new TelaLogin().start(new Stage());
        TelaCadastro.getStage().close();
    }

    /** Método que cadastra cliente no sistema de decolagem, se as informações
     * fornecidas estiverem corretas, o cliente é cadastrado com sucesso, se não,
     * o cliente é informado sobre o erro.
     *
     * @param event
     * 
     * @throws Exception
     */
    @FXML
    private void clicaCadastrar(ActionEvent event) throws Exception {
        String nome = fieldNickname.getText();
        String senha = fieldSenha.getText();

        if (verificarCampos(nome, senha)) {
            
            if (conexao.conecta()) {
                conexao.envia("cadastrar");
                conexao.envia(nome);
                conexao.envia(senha);
                String resposta = conexao.recebe();
                conexao.desconecta();

                if (resposta.equals("1")) {
                    new TelaLogin().start(new Stage());
                    TelaCadastro.getStage().close();
                    JOptionPane.showMessageDialog(null, "Cadastro realizado!", "Confirmação", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Nickname já cadastrado!", "Erro!", JOptionPane.ERROR_MESSAGE);
                }
            }

        }
    }

    /** Método que verifica se os campos da interface estão devidamente preenchidos.
     *
     * @param nome
     * @param senha
     * 
     * @return boolean (true or false)
     */
    private boolean verificarCampos(String nome, String senha) {
        
        if (!nome.equals("") && !senha.equals("")) {
            return true;
        } else {
            JOptionPane.showMessageDialog(null, "Campo(s) vazio(s)!", "Alerta!", JOptionPane.WARNING_MESSAGE);
        }
        return false;
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