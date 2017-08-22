package SistemaDecolagem;

import java.net.MalformedURLException;

import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import util.Trecho;


/**
 * 
 * @author Camille Jesus e Felipe Damasceno
 */
public class ComunicacaoServidor {
    
    private static ComunicacaoServidor cliente;   //Autoreferência
    private static String nome;   //Nome do servidor
    private LinkedList<InterfaceComunicacao> servidores;   //Lista de servidores
    
    /** Construtor da classe, inicializa a lista de servidores.
     */
    public ComunicacaoServidor(){
        this.servidores = new LinkedList<InterfaceComunicacao>();
    }
    
    /** Método que cria o objeto de comunicação.
     */
    public static void singleton(){
        cliente = new ComunicacaoServidor();        
    }
    
    /** Método que retorna a instância da comunicação.
     * 
     * @return 
     */
    public static ComunicacaoServidor getInstance(){
        return cliente;
    }
    
    /** Método que retorna o nome do servidor.
     * 
     * @return nome
     */
    public static String getNome(){
        return nome;
    }
    
    /** Método que altera o nome do servidor.
     * 
     * @param nome 
     */
    public void setNome(String nome){
        ComunicacaoServidor.nome = nome;
    }
    
    /** Método que conecta com a interface remota.
     * 
     * @throws NotBoundException
     * @throws RemoteException
     * @throws MalformedURLException 
     */
    public void conectar() throws NotBoundException, RemoteException, MalformedURLException{
        InterfaceComunicacao c;

        switch (nome) {
            case "A":
                c = (InterfaceComunicacao) Naming.lookup("rmi://localhost/B");
                this.servidores.add(c);
                c = (InterfaceComunicacao) Naming.lookup("rmi://localhost/C");
                this.servidores.add(c);
                break;
            case "B":
                c = (InterfaceComunicacao) Naming.lookup("rmi://localhost/A");
                this.servidores.add(c);
                c = (InterfaceComunicacao) Naming.lookup("rmi://localhost/C");
                this.servidores.add(c);
                break;
            case "C":
                c = (InterfaceComunicacao) Naming.lookup("rmi://localhost/A");
                this.servidores.add(c);
                c = (InterfaceComunicacao) Naming.lookup("rmi://localhost/B");
                this.servidores.add(c);
                break;
            default:
                break;
        }
        System.out.println("Interface carregada...");
    }
    
    /** Método remoto que retorna uma lista de cidades vizinhas de uma dada origem.
     * 
     * @param origem
     * 
     * @return ArrayList
     * 
     * @throws RemoteException 
     */
    public ArrayList<Trecho> getVizinhos(String origem) throws RemoteException{
        ArrayList<Trecho> vizinhos = new ArrayList<>();
        
        for (InterfaceComunicacao servidor: this.servidores){
            ArrayList<Trecho> v = servidor.getVizinhos(origem);
            
            if (v != null){
                vizinhos.addAll(v);
            }              
        }        
        return vizinhos;
    }
    
    public HashSet<String> getCidades() throws RemoteException{
        HashSet<String> cidades = new HashSet<>();
        
        for (InterfaceComunicacao servidor: this.servidores){
            HashSet<String> c = servidor.getCidades();
            
            if (c != null){
                cidades.addAll(c);
            }              
        }        
        return cidades;
    }
    
    //Teste de comunicação.
    public void hello() throws RemoteException{
       
        for (InterfaceComunicacao servidor: this.servidores){
            System.out.println(servidor.hello());
        }        
    }

    public void addCompra(String nomeCliente, String inicio, String fim) throws RemoteException {
        
        for (InterfaceComunicacao servidor: this.servidores) {
            servidor.addCompra(nomeCliente, inicio, fim);            
        }
    }

    public String compraPassagem(String inicio, String fim) throws RemoteException {
        
        for (InterfaceComunicacao servidor: this.servidores) {
            return (servidor.compraPassagem(inicio, fim));                       
        }
        return "0";
    }
    
}