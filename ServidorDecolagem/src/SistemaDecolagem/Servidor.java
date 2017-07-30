package SistemaDecolagem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.AlreadyBoundException;

import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import javax.swing.JOptionPane;

import util.Grafo;


/** Classe Servidor, responsável por atender as solicitações dos clientes e conectar
 * os servidores.
 * 
 * @author Camille Jesus e Felipe Damasceno
 */
public class Servidor extends UnicastRemoteObject implements InterfaceComunicacao, Serializable {

    private transient Grafo grafo;
    private transient ServerSocket serverSocket;
    private HashMap<String, String> clientes;// <nome, senha>

    protected Servidor() throws RemoteException {
        super();
        this.grafo = new Grafo();
        this.clientes = new HashMap<>();
        
        try {
            switch (ComunicacaoServidor.getNome()) {
                case "A":
                    LocateRegistry.createRegistry(1099);
                    break;
                case "B":
                    LocateRegistry.createRegistry(2000);
                    break;
                case "C":
                    LocateRegistry.createRegistry(2001);
                    break;
                default:
                    break;
            }
            Naming.bind(ComunicacaoServidor.getNome(), this);
        } catch (MalformedURLException | AlreadyBoundException | RemoteException e) {
            System.out.println(e.getMessage());
        }
    }
    
    /** Método que retorna a senha de um cliente.
     *
     * @param nome
     * 
     * @return cliente
     */
    public String getSenha(String nome) {
        return (this.clientes.get(nome));
    }
    
    /** Método que retorna os clientes.
     *
     * @return clientes
     */
    public HashMap<String, String> getClientes() {
        return (this.clientes);
    }

    /**
     * Muda o valor dos clientes
     *
     * @param clientes
     */
    public void setClientes(HashMap<String, String> clientes) {
        this.clientes = clientes;
    }
    
    /** Método que adiciona um cliente.
     *
     * @param nome
     * @param senha
     */
    public void addCliente(String nome, String senha) {
        this.clientes.put(nome, senha);
    }
    
    /** Método que verfica se cliente já está cadastrado.
     *
     * @param nome
     * 
     * @return boolean (true or false)
     */
    public boolean contemCliente(String nome) {
        return (this.clientes.containsKey(nome));
    }
    
    /** Método remoto que retorna uma lista de cidades vizinhas de uma dada origem.
     * 
     * @param origem
     * 
     * @return ArrayList
     * 
     * @throws RemoteException 
     */
    @Override
    public ArrayList<String> getVizinhos(String origem) throws RemoteException {
        return (this.grafo.getVizinhos(origem));
    }   
    
    /** Método que abre uma porta do servidor e permite que mais de um cliente a acesse.
     * @throws java.io.IOException
     */
    public void executa() throws IOException {
        
        switch (ComunicacaoServidor.getNome()) {
            case "A":
                this.serverSocket = new ServerSocket(12345);
                break;
            case "B":
                this.serverSocket = new ServerSocket(12346);
                break;
            case "C":
                this.serverSocket = new ServerSocket(12347);
                break;
            default:
                break;
        }

        while (true) {            
            Socket cliente = (this.serverSocket.accept());   //Aceita um cliente

            //Inicia uma nova thread para o cliente:
            new Thread(new TrataCliente(cliente.getInputStream(), cliente.getOutputStream(), this)).start();
        }
    }

    /** Método que desconecta o servidor.
     *
     * @throws IOException
     */
    public void fechar() throws IOException {
        this.serverSocket.close();
        System.exit(0);
    }

    /** Método que verifica todos os caminhos da origem ao destino especificados.
     * 
     * @param origem
     * @param destino
     * 
     * @throws RemoteException
     * @throws NotBoundException
     * @throws MalformedURLException 
     */
    public void caminhosPossiveis(String origem, String destino) throws RemoteException, NotBoundException, MalformedURLException {
        ComunicacaoServidor comunicacao = ComunicacaoServidor.getInstance();
        comunicacao.conectar();
        comunicacao.hello();   //Teste de comunicação
        this.grafo.caminhosPossiveis(origem, destino);
    }

    /** Método que carrega os dados dos clientes para o sistema.
     * 
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void carregar() throws IOException, ClassNotFoundException {
        File file = new File("servidor" + ComunicacaoServidor.getNome() + ".ser");
        
        if (file.exists()) {
            Servidor servidor = (Servidor) (new ObjectInputStream(new FileInputStream(file))).readObject();
            setClientes(servidor.getClientes());
        }
    }
    
    /** Método que carrega os dados dos trechos no sistema.
     *
     * @throws FileNotFoundException
     */
    public void carregarTrechos() throws FileNotFoundException {
        String info[] = new String[3];
        //Escolhe o arquivo com os trechos da empresa:
        File file = new File("trechos_" + ComunicacaoServidor.getNome());
        Scanner scan = new Scanner(file);   //Lê
        
        while (scan.hasNext()) {
            info = (scan.nextLine()).split("-");   //Formato: origem - destino - número de passagens
            this.grafo.addCaminho(info[0].trim(), info[1].trim());   //Adiciona ao grafo            
        }
    }

    public static void main(String[] args) throws FileNotFoundException, RemoteException, NotBoundException, MalformedURLException {
        ComunicacaoServidor.singleton();
        ComunicacaoServidor comunicacao = ComunicacaoServidor.getInstance();
        comunicacao.setNome(JOptionPane.showInputDialog(null, "Informe Empresa: "));        
        Servidor servidor = new Servidor();
        servidor.carregarTrechos();
        System.out.println("Servidor on...");
        //Aqui a comunicacao ainda nao ta funcionando, pois quando em faço
        //comunicacao.conectar ele vai conectar com servidores que talvez 
        //ainda nao esteja on gerando um erro. Dessa forma o conectar tem 
        //que ser feito por fora, quando o cliente chamar alguma funcao sei la.
        //Outra coisa, nao testei o caminhosPossiveis ainda, precisa que um 
        //cliente chame, pelo mesmo motivo do conectar, tem que ser externo,
        //depois de todos os servidores estar on.
        
        try {
            servidor.carregar();
            servidor.executa();
            servidor.fechar();            
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    //Teste de comunicação.
    @Override
    public String hello() {
        return ("Hello");
    }
    
}