/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.rmi.RemoteException;

/**
 *
 * @author felipe
 */
public class TestGrafo {

    public static void main(String[] args) throws RemoteException {
        Grafo G = new Grafo();
        G.addCaminho("A", "B");
        G.addCaminho("A", "C");
        G.addCaminho("C", "D");
        G.addCaminho("D", "E");
        G.addCaminho("C", "F");
        G.addCaminho("B", "F");
        G.addCaminho("F", "D");
        G.addCaminho("D", "G");
        G.addCaminho("E", "G");
        
        G.caminhosPossiveis("A", "G");
        System.out.println();
        G.caminhosPossiveis("B", "F");
    }
}
