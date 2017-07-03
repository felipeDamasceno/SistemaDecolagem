/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uefs.ecomp.tempo.view;

import br.uefs.ecomp.tempo.connection.Conexao;
import java.io.IOException;
import javafx.application.Platform;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

/**
 *
 * @author felipe
 */
public class TelaLogin {
    public static void main(String[] args) throws IOException, Exception {
       String nome = JOptionPane.showInputDialog("Qual o seu nome?");
       Conexao.singleton();
        
        if (Conexao.getInstancia() != null) {
            Conexao conexao = Conexao.getInstancia();
            conexao.conectar();
            conexao.setNome(nome);
            conexao.setMestre(nome);
        }
        javafx.application.Application.launch(TelaRelogio.class);
        
    }
}
