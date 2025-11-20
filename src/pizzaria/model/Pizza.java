package pizzaria.model;

public class Pizza {
    private int idPizza;
    private String nome;
    private String descricao;
    private double preco;
    private String imagem;

    public Pizza() {}

    public Pizza(int idPizza, String nome, String descricao, double preco) {
        this.idPizza = idPizza;
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
    }

    public int getIdPizza() {
        return idPizza;
    }

    public void setIdPizza(int idPizza) {
        this.idPizza = idPizza;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }
}