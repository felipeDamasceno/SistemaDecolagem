/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientedecolagem;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import javax.swing.JOptionPane;


/**
 * Classe Conexao, inicializa a conexão com o servidor e se comunica com ele enviando
 * e recebendo strings.
 * 
 * @author Camille Jesus e Felipe Damasceno
 */
public class Conexao {
    
    private static Conexao Conexao;

    private Socket socket;
    private Scanner entrada;
    private PrintStream saida;
    private static String ip;// ip do servidor	
    private static String nome;

    /** Método que inicializa a classe.
     * 
     * @throws UnknownHostException
     * @throws IOException
     */
    public static void singleton() throws UnknownHostException, IOException{
        Conexao = new Conexao();
    }

    /**Método que retorna a instância da classe.
     * 
     * @return Conexao
     */
    public static Conexao getInstancia(){
        return Conexao; 
    }

    /** Método que altera o valor do ip do servidor.
     * 
     * @param ip
     */
    public static void setIp(String ip){
            Conexao.ip = ip;
    }
    
    
    /** Método que altera o valor do nome da empresa.
     * 
     * @param ip
     */
    public static void setNome(String nome){
            Conexao.nome = nome;
    }
    
    

    /** Método que conecta com o servidor.
     * 
     * @return true or false
     */	
    public boolean conecta() {
        
    	try{
            if (nome.equals("A")){
                socket = new Socket(ip, 12345);
            }else if (nome.equals("B")){
                socket = new Socket(ip, 12346);
            }else if (nome.equals("C")){
                socket = new Socket(ip, 12347);
            }
            

            entrada = new Scanner(socket.getInputStream());
            saida = new PrintStream(socket.getOutputStream());
            return true; // sucesso
    	} catch (Exception e){
            JOptionPane.showMessageDialog(null, "Erro ao conectar o servidor", "Erro",
                    JOptionPane.ERROR_MESSAGE);
            return false; // deu erro
    	}
    }
   
    /** Método que envia uma string para o servidor.
     * 
     * @param s
     */
    public void envia(String s){
    	saida.println(s);
    }    
    
    /** Método que recebe uma string do servidor e a retorna.
     * 
     * @return String
     */
    public String recebe(){
    	return entrada.nextLine();
    }
    
    /** Método que desconecta do servidor.
     * 
     * @throws IOException
     */
    public void desconecta() throws IOException{
    	saida.close();
    	entrada.close();
    	socket.close();
    }    
    
}