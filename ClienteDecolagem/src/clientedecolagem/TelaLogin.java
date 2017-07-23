package clientedecolagem;

import java.io.IOException;
import java.net.UnknownHostException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javax.swing.JOptionPane;


/**
 * Classe que inicializa a interface a tela do login. Inicializando com ele a
 * classe Conexao e o ip do servidor.
 *
 * @author felipe
 *
 */
public class TelaLogin extends Application {

    private static Stage stage;

    /**
     * Cria a tela e a mostra.
     */
    @Override
    public void start(Stage stage) throws Exception {
        TelaLogin.stage = stage;
        Parent parent = FXMLLoader.load(getClass().getResource("TelaLogin.fxml"));
        Scene scene = new Scene(parent);
        stage.setScene(scene);
        stage.setTitle("Sistema Decolagem");
        stage.show();

    }

    /**
     * Retorna o Stage da interface.
     *
     * @return Stage
     */
    public static Stage getStage() {
        return stage;
    }

    /**
     * Função principal que inicializa a classe conexão, recebe ip do servidor e
     * inicializa a interface de login.
     *
     * @param args
     * @throws UnknownHostException
     * @throws IOException
     */
    public static void main(String[] args) throws UnknownHostException, IOException {
        Conexao.singleton();
        
        String ip = JOptionPane.showInputDialog("Digite ip do servidor");
        String nome = JOptionPane.showInputDialog("Digite nome da empresa");
        if (!ip.equals("")) {
            Conexao.setIp(ip);
            Conexao.setNome(nome);

            launch(args);

        }

    }
}