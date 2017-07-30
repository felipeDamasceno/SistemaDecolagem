package SistemaDecolagem;

import java.rmi.Remote;
import java.rmi.RemoteException;

import java.util.ArrayList;


public interface InterfaceComunicacao extends Remote {
    
    public ArrayList<String> getVizinhos(String origem) throws RemoteException;
    public String hello() throws RemoteException;   //Teste de comunicação
    
}