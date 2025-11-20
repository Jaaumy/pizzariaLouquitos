package pizzaria.model;

import java.util.Date;

public class Avaliacao {

    private int idAvaliacao;
    private int idPizzaRef;
    private int idClienteRef;
    private int nota;
    private String comentario;
    private Date dataAvaliacao;

    public int getIdAvaliacao() {
        return idAvaliacao;
    }

    public void setIdAvaliacao(int idAvaliacao) {
        this.idAvaliacao = idAvaliacao;
    }

    public int getIdPizzaRef() {
        return idPizzaRef;
    }

    public void setIdPizzaRef(int idPizzaRef) {
        this.idPizzaRef = idPizzaRef;
    }

    public int getIdClienteRef() {
        return idClienteRef;
    }

    public void setIdClienteRef(int idClienteRef) {
        this.idClienteRef = idClienteRef;
    }

    public int getNota() {
        return nota;
    }

    public void setNota(int nota) {
        this.nota = nota;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Date getDataAvaliacao() {
        return dataAvaliacao;
    }

    public void setDataAvaliacao(Date dataAvaliacao) {
        this.dataAvaliacao = dataAvaliacao;
    }
}