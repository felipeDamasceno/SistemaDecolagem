package clientedecolagem;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

/**
 *
 * @author felipe
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

    /**
     * Funcao que executa ao inicializar.
     */
    @Override
    public void initialize(URL url, ResourceBundle bundle) {

    }

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

}
