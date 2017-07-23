package util;

import SistemaDecolagem.ComunicacaoServidor;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;
import java.util.TreeSet;

/**
 *
 * @author felipe
 */
public class Grafo {
    //lista de adjacencia
    private HashMap<String,ArrayList<String>> caminhos;
    //caminho atual
    private Stack<String> Caminho  = new Stack<String>();
    // conjunto de vertices que foram vizitados no caminho
    private TreeSet<String> vizitados  = new TreeSet<String>();
    
    public Grafo(){
        caminhos = new HashMap<String,ArrayList<String>>();     
    }
    
    public void addCaminho(String origem, String destino){
        if (caminhos.containsKey(origem)){
            caminhos.get(origem).add(destino);
        }else{
            ArrayList<String> vizinhos = new ArrayList<String>();
            vizinhos.add(destino);
            caminhos.put(origem, vizinhos);
        }
        
    }
    
    public ArrayList<String> getVizinhos(String origem){
        return caminhos.get(origem);
    }
    
    
    //DFS
    public void caminhosPossiveis(String origem, String destino) throws RemoteException {

        Caminho.push(origem);
        vizitados.add(origem);

        if (origem.equals(destino)) 
            System.out.println(Caminho);

        else {
            ComunicacaoServidor comunicacao = ComunicacaoServidor.getInstance();
            ArrayList<String> vizinhos = comunicacao.getVizinhos(origem);
            vizinhos.addAll(getVizinhos(origem));
            for (String vizinho : vizinhos) {
                if (!vizitados.contains(vizinho)){
                    caminhosPossiveis(vizinho, destino);
                }
            }
        }

        Caminho.pop();
        vizitados.remove(origem);
        
    }
            
}
