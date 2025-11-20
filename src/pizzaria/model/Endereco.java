package pizzaria.model;

public class Endereco {
    private int idEndereco;
    private int idClienteRef;
    private String rotulo;
    private String rua;
    private String numero;
    private String bairro;
    private String cep;
    private String complemento;

    public int getIdEndereco() {
        return idEndereco;
    }

    public void setIdEndereco(int idEndereco) {
        this.idEndereco = idEndereco;
    }

    public int getIdClienteRef() {
        return idClienteRef;
    }

    public void setIdClienteRef(int idClienteRef) {
        this.idClienteRef = idClienteRef;
    }

    public String getRotulo() {
        return rotulo;
    }

    public void setRotulo(String rotulo) {
        this.rotulo = rotulo;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    @Override
    public String toString() {
        return String.format("%s: %s, %s - %s", rotulo, rua, numero, cep);
    }
}