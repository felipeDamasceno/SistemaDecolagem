package util;

import java.io.Serializable;


/**
 *
 * @author Camille Jesus e Felipe Damasceno
 */
public class Trecho implements Serializable {
    
    private String cidade;
    private String servidor;
    private int assentos;

    public Trecho(String cidade, String servidor, int assentos) {
        this.cidade = cidade;
        this.servidor = servidor;
        this.assentos = assentos;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getServidor() {
        return servidor;
    }

    public void setServidor(String servidor) {
        this.servidor = servidor;
    }

    public int getAssentos() {
        return assentos;
    }

    public void setAssentos(int assentos) {
        this.assentos = assentos;
    }
    
    public void diminuiAssento() {
        this.assentos -= 1;
    }
    
}