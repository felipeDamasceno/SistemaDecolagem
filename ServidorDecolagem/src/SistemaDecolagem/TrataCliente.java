package SistemaDecolagem;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Scanner;

/**
 * Classe que trata dos multi-clientes. Ela possue as funcoes para cada acao que
 * o cliente pode tomar no servidor central do jogo.
 *
 * @author felipe e Camille
 *
 */
public class TrataCliente implements Runnable {

    private Scanner entrada;
    private PrintStream saida;
    private Servidor servidor;

    /**
     * Inicializados da classe. Recebe o servidor e as entradas e saidas para
     * comunicacao com cliente
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

    /**
     * Metodo que eh executado quando a classe inicia. Pedi uma acao do cliente
     * para chamar a funcao apropriada para ela. Depois fecha a conexao com
     * cliente.
     */
    public void run() {
        try {
            String acao = entrada.nextLine();
            if (acao.equals("cadastrar")) {
                cadastrar();
            } else if (acao.equals("entrar")) {
                entrar();
            } else if (acao.equals("fechar")) {
                servidor.fechar();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            entrada.close();
            saida.close();
        }
    }
   
    /**
     * Salva servidor em arquivo.
     *
     * @throws IOException
     */
    private void salvar() throws IOException {
        FileOutputStream arquivoGrav = new FileOutputStream("servidor"+ComunicacaoServidor.getNome()+".ser");
        ObjectOutputStream objGravar = new ObjectOutputStream(arquivoGrav);

        objGravar.writeObject(servidor);
        objGravar.flush();
        objGravar.close();

        arquivoGrav.flush();
        arquivoGrav.close();

    }

    /**
     * Loga em uma conta se informacoes estiver correta
     */
    private void entrar() {
        String nome = entrada.nextLine();
        String senha = entrada.nextLine();
        String confirmarSenha = servidor.getSenha(nome);

        if (confirmarSenha != null) {// existe senha
            // senha esta correta
            if (senha.equals(confirmarSenha)) {
                saida.println("1");// tudo correto

            }

        }
        saida.println("0");// teve erro

    }

    /**
     * Cadastra um cliente ao banco
     *
     * @throws IOException
     */
    private void cadastrar() throws IOException {
        String nome = entrada.nextLine();
        String senha = entrada.nextLine();
        if (!servidor.contemCliente(nome)) {
            servidor.addCliente(nome, senha);
            saida.println("1");// tudo correto
            salvar();
        } else {
            saida.println("0");//nome ja existe
        }

    }
}