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
    private static String ip;   //Ip do servidor	
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

    public static String getNome() {
        return nome;
    }
        
    /** Método que altera o valor do nome do servidor.
     * 
     * @param nome 
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

            switch (nome) {
                case "A":
                    this.socket = new Socket(ip, 12345);
                    break;
                case "B":
                    this.socket = new Socket(ip, 12346);
                    break;
                case "C":
                    this.socket = new Socket(ip, 12347);
                    break;
                default:
                    break;
            }
            this.entrada = new Scanner(this.socket.getInputStream());
            this.saida = new PrintStream(this.socket.getOutputStream());
            return true;   //Sucesso
    	} catch (IOException e){
            JOptionPane.showMessageDialog(null, "Erro ao conectar com o servidor",
                    "Erro", JOptionPane.ERROR_MESSAGE);
            return false;   //Erro
    	}
    }
   
    /** Método que envia uma string para o servidor.
     * 
     * @param s
     */
    public void envia(String s){
    	this.saida.println(s);
    }    
    
    /** Método que recebe uma string do servidor e a retorna.
     * 
     * @return String
     */
    public String recebe(){
    	return (this.entrada.nextLine());
    }
    
    /** Método que desconecta do servidor.
     * 
     * @throws IOException
     */
    public void desconecta() throws IOException{
    	this.saida.close();
    	this.entrada.close();
    	this.socket.close();
    }    
    
}