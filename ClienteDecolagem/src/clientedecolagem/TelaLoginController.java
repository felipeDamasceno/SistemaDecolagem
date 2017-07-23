/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
 * Classe que possui as funcoes dos botoes da interface de login.
 *
 * @author felipe
 *
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

    /**
     * Funcao que executa ao inicializar.
     */
    @Override
    public void initialize(URL url, ResourceBundle bundle) {

    }

    /**
     * Ao clicar no botao de login verifica informacoes (identificador e senha)
     * e se ela estiver no servidor abri a tela principal, caso contrario o
     * usuario recebe uma mensagem para informar do erro.
     *
     * @param event
     *
     */
    @FXML
    private void clicaEntrar(ActionEvent event) {
        try {
            String nome = fieldNickname.getText();
            String senha = fieldSenha.getText();

            if (verificarCampos(nome, senha)) {

                Conexao cliente = Conexao.getInstancia();
                if (cliente.conecta()) {
                    cliente.envia("entrar");
                    cliente.envia(nome);
                    cliente.envia(senha);

                    String resposta = cliente.recebe();
                    cliente.desconecta();

                    if (resposta.equals("1")) {
                       
                        System.out.println("Funcionou!!!");
                        
                        //TelaLogin.getStage().close();
                                  

                       
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

    /**
     * Verifica se os campos da interface estao em branco.
     *
     * @param nome
     * @param senha
     * @return true se campos nao estao em branco e false se esta em branco
     */
    private boolean verificarCampos(String identificador, String senha) {
        if (!identificador.equals("") && !senha.equals("")) {

            return true;//tudo correto
        } else {
            JOptionPane.showMessageDialog(null, "Campo(s) vazio(s)!", "Alerta!", JOptionPane.WARNING_MESSAGE);
        }
        return false;//tem erro
    }

    
    /**
     * Chama a tela de cadastro e fecha a tela atual.
     *
     * @param event
     *
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

}
