/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SistemaDecolagem;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import util.Grafo;

public class Servidor extends UnicastRemoteObject implements InterfaceComunicacao{
    
    private Grafo caminhos;
 
    protected Servidor() throws RemoteException{
        super();
        System.out.println("servidor on");
        caminhos = new Grafo();
        try {
            LocateRegistry.createRegistry(1099);
            
            Naming.bind(ComunicacaoServidor.getNome(), this);

        } catch (Exception e) {

            System.out.println( e.getMessage());
        }
    }
    
    @Override
    public ArrayList<String> getVizinhos(String origem) throws RemoteException {
        return caminhos.getVizinhos(origem);
    }
    
    public void caminhosPossiveis(String v, String t) throws RemoteException{
        caminhos.caminhosPossiveis(v, t);
    }
    
    /**
     * Carrega os trechos de uma empresa em um grafo.
     * @throws FileNotFoundException 
     */
    public void carregarTrechos() throws FileNotFoundException {
        String trecho[] = new String[3];
        // escolhe arquivos com os trechos da empresa
        JFileChooser arquivo = new JFileChooser();
        int returnVal = arquivo.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = arquivo.getSelectedFile();
            // le o arquivo com trechos
            Scanner scan = new Scanner(file);
            while (scan.hasNext()) {
                String s = scan.nextLine();
                // formato de s: origem - destino - numero de passagens
                trecho = s.split("-"); 
                //adiciona no grafo
                caminhos.addCaminho(trecho[0].trim(), trecho[1].trim());
            }
        }

    }

    public static void main(String[] args) throws FileNotFoundException, RemoteException, NotBoundException, MalformedURLException {
        ComunicacaoServidor.singleton();
        ComunicacaoServidor comunicacao = ComunicacaoServidor.getInstance();
        //Digite A, B ou C
        //A ideia aqui é permitir rodar em localhost
        //Cada empresa vai ter um bind diferente no servidor
        String name = JOptionPane.showInputDialog("qual nome da empresa?");
        comunicacao.setNome(name);
        
        Servidor servidor = new Servidor();
        servidor.carregarTrechos();
        //Aqui a comunicacao ainda nao ta funcionando, pois quando em faço
        //comunicacao.conectar ele vai conectar com servidores que talvez 
        //ainda nao esteja on gerando um erro. Dessa forma o conectar tem 
        //que ser feito por fora, quando o cliente chamar alguma funcao sei la.
        //Outra coisa, nao testei o caminhosPossiveis ainda, precisa que um 
        //cliente chame, pelo mesmo motivo do conectar, tem que ser externo,
        //depois de todos os servidores estar on.
        
    }

    

}
