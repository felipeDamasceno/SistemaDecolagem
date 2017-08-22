package util;

import SistemaDecolagem.ComunicacaoServidor;

import java.rmi.RemoteException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;


/**
 * Classe Grafo, representa todas as cidades e trechos por lista de adjacência.
 * 
 * @author Camille Jesus e Felipe Damasceno
 */
public class Grafo {
    
    private HashMap<String,ArrayList<Trecho>> grafo;   //Lista de adjacência
    private Stack<String> caminhoAtual;   //Pilha de caminho atual
    private TreeSet<String> visitadas;   //Conjunto de cidades visitadas no caminho
    private ArrayList<ArrayList<String>> todosCaminhos = new  ArrayList<>();
    //Estabelece comunicação:
    private static ComunicacaoServidor comunicacao = ComunicacaoServidor.getInstance();
    
    /** Construtor da classe, o grafo é inicializado.
     */
    public Grafo(){
        this.grafo = new HashMap<>();
        this.caminhoAtual = new Stack<>();
        this.visitadas = new TreeSet<>();
        this.todosCaminhos = new ArrayList<>();
    }

    public ArrayList<ArrayList<String>> getTodosCaminhos() {
        System.out.println("todos os caminhos grafo");
        System.out.println(todosCaminhos + "no grafo");
        return this.todosCaminhos;
    }
    
    public HashSet<String> getTodasCidades() throws RemoteException {
        HashSet<String> todasCidades = new HashSet<>();
        
        this.grafo.keySet().forEach((cidade) -> {
            todasCidades.add(cidade);
        });
        return todasCidades;        
    }
    
    public void limpaTodosCaminhos() {
        this.todosCaminhos.clear();
    }
    
    /** Método que retorna uma lista de cidades vizinhas de uma dada origem.
     *
     * @param origem
     * 
     * @return ArrayList
     */
    public ArrayList<Trecho> getVizinhos(String origem){
        return (this.grafo.get(origem));
    }
    
    public String diminuiAssento(String inicio, String fim) {
        ArrayList<Trecho> trechos = this.grafo.get(inicio);
        
        if (trechos != null) {
                
            for (Trecho trecho : trechos) {

                if (trecho.getCidade().equals(fim)) {

                    if (trecho.getAssentos() > 0) {
                        trecho.diminuiAssento();
                        System.out.println("compra realizada");
                        System.out.println(trecho.getAssentos());
                        return "1";
                    }
                }
            }
        }
        System.out.println("compra não realizada");
        return "0";
    }
    
    /** Método que recebe as cidades de origem e destino, e cria um trecho entre elas.
     *
     * @param origem
     * @param destino
     */
    public void addCaminho(String origem, Trecho destino) {
        
        if (this.grafo.containsKey(origem)) {
            this.grafo.get(origem).add(destino);
        } else {
            ArrayList<Trecho> vizinhos = new ArrayList<>();
            vizinhos.add(destino);
            this.grafo.put(origem, vizinhos);
        }        
    }    
    
    /** Método que identifica todas as possíveis rotas (conjuntos de trechos) formadas
     * a partir das cidades de origem e destino especificadas.
     *
     * @param origem
     * @param destino
     */
    public void caminhosPossiveis(String origem, String destino) throws RemoteException {
        this.caminhoAtual.push(origem);   //Adiciona cidade na pilha
        this.visitadas.add(origem);   //Adiciona cidade na lista de visitadas
        
        if (origem.equals(destino)) {   //Se chegou ao destino:
            ArrayList<String> rota = new ArrayList<>();
                        
            for (String cidade : this.caminhoAtual) {
                rota.add(cidade);
            }
            rota.add("@");
            this.todosCaminhos.add(rota);
            
            /*
            this.todosCaminhos.add(this.caminhoAtual.toString());
            */
            System.out.println(rota);
        } else {   //Se não:
            
            //Pega vizinhos de tal origem no próprio servidor:
            ArrayList<Trecho> vizinhos = this.getVizinhos(origem);
            //Pega vizinhos de tal origem nos outros servidores:
            ArrayList<Trecho> outrosVizinhos = comunicacao.getVizinhos(origem);
            
            if (vizinhos != null) {
                outrosVizinhos.addAll(vizinhos);   //Cria lista geral de vizinhos
            }
            
            for (Trecho vizinho : outrosVizinhos) {   //Percorre a lista de vizinhos totais
                
                if (!this.visitadas.contains(vizinho.getCidade())){   //Se cidade vizinha ainda não foi visitada:
                    //Método recursivo que estabelece caminhos possíveis a partir dela:
                    this.caminhosPossiveis(vizinho.getCidade(), destino);
                }
            }
        }
        this.caminhoAtual.pop();   //Remove cidade-topo da pilha
        this.visitadas.remove(origem);   //Remove cidade da lista de visitadas
    }
            
}