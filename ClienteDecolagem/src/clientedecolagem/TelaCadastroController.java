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
 * Classe que possui as funcoes dos botoes da interface de cadastro de cliente.
 *
 * @author felipe
 *
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

    /**
     * Funcao que executa ao inicializar.
     */
    @Override
    public void initialize(URL url, ResourceBundle bundle) {

    }

    /**
     * Volta para a tela de login.
     *
     * @param event
     * @throws Exception
     */
    @FXML
    private void clicaVoltar(ActionEvent event) throws Exception {
        new TelaLogin().start(new Stage());
        TelaCadastro.getStage().close();
    }

    /**
     * Cadastra cliente ao banco. Se informações fornecidas estão da forma
     * correta, cliente é cadastrado xcom sucesso, se não o cliente é avisado do
     * erro.
     *
     * @param event
     * @throws Exception
     */
    @FXML
    private void clicaCadastrar(ActionEvent event) throws Exception {
        String nome = fieldNickname.getText();
        String senha = fieldSenha.getText();

        if (verificarCampos(nome, senha)) {

            Conexao cliente = Conexao.getInstancia();
            if (cliente.conecta()) {
                cliente.envia("cadastrar");
                cliente.envia(nome);
                cliente.envia(senha);

                String resposta = cliente.recebe();
                cliente.desconecta();

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

    /**
     * Verifica se os campos da interface nao estao vazios e a comfirmacao da
     * senha iqual a senha.
     *
     * @param nome
     * @param senha
     * @param confirmarSenha
     * @return boolean, true para campos corretos e false campos incorretos.
     */
    private boolean verificarCampos(String nome, String senha) {
        if (!nome.equals("") && !senha.equals("")) {
            return true;
        } else {
            JOptionPane.showMessageDialog(null, "Campo(s) vazio(s)!", "Alerta!", JOptionPane.WARNING_MESSAGE);
        }
        return false;//tem erro
    }

}
