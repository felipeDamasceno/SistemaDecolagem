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
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import javax.swing.JOptionPane;

import util.Grafo;
import util.Trecho;

/**
 * Classe Servidor, responsável por atender as solicitações dos clientes e
 * conectar os servidores.
 *
 * @author Camille Jesus e Felipe Damasceno
 */
public class Servidor extends UnicastRemoteObject implements InterfaceComunicacao, Serializable {

    private transient Grafo grafo;
    private transient ServerSocket serverSocket;
    private HashMap<String, String> clientes;   // <nome, senha>
    private HashMap<String, ArrayList<String>> compras;   // <nome, trechos>

    protected Servidor() throws RemoteException {
        super();
        this.grafo = new Grafo();
        this.clientes = new HashMap<>();
        this.compras = new HashMap<>();

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

    /**
     * Método que retorna os clientes.
     *
     * @return clientes
     */
    public HashMap<String, String> getClientes() {
        return (this.clientes);
    }

    /**
     * Método que altera os clientes.
     *
     * @param clientes
     */
    public void setClientes(HashMap<String, String> clientes) {
        this.clientes = clientes;
    }

    /**
     * Método que adiciona um cliente.
     *
     * @param nome
     * @param senha
     */
    public void addCliente(String nome, String senha) {
        this.clientes.put(nome, senha);
    }

    /**
     * Método que verfica se cliente já está cadastrado.
     *
     * @param nome
     *
     * @return boolean (true or false)
     */
    public boolean contemCliente(String nome) {
        return (this.clientes.containsKey(nome));
    }

    /**
     * Método que retorna a senha de um cliente.
     *
     * @param nome
     *
     * @return cliente
     */
    public String getSenha(String nome) {
        return (this.clientes.get(nome));
    }

    /**
     * Método que retorna as compras.
     *
     * @return clientes
     */
    public HashMap<String, ArrayList<String>> getCompras() {
        return (this.compras);
    }

    /**
     * Método que altera as comrpas.
     *
     * @param compras
     */
    public void setCompras(HashMap<String, ArrayList<String>> compras) {
        this.compras = compras;
    }

    /**
     * Método que adiciona uma trecho.
     *
     * @param nome
     * @param inicio
     * @param fim
     */
    @Override
    public void addCompra(String nome, String inicio, String fim) {

        if (this.contemCompra(nome) == false) {
            ArrayList<String> array = new ArrayList<>();
            array.add(inicio);
            array.add(fim);
            this.compras.put(nome, array);
            System.out.println("novo trecho" + nome);
            System.out.println(inicio + fim);
        } else {

            if (!this.getTrechos(nome).contains(fim)) {
                this.getTrechos(nome).add(fim);
            }
            System.out.println("trecho existente" + nome);
            System.out.println(fim);
        }
        System.out.println(this.getTrechos(nome));
    }

    /**
     * Método que verfica se cliente já está cadastrado.
     *
     * @param nome
     *
     * @return boolean (true or false)
     */
    public boolean contemCompra(String nome) {
        return (this.compras.containsKey(nome));
    }

    /**
     * Método que retorna as compras de um cliente.
     *
     * @param nome
     *
     * @return cliente
     */
    public ArrayList<String> getTrechos(String nome) {
        return (this.compras.get(nome));
    }

    /**
     * Método remoto que retorna uma lista de cidades vizinhas de uma dada
     * origem.
     *
     * @param origem
     *
     * @return ArrayList
     *
     * @throws RemoteException
     */
    @Override
    public ArrayList<Trecho> getVizinhos(String origem) throws RemoteException {
        return (this.grafo.getVizinhos(origem));
    }

    @Override
    public HashSet<String> getCidades() throws RemoteException {
        return (this.grafo.getTodasCidades());
    }

    /**
     * Método que abre uma porta do servidor e permite que mais de um cliente a
     * acesse.
     *
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

    /**
     * Método que desconecta o servidor.
     *
     * @throws IOException
     */
    public void fechar() throws IOException {
        this.serverSocket.close();
        System.exit(0);
    }

    public void limpaCaminhos() {
        this.grafo.limpaTodosCaminhos();
    }

    /**
     * Método que verifica todos os caminhos da origem ao destino especificados.
     *
     * @param origem
     * @param destino
     *
     * @throws RemoteException
     * @throws NotBoundException
     * @throws MalformedURLException
     */
    public String caminhosPossiveis(String origem, String destino) throws RemoteException, NotBoundException, MalformedURLException {

        this.grafo.caminhosPossiveis(origem, destino);
        return (this.grafo.getTodosCaminhos().toString());
    }

    public String cidadesPossiveis() throws RemoteException, NotBoundException, MalformedURLException {
        ComunicacaoServidor comunicacao = ComunicacaoServidor.getInstance();

        HashSet<String> cidades = this.grafo.getTodasCidades();
        HashSet<String> outrasCidades = comunicacao.getCidades();

        if (cidades != null) {
            outrasCidades.addAll(cidades);   //Cria lista geral de vizinhos
        }
        return (outrasCidades.toString());
    }

    public String trechosPossiveis(String inicio) throws RemoteException, NotBoundException, MalformedURLException {
        ComunicacaoServidor comunicacao = ComunicacaoServidor.getInstance();

        ArrayList<Trecho> vizinhos = this.grafo.getVizinhos(inicio);
        ArrayList<Trecho> outrosVizinhos = comunicacao.getVizinhos(inicio);

        if (vizinhos != null) {
            outrosVizinhos.addAll(vizinhos);   //Cria lista geral de vizinhos
        }
        String string[] = new String[outrosVizinhos.size()];

        for (int i = 0; i < outrosVizinhos.size(); i++) {
            string[i] = outrosVizinhos.get(i).getCidade() + "-" + "Servidor" + outrosVizinhos.get(i).getServidor() + "@";
        }
        return (Arrays.toString(string));
    }

    public String compra(String cliente, String inicio, String fim, String server) throws NotBoundException, RemoteException, MalformedURLException {
        ComunicacaoServidor comunicacao = ComunicacaoServidor.getInstance();

        if (ComunicacaoServidor.getNome().equals(server)) {
            System.out.println("compra no mesmo servidor");
            if (this.compraPassagem(inicio, fim).equals("1")) {
                this.addCompra(cliente, inicio, fim);
                comunicacao.addCompra(cliente, inicio, fim);
                return "1";
            }
            return "0";
        } else {
            System.out.println("compra em outro servidor");
            if (comunicacao.compraPassagem(inicio, fim).equals("1")) {
                comunicacao.addCompra(cliente, inicio, fim);
                this.addCompra(cliente, inicio, fim);
                return "1";
            }
            return "0";
        }

    }

    @Override
    public String compraPassagem(String inicio, String fim) {
        return (this.grafo.diminuiAssento(inicio, fim));
    }

    /**
     * Método que carrega os dados dos clientes para o sistema.
     *
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void carregar() throws IOException, ClassNotFoundException {
        File file = new File("servidor" + ComunicacaoServidor.getNome() + ".ser");

        if (file.exists()) {
            Servidor servidor = (Servidor) (new ObjectInputStream(new FileInputStream(file))).readObject();
            this.setClientes(servidor.getClientes());
            this.setCompras(servidor.getCompras());
        }
    }

    /**
     * Método que carrega os dados dos trechos no sistema.
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
            this.grafo.addCaminho(info[0].trim(), new Trecho(info[1].trim(), ComunicacaoServidor.getNome(), Integer.parseInt(info[2].trim())));   //Adiciona ao grafo            
        }
    }



    public String retornaReserva(String cliente) {
        String retorna = new String();
        ArrayList<String> array = this.getTrechos(cliente);

        if (array != null) {

            for (String trecho : array) {
                retorna += trecho + "->";
            }
            return retorna.substring(0, retorna.length() - 2);
        } else {
            return "";
        }

    }

       
}
