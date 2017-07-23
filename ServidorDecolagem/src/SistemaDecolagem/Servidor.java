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
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import util.Grafo;

public class Servidor extends UnicastRemoteObject implements InterfaceComunicacao, Serializable {

    private transient Grafo caminhos;
    private transient ServerSocket serverSocket;
    private HashMap<String, String> clientes;// <nome, senha>

    protected Servidor() throws RemoteException {
        super();
        System.out.println("servidor on");
        caminhos = new Grafo();
        clientes = new HashMap<String, String>();
        try {
            LocateRegistry.createRegistry(1099);

            Naming.bind(ComunicacaoServidor.getNome(), this);

        } catch (Exception e) {

            System.out.println(e.getMessage());
        }
    }

    /**
     * Abri porta do servidor e permite que mais de um cliente a acesse.
     */
    public void executa() throws IOException {
        if (ComunicacaoServidor.getNome().equals("A")){
            serverSocket = new ServerSocket(12345);
        } else if(ComunicacaoServidor.getNome().equals("B")){
            serverSocket = new ServerSocket(12346);
        }else if (ComunicacaoServidor.getNome().equals("C")){
            serverSocket = new ServerSocket(12347);
        }
        

        while (true) {
            // aceita um cliente
            Socket cliente = serverSocket.accept();

            // cria uma classe para tratar cada novo cliente em uma nova thread
            TrataCliente tc = new TrataCliente(cliente.getInputStream(), cliente.getOutputStream(), this);
            new Thread(tc).start();
        }

    }

    /**
     * Fecha servidor. Nao permite que os clientes se conectem mais com ele.
     *
     * @throws IOException
     */
    public void fechar() throws IOException {
        serverSocket.close();
        System.exit(0);
    }

    /**
     * Retorna senha de acordo com seu nome
     *
     * @param nome
     * @return cliente
     */
    public String getSenha(String nome) {
        return clientes.get(nome);

    }

    /**
     * Adiciona cliente no servidor
     *
     * @param identificador
     * @param cliente
     */
    public void addCliente(String nome, String senha) {
        clientes.put(nome, senha);
    }

    @Override
    public ArrayList<String> getVizinhos(String origem) throws RemoteException {
        
        return caminhos.getVizinhos(origem);
    }

    public void caminhosPossiveis(String origem, String destino) throws RemoteException, NotBoundException, MalformedURLException {
        ComunicacaoServidor comunicacao = ComunicacaoServidor.getInstance();
        comunicacao.conectar();
        caminhos.caminhosPossiveis(origem, destino);
    }

    /**
     * Carrega informações do servidor que foram salvas.
     *
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void carregar() throws IOException, ClassNotFoundException {
        File f = new File("servidor" + ComunicacaoServidor.getNome() + ".ser");
        if (f.exists()) {
            FileInputStream fin = new FileInputStream(f);
            ObjectInputStream ois = new ObjectInputStream(fin);
            Servidor servidor = (Servidor) ois.readObject();
            setClientes(servidor.getClientes());

            ois.close();
        }

    }

    /**
     * Verfica se nome do cliente existe no servidor ou nao
     *
     * @param nome
     * @return boolean
     */
    public boolean contemCliente(String nome) {
        return clientes.containsKey(nome);
    }

    /**
     * Retorna os clientes
     *
     * @return clientes
     */
    public HashMap<String, String> getClientes() {
        return this.clientes;
    }

    /**
     * Muda o valor dos clientes
     *
     * @param clientes
     */
    public void setClientes(HashMap<String, String> clientes) {
        this.clientes = clientes;
    }

    /**
     * Carrega os trechos de uma empresa em um grafo.
     *
     * @throws FileNotFoundException
     */
    public void carregarTrechos() throws FileNotFoundException {
        String trecho[] = new String[3];
        // escolhe arquivos com os trechos da empresa
        JFileChooser arquivo = new JFileChooser();
        int returnVal = arquivo.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = arquivo.getSelectedFile();
            // le o arquivo com trechos
            Scanner scan = new Scanner(file);
            while (scan.hasNext()) {
                String s = scan.nextLine();
                // formato de s: origem - destino - numero de passagens
                trecho = s.split("-");
                //adiciona no grafo
                caminhos.addCaminho(trecho[0].trim(), trecho[1].trim());
            }
        }

    }

    public static void main(String[] args) throws FileNotFoundException, RemoteException, NotBoundException, MalformedURLException {
        ComunicacaoServidor.singleton();
        ComunicacaoServidor comunicacao = ComunicacaoServidor.getInstance();
        //Digite A, B ou C
        //A ideia aqui é permitir rodar em localhost
        //Cada empresa vai ter um bind diferente no servidor
        String name = JOptionPane.showInputDialog("qual nome da empresa?");
        comunicacao.setNome(name);

        Servidor servidor = new Servidor();
        servidor.carregarTrechos();
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
            //servidor deu algum erro durante execucao
        } catch (IOException e) {
            e.printStackTrace();
            //classe não encontrada, servidor nao roda
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

}
