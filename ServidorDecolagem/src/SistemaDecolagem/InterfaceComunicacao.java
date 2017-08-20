package SistemaDecolagem;

import java.rmi.Remote;
import java.rmi.RemoteException;

import java.util.ArrayList;
import java.util.Set;

import util.Trecho;


public interface InterfaceComunicacao extends Remote {
    
    public ArrayList<Trecho> getVizinhos(String origem) throws RemoteException;
    public Set<String> getCidades() throws RemoteException;
    public String hello() throws RemoteException;   //Teste de comunicação
    
}