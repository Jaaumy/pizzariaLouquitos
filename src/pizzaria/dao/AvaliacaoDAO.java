package pizzaria.dao;

import pizzaria.model.Avaliacao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class AvaliacaoDAO {

    public void salvarAvaliacao(Avaliacao avaliacao) {
        String sql = "INSERT INTO avaliacoes (id_pizza_ref, id_cliente_ref, nota, comentario) VALUES (?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = ConnectionFactory.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, avaliacao.getIdPizzaRef());
            ps.setInt(2, avaliacao.getIdClienteRef());
            ps.setInt(3, avaliacao.getNota());
            ps.setString(4, avaliacao.getComentario());
            ps.executeUpdate();

            JOptionPane.showMessageDialog(null, "Avaliação salva com sucesso!");

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao salvar avaliação: " + e.getMessage());
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

    public double getMediaAvaliacaoPorPizza(int idPizza) {
        String sql = "SELECT AVG(nota) FROM avaliacoes WHERE id_pizza_ref = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        double media = 0.0;

        try {
            conn = ConnectionFactory.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idPizza);
            rs = ps.executeQuery();

            if (rs.next()) {
                media = rs.getDouble(1);
            }
        } catch (SQLException e) {
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
        return media;
    }

    public List<Avaliacao> listarTodasAvaliacoes() {
        List<Avaliacao> avaliacoes = new ArrayList<>();
        String sql = "SELECT * FROM avaliacoes ORDER BY data_avaliacao DESC";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = ConnectionFactory.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Avaliacao a = new Avaliacao();
                a.setIdAvaliacao(rs.getInt("id_avaliacao"));
                a.setIdPizzaRef(rs.getInt("id_pizza_ref"));
                a.setIdClienteRef(rs.getInt("id_cliente_ref"));
                a.setNota(rs.getInt("nota"));
                a.setComentario(rs.getString("comentario"));
                a.setDataAvaliacao(rs.getTimestamp("data_avaliacao"));
                avaliacoes.add(a);
            }
        } catch (SQLException e) {
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
        return avaliacoes;
    }
}