package clientedecolagem;

import java.net.URL;

import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import javafx.event.ActionEvent;

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


/**
 * Classe que controla a interface da tela de login do cliente.
 *
 * @author Camille Jesus e Felipe Damasceno
 */
public class TelaLoginController implements Initializable {

    @FXML
    private TitledPane telaLogin;
    @FXML
    private Button entrar;
    @FXML
    private PasswordField fieldSenha;
    @FXML
    private AnchorPane pane;
    @FXML
    private TextField fieldNickname;
    @FXML
    private Hyperlink cadastrese;
    @FXML
    private Label labelNickname;
    @FXML
    private Label labelSenha;
    private static Conexao conexao = Conexao.getInstancia();

    /** Método que loga cliente no sistema de decolagem, se as informações fornecidas
     * estiverem corretas, o cliente é cadastrado com sucesso, se não, o cliente
     * é informado sobre o erro.
     *
     * @param event
     * 
     * @throws Exception
     */
    @FXML
    private void clicaEntrar(ActionEvent event) {
        
        try {
            String nome = fieldNickname.getText();
            String senha = fieldSenha.getText();

            if (verificarCampos(nome, senha)) {
                
                if (conexao.conecta()) {
                    conexao.envia("entrar");
                    conexao.envia(nome);
                    conexao.envia(senha);
                    String resposta = conexao.recebe();
                    conexao.desconecta();

                    if (resposta.equals("1")) {                       
                        new TelaInicial().start(new Stage());                        
                        TelaLogin.getStage().close();
                    } else {
                        JOptionPane.showMessageDialog(null, "Informações inválidas!", "Erro!", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Problema no Servidor!", "Erro!", JOptionPane.ERROR_MESSAGE);
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
    
    /** Método que retorna à tela de cadastro e fecha a tela atual.
     *
     * @param event
     */
    @FXML
    private void clicaCadastrar(ActionEvent event) {
        
        try {
            new TelaCadastro().start(new Stage());
            TelaLogin.getStage().close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Problema ao mudar de tela!", "Erro!", JOptionPane.ERROR_MESSAGE);
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