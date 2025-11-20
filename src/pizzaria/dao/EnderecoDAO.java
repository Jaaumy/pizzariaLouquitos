package pizzaria.dao;

import pizzaria.model.Endereco;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class EnderecoDAO {

    public List<Endereco> listarEnderecos(int idCliente) {
        List<Endereco> enderecos = new ArrayList<>();
        String sql = "SELECT * FROM enderecos WHERE id_cliente_ref = ?";

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = ConnectionFactory.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idCliente);
            rs = ps.executeQuery();

            while(rs.next()) {
                Endereco end = new Endereco();
                end.setIdEndereco(rs.getInt("id_endereco"));
                end.setIdClienteRef(rs.getInt("id_cliente_ref"));
                end.setRotulo(rs.getString("rotulo"));
                end.setRua(rs.getString("rua"));
                end.setNumero(rs.getString("numero"));
                end.setBairro(rs.getString("bairro"));
                end.setCep(rs.getString("cep"));
                end.setComplemento(rs.getString("complemento"));
                enderecos.add(end);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao listar endereços: " + e.getMessage());
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
        return enderecos;
    }

    public void salvarEndereco(Endereco endereco) {
        String sql = "INSERT INTO enderecos (id_cliente_ref, rotulo, rua, numero, bairro, cep, complemento) VALUES (?, ?, ?, ?, ?, ?, ?)";

        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = ConnectionFactory.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, endereco.getIdClienteRef());
            ps.setString(2, endereco.getRotulo());
            ps.setString(3, endereco.getRua());
            ps.setString(4, endereco.getNumero());
            ps.setString(5, endereco.getBairro());
            ps.setString(6, endereco.getCep());
            ps.setString(7, endereco.getComplemento());

            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Endereço '" + endereco.getRotulo() + "' salvo com sucesso!");

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao salvar endereço: " + e.getMessage());
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