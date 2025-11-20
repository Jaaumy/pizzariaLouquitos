package pizzaria.dao;

import pizzaria.model.Pizza;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class PizzaDAO {

    public List<Pizza> listarPizzas() {
        String sql = "SELECT * FROM pizzas ORDER BY nome";

        List<Pizza> pizzas = new ArrayList<>();

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = ConnectionFactory.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Pizza pizza = new Pizza();
                pizza.setIdPizza(rs.getInt("id_pizza"));
                pizza.setNome(rs.getString("nome"));
                pizza.setDescricao(rs.getString("descricao"));
                pizza.setPreco(rs.getDouble("preco"));
                pizza.setImagem(rs.getString("imagem"));

                pizzas.add(pizza);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao listar pizzas: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return pizzas;
    }

    public void adicionarPizza(Pizza pizza) {
        String sql = "INSERT INTO pizzas (nome, descricao, preco, imagem) VALUES (?, ?, ?, ?)";

        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = ConnectionFactory.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, pizza.getNome());
            ps.setString(2, pizza.getDescricao());
            ps.setDouble(3, pizza.getPreco());
            ps.setString(4, pizza.getImagem());

            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Pizza '" + pizza.getNome() + "' adicionada com sucesso!");

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao adicionar pizza: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}