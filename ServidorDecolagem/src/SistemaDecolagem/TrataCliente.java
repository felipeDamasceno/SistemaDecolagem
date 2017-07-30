package SistemaDecolagem;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import java.net.MalformedURLException;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import java.util.Scanner;


/** Classe TrataCliente, thread que é criada para tratamento de cada cliente aceito
 * no servidor.
 * 
 * @author Camille Jesus e Felipe Damasceno
 */
public class TrataCliente implements Runnable {

    private Scanner entrada;
    private PrintStream saida;
    private Servidor servidor;

    /** Construtor da classe que inicializa os atributos com os valores recebidos
     * de servidor, entrada e saída para comunicação com o "cliente".
     *
     * @param in
     * @param out
     * @param servidor
     */
    public TrataCliente(InputStream in, OutputStream out, Servidor servidor) {
        this.entrada = new Scanner(in);
        this.saida = new PrintStream(out);
        this.servidor = servidor;
    }

    /** Método executado para cada cliente aceito, inicia a leitura e escrita de
     * objetos e apresenta as condições de envio e execução para cada requisição
     * possível.
     */
    public void run() {
        
        try {
            
            switch (this.entrada.nextLine()) {
                case "cadastrar":
                    this.cadastrar();
                    break;
                case "entrar":
                    this.entrar();
                    break;
                case "cidades":
                    this.saida.println(this.servidor.getCidades());
                    break;
                case "caminhos":
                    this.caminhos();
                    break;
                case "fechar":
                    this.servidor.fechar();
                    break;                
                default:
                    break;
            }
        } catch (IOException | NotBoundException e) {
            e.printStackTrace();
        } finally {
            this.entrada.close();
            this.saida.close();
        }
    }
    
    /** Método que cadastra um cliente no sistema.
     *
     * @throws IOException
     */
    private void cadastrar() throws IOException {
        String nome = this.entrada.nextLine();
        String senha = this.entrada.nextLine();
        
        if (!this.servidor.contemCliente(nome)) {   //Se o cliente não está no servidor
            this.servidor.addCliente(nome, senha);   //Adiciona
            this.saida.println("1");   //Confirmação
            this.salvar();   //Salva as informações do cliente em arquivo
        } else {
            this.saida.println("0");   //Não confirmação
        }
    }
   
    /** Método que salva o servidor em arquivo.
     *
     * @throws IOException
     */
    private void salvar() throws IOException {
        FileOutputStream arquivoGrav = new FileOutputStream("servidor"+ComunicacaoServidor.getNome()+".ser");
        ObjectOutputStream objGravar = new ObjectOutputStream(arquivoGrav);
        objGravar.writeObject(this.servidor);
        objGravar.flush();
        objGravar.close();
        arquivoGrav.flush();
        arquivoGrav.close();
    }
    
    /** Método que loga um cliente no sistema.
     */
    private void entrar() {
        String nome = this.entrada.nextLine();
        String senha = this.entrada.nextLine();
        String confirmarSenha = this.servidor.getSenha(nome);

        if (confirmarSenha != null) {   //Se existe senha
            
            if (senha.equals(confirmarSenha)) {   //E ela está correta
                this.saida.println("1");   //Confirmação
            } else {
                this.saida.println("0");   //Não confirmação                
            }
        }
    }
    
    /** Método que verifica todos os caminhos da origem ao destino especificados.
     * 
     * @throws RemoteException
     * @throws NotBoundException
     * @throws MalformedURLException 
     */
    private void caminhos() throws RemoteException, NotBoundException, MalformedURLException{
        String origem = this.entrada.nextLine();
        String destino = this.entrada.nextLine();        
        this.servidor.caminhosPossiveis(origem, destino);
    }
    
}