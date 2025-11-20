package pizzaria.model;

import java.util.Date;

public class Cliente {
    private int idCliente;
    private int idUsuarioRef;
    private String telefone;
    private Date dataNascimento;

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public int getIdUsuarioRef() {
        return idUsuarioRef;
    }

    public void setIdUsuarioRef(int idUsuarioRef) {
        this.idUsuarioRef = idUsuarioRef;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public Date getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }
}