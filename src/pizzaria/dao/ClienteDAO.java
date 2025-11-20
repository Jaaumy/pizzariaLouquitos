package pizzaria.dao;

import pizzaria.model.Cliente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class ClienteDAO {

    public void criarCliente(Cliente cliente, Connection conn) throws SQLException {
        String sql = "INSERT INTO clientes (id_usuario_ref, telefone, data_nascimento) VALUES (?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cliente.getIdUsuarioRef());
            ps.setString(2, cliente.getTelefone());

            if (cliente.getDataNascimento() != null) {
                ps.setDate(3, new java.sql.Date(cliente.getDataNascimento().getTime()));
            } else {
                ps.setNull(3, Types.DATE);
            }

            ps.executeUpdate();
        }
    }

    public void criarClienteSimples(Cliente cliente) throws SQLException {
        String sql = "INSERT INTO clientes (id_usuario_ref, telefone, data_nascimento) VALUES (?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, cliente.getIdUsuarioRef());
            ps.setString(2, cliente.getTelefone());

            if (cliente.getDataNascimento() != null) {
                ps.setDate(3, new java.sql.Date(cliente.getDataNascimento().getTime()));
            } else {
                ps.setNull(3, Types.DATE);
            }

            ps.executeUpdate();
        }
    }

    public Cliente getClientePorUsuarioId(int idUsuario) {
        String sql = "SELECT * FROM clientes WHERE id_usuario_ref = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = ConnectionFactory.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idUsuario);
            rs = ps.executeQuery();

            if (rs.next()) {
                Cliente cliente = new Cliente();
                cliente.setIdCliente(rs.getInt("id_cliente"));
                cliente.setIdUsuarioRef(rs.getInt("id_usuario_ref"));
                cliente.setTelefone(rs.getString("telefone"));
                cliente.setDataNascimento(rs.getDate("data_nascimento"));
                return cliente;
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
        return null;
    }
}