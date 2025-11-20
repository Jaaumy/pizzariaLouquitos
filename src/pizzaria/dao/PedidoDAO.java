package pizzaria.dao;

import pizzaria.model.ItemCarrinho;
import pizzaria.model.Pedido;
import pizzaria.model.Pizza;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class PedidoDAO {

    public boolean salvarPedido(Pedido pedido) {

        String sqlPedido = "INSERT INTO pedidos (id_usuario_cliente, tipo_entrega, forma_pagamento, status_pedido, valor_total, id_endereco_ref, id_cupom_ref, valor_desconto) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        String sqlItem = "INSERT INTO itenspedido (id_pedido_ref, id_pizza_ref, quantidade) VALUES (?, ?, ?)";

        Connection conn = null;
        PreparedStatement psPedido = null;
        PreparedStatement psItem = null;
        ResultSet rsKeys = null;
        int idPedidoGerado = -1;

        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);

            psPedido = conn.prepareStatement(sqlPedido, Statement.RETURN_GENERATED_KEYS);
            psPedido.setInt(1, pedido.getIdUsuarioCliente());
            psPedido.setString(2, pedido.getTipoEntrega());
            psPedido.setString(3, pedido.getFormaPagamento());
            psPedido.setString(4, "Pendente");
            psPedido.setDouble(5, pedido.getValorTotal());

            if (pedido.getIdEnderecoEntrega() > 0) {
                psPedido.setInt(6, pedido.getIdEnderecoEntrega());
            } else {
                psPedido.setNull(6, Types.INTEGER);
            }

            if (pedido.getIdCupomRef() > 0) {
                psPedido.setInt(7, pedido.getIdCupomRef());
            } else {
                psPedido.setNull(7, Types.INTEGER);
            }

            psPedido.setDouble(8, pedido.getValorDesconto());

            psPedido.executeUpdate();

            rsKeys = psPedido.getGeneratedKeys();
            if (rsKeys.next()) {
                idPedidoGerado = rsKeys.getInt(1);
            } else {
                throw new SQLException("Falha ao obter ID do pedido.");
            }

            psItem = conn.prepareStatement(sqlItem);
            for (ItemCarrinho item : pedido.getItens()) {
                psItem.setInt(1, idPedidoGerado);
                psItem.setInt(2, item.getPizza().getIdPizza());
                psItem.setInt(3, item.getQuantidade());
                psItem.addBatch();
            }
            psItem.executeBatch();

            conn.commit();
            return true;

        } catch (SQLException e) {
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            JOptionPane.showMessageDialog(null, "Erro ao salvar pedido: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (rsKeys != null) rsKeys.close();
                if (psPedido != null) psPedido.close();
                if (psItem != null) psItem.close();
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Pedido> listarPedidosPorUsuario(int idUsuario) {
        List<Pedido> pedidos = new ArrayList<>();
        String sqlPedidos = "SELECT p.*, e.rua, e.numero, e.cep FROM pedidos p " +
                "LEFT JOIN enderecos e ON p.id_endereco_ref = e.id_endereco " +
                "WHERE p.id_usuario_cliente = ? ORDER BY p.data_pedido DESC";

        String sqlItens = "SELECT i.*, p.nome, p.preco FROM itenspedido i " +
                "JOIN pizzas p ON i.id_pizza_ref = p.id_pizza " +
                "WHERE i.id_pedido_ref = ?";

        Connection conn = null;
        PreparedStatement psPedidos = null;
        ResultSet rsPedidos = null;
        PreparedStatement psItens = null;
        ResultSet rsItens = null;

        try {
            conn = ConnectionFactory.getConnection();
            psPedidos = conn.prepareStatement(sqlPedidos);
            psPedidos.setInt(1, idUsuario);
            rsPedidos = psPedidos.executeQuery();

            while (rsPedidos.next()) {
                Pedido pedido = new Pedido();
                pedido.setIdPedido(rsPedidos.getInt("id_pedido"));
                pedido.setIdUsuarioCliente(rsPedidos.getInt("id_usuario_cliente"));
                pedido.setTipoEntrega(rsPedidos.getString("tipo_entrega"));
                pedido.setFormaPagamento(rsPedidos.getString("forma_pagamento"));
                pedido.setStatusPedido(rsPedidos.getString("status_pedido"));
                pedido.setValorTotal(rsPedidos.getDouble("valor_total"));
                pedido.setIdEnderecoEntrega(rsPedidos.getInt("id_endereco_ref"));
                pedido.setIdCupomRef(rsPedidos.getInt("id_cupom_ref"));
                pedido.setValorDesconto(rsPedidos.getDouble("valor_desconto"));

                if (pedido.getIdEnderecoEntrega() > 0) {
                    String endStr = String.format("%s, %s - CEP: %s", rsPedidos.getString("rua"), rsPedidos.getString("numero"), rsPedidos.getString("cep"));
                    pedido.setEnderecoCompleto(endStr);
                }

                psItens = conn.prepareStatement(sqlItens);
                psItens.setInt(1, pedido.getIdPedido());
                rsItens = psItens.executeQuery();

                ArrayList<ItemCarrinho> itens = new ArrayList<>();
                while (rsItens.next()) {
                    Pizza pizza = new Pizza();
                    pizza.setIdPizza(rsItens.getInt("id_pizza_ref"));
                    pizza.setNome(rsItens.getString("nome"));
                    pizza.setPreco(rsItens.getDouble("preco"));

                    int quantidade = rsItens.getInt("quantidade");
                    itens.add(new ItemCarrinho(pizza, quantidade));
                }
                pedido.setItens(itens);
                pedidos.add(pedido);

                if (rsItens != null) rsItens.close();
                if (psItens != null) psItens.close();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao listar pedidos: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rsPedidos != null) rsPedidos.close();
                if (psPedidos != null) psPedidos.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return pedidos;
    }

    public int contarTotalPedidos() {
        String sql = "SELECT COUNT(*) FROM pedidos";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = ConnectionFactory.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
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
        return 0;
    }

    public int contarTotalPizzasVendidas() {
        String sql = "SELECT SUM(quantidade) FROM itenspedido";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = ConnectionFactory.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
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
        return 0;
    }

    public double calcularFaturamentoTotal() {
        String sql = "SELECT SUM(valor_total) FROM pedidos WHERE status_pedido != 'Cancelado'";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = ConnectionFactory.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble(1);
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
        return 0.0;
    }

    public List<Pedido> listarTodosPedidos() {
        List<Pedido> pedidos = new ArrayList<>();
        String sql = "SELECT p.*, u.nome, e.rua, e.numero, e.cep FROM pedidos p " +
                "JOIN usuarios u ON p.id_usuario_cliente = u.id_usuario " +
                "LEFT JOIN enderecos e ON p.id_endereco_ref = e.id_endereco " +
                "ORDER BY p.data_pedido DESC";

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = ConnectionFactory.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Pedido pedido = new Pedido();
                pedido.setIdPedido(rs.getInt("id_pedido"));
                pedido.setIdUsuarioCliente(rs.getInt("id_usuario_cliente"));
                pedido.setTipoEntrega(rs.getString("tipo_entrega"));
                pedido.setFormaPagamento(rs.getString("forma_pagamento"));
                pedido.setStatusPedido(rs.getString("status_pedido"));
                pedido.setValorTotal(rs.getDouble("valor_total"));
                pedido.setIdEnderecoEntrega(rs.getInt("id_endereco_ref"));
                pedido.setIdCupomRef(rs.getInt("id_cupom_ref"));
                pedido.setValorDesconto(rs.getDouble("valor_desconto"));

                if (pedido.getIdEnderecoEntrega() > 0) {
                    String endStr = String.format("%s, %s - CEP: %s", rs.getString("rua"), rs.getString("numero"), rs.getString("cep"));
                    pedido.setEnderecoCompleto(endStr);
                }

                pedidos.add(pedido);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao listar todos os pedidos: " + e.getMessage());
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
        return pedidos;
    }

    public void atualizarStatusPedido(int idPedido, String novoStatus) {
        String sql = "UPDATE pedidos SET status_pedido = ? WHERE id_pedido = ?";
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = ConnectionFactory.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, novoStatus);
            ps.setInt(2, idPedido);

            ps.executeUpdate();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao atualizar status: " + e.getMessage());
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

    public Pedido getPedidoAtivo(int idUsuario) {
        String sql = "SELECT * FROM pedidos WHERE id_usuario_cliente = ? " +
                "AND status_pedido IN ('Pendente', 'Em Preparo') " +
                "ORDER BY data_pedido DESC LIMIT 1";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = ConnectionFactory.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idUsuario);
            rs = ps.executeQuery();

            if (rs.next()) {
                Pedido pedido = new Pedido();
                pedido.setIdPedido(rs.getInt("id_pedido"));
                pedido.setStatusPedido(rs.getString("status_pedido"));
                return pedido;
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