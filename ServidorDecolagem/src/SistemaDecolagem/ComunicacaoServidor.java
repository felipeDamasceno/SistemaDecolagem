package SistemaDecolagem;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.LinkedList;

public class ComunicacaoServidor {
    private static ComunicacaoServidor cliente;
    private static String nome;
    private LinkedList<InterfaceComunicacao> servidores;
    
    public ComunicacaoServidor(){
        servidores = new LinkedList<InterfaceComunicacao>();
    }
    
    public static String getNome(){
        return nome;
    }
    
    public void setNome(String nome){
        ComunicacaoServidor.nome = nome;
    }
    
    public static ComunicacaoServidor getInstance(){
        return cliente;
    }
    
    public static void singleton(){
        cliente = new ComunicacaoServidor();
        
    }
    
    public void conectar() throws NotBoundException, RemoteException, MalformedURLException{
        InterfaceComunicacao c;
        if (nome.equals("A")){
            c = (InterfaceComunicacao) Naming.lookup("rmi://localhost/B");
            servidores.add(c);
            c = (InterfaceComunicacao) Naming.lookup("rmi://localhost/C");
            servidores.add(c);
               
        }else if (nome.equals("B")){
            c = (InterfaceComunicacao) Naming.lookup("rmi://localhost/A");
            servidores.add(c);
            c = (InterfaceComunicacao) Naming.lookup("rmi://localhost/C");
            servidores.add(c);
            
        }else if (nome.equals("C")){
            c = (InterfaceComunicacao) Naming.lookup("rmi://localhost/A");
            servidores.add(c);
            c = (InterfaceComunicacao) Naming.lookup("rmi://localhost/B");
            servidores.add(c);
        }
        
    }
    
    public ArrayList<String> getVizinhos(String origem) throws RemoteException{
        ArrayList<String> vizinhos = new ArrayList<String>();
        for (InterfaceComunicacao servidor: servidores){
            vizinhos.addAll(servidor.getVizinhos(origem));
        }
        return vizinhos;
    }
}

   

