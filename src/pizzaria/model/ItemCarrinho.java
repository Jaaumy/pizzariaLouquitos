package pizzaria.model;

public class ItemCarrinho {
    private Pizza pizza;
    private int quantidade;

    public ItemCarrinho(Pizza pizza, int quantidade) {
        this.pizza = pizza;
        this.quantidade = quantidade;
    }

    public Pizza getPizza() {
        return pizza;
    }

    public void setPizza(Pizza pizza) {
        this.pizza = pizza;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public double getSubtotal() {
        return pizza.getPreco() * quantidade;
    }
}