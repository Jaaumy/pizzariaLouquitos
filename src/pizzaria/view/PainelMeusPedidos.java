package pizzaria.view;

import pizzaria.dao.AvaliacaoDAO;
import pizzaria.dao.PedidoDAO;
import pizzaria.model.Avaliacao;
import pizzaria.model.ItemCarrinho;
import pizzaria.model.Pedido;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class PainelMeusPedidos extends JPanel {

    private MainFrame mainFrame;
    private PedidoDAO pedidoDAO;
    private AvaliacaoDAO avaliacaoDAO;
    private JPanel painelListaPedidos;

    public PainelMeusPedidos(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.pedidoDAO = new PedidoDAO();
        this.avaliacaoDAO = new AvaliacaoDAO();

        setLayout(new BorderLayout(10, 10));
        setBackground(TemaLouquitos.CINZA_CLARO);
        setBorder(new EmptyBorder(10, 10, 10, 10));

        criarHeader();

        painelListaPedidos = new JPanel();
        painelListaPedidos.setLayout(new BoxLayout(painelListaPedidos, BoxLayout.Y_AXIS));
        painelListaPedidos.setBackground(TemaLouquitos.BRANCO_PADRAO);

        JScrollPane scrollPane = new JScrollPane(painelListaPedidos);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(scrollPane, BorderLayout.CENTER);
    }

    private void criarHeader() {
        JPanel painelHeader = new JPanel(new BorderLayout());
        painelHeader.setOpaque(false);

        JLabel lblTitulo = new JLabel("Meus Pedidos");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 28));
        lblTitulo.setForeground(TemaLouquitos.VERMELHO_PIZZARIA);

        JButton btnVoltar = new JButton("Voltar ao Cardápio");
        btnVoltar.setFont(new Font("Arial", Font.BOLD, 14));
        btnVoltar.setBackground(TemaLouquitos.CINZA_ESCURO);
        btnVoltar.setForeground(TemaLouquitos.BRANCO_PADRAO);
        btnVoltar.setFocusPainted(false);

        btnVoltar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.showPanel("Cliente");
            }
        });

        painelHeader.add(lblTitulo, BorderLayout.WEST);
        painelHeader.add(btnVoltar, BorderLayout.EAST);
        add(painelHeader, BorderLayout.NORTH);
    }

    public void carregarPedidos() {
        painelListaPedidos.removeAll();

        if (mainFrame.getUsuarioLogado() == null) {
            return;
        }

        int idUsuario = mainFrame.getUsuarioLogado().getIdUsuario();
        List<Pedido> pedidos = pedidoDAO.listarPedidosPorUsuario(idUsuario);

        if (pedidos.isEmpty()) {
            JLabel lblVazio = new JLabel("Você ainda não fez nenhum pedido.");
            lblVazio.setFont(new Font("Arial", Font.PLAIN, 18));
            lblVazio.setHorizontalAlignment(SwingConstants.CENTER);
            painelListaPedidos.add(lblVazio);
        } else {
            for (Pedido pedido : pedidos) {
                painelListaPedidos.add(criarPainelPedido(pedido));
                painelListaPedidos.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        }

        painelListaPedidos.revalidate();
        painelListaPedidos.repaint();
    }

    private void abrirDialogAvaliacao(ItemCarrinho item) {
        if (mainFrame.getClienteLogado() == null) return;

        JPanel painel = new JPanel(new GridLayout(0, 1, 5, 5));
        painel.add(new JLabel("Qual sua nota para " + item.getPizza().getNome() + "?"));

        JComboBox<Integer> comboNota = new JComboBox<>(new Integer[]{5, 4, 3, 2, 1});
        painel.add(comboNota);

        painel.add(new JLabel("Comentário (opcional):"));
        JTextArea txtComentario = new JTextArea(3, 20);
        painel.add(new JScrollPane(txtComentario));

        int result = JOptionPane.showConfirmDialog(mainFrame, painel, "Avaliar Pizza",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            Avaliacao a = new Avaliacao();
            a.setIdClienteRef(mainFrame.getClienteLogado().getIdCliente());
            a.setIdPizzaRef(item.getPizza().getIdPizza());
            a.setNota((Integer) comboNota.getSelectedItem());
            a.setComentario(txtComentario.getText());

            avaliacaoDAO.salvarAvaliacao(a);
        }
    }

    private JPanel criarPainelPedido(Pedido pedido) {
        JPanel painelPedido = new JPanel();
        painelPedido.setLayout(new BoxLayout(painelPedido, BoxLayout.Y_AXIS));
        painelPedido.setBackground(TemaLouquitos.CINZA_CLARO);
        painelPedido.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(TemaLouquitos.VERDE_PIZZARIA, 1),
                new EmptyBorder(15, 15, 15, 15)
        ));
        painelPedido.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblIdPedido = new JLabel(String.format("Pedido #%d - Status: %s", pedido.getIdPedido(), pedido.getStatusPedido()));
        lblIdPedido.setFont(new Font("Arial", Font.BOLD, 20));
        lblIdPedido.setForeground(TemaLouquitos.VERMELHO_PIZZARIA);

        String detalhes = String.format("Pagamento: %s | Entrega: %s", pedido.getFormaPagamento(), pedido.getTipoEntrega());
        if (pedido.getTipoEntrega().equals("Entrega") && pedido.getEnderecoCompleto() != null) {
            detalhes += " (" + pedido.getEnderecoCompleto() + ")";
        }

        JLabel lblDetalhes = new JLabel(detalhes);
        lblDetalhes.setFont(new Font("Arial", Font.ITALIC, 14));

        JSeparator separator = new JSeparator();
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 10));

        painelPedido.add(lblIdPedido);
        painelPedido.add(Box.createRigidArea(new Dimension(0, 5)));
        painelPedido.add(lblDetalhes);
        painelPedido.add(Box.createRigidArea(new Dimension(0, 10)));
        painelPedido.add(separator);
        painelPedido.add(Box.createRigidArea(new Dimension(0, 10)));

        for (ItemCarrinho item : pedido.getItens()) {
            JPanel painelItem = new JPanel(new BorderLayout());
            painelItem.setOpaque(false);

            JLabel lblItem = new JLabel(String.format("%dx %s (R$ %.2f)",
                    item.getQuantidade(),
                    item.getPizza().getNome(),
                    item.getSubtotal()));
            lblItem.setFont(new Font("Arial", Font.PLAIN, 14));
            painelItem.add(lblItem, BorderLayout.CENTER);

            if (pedido.getStatusPedido().equals("Concluído")) {
                JButton btnAvaliar = new JButton("Avaliar");
                btnAvaliar.setFont(new Font("Arial", Font.PLAIN, 10));
                btnAvaliar.addActionListener(e -> abrirDialogAvaliacao(item));
                painelItem.add(btnAvaliar, BorderLayout.EAST);
            }

            painelPedido.add(painelItem);
        }

        painelPedido.add(Box.createVerticalGlue());
        painelPedido.add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel lblTotal = new JLabel(String.format("Total: R$ %.2f", pedido.getValorTotal()));
        if (pedido.getValorDesconto() > 0) {
            lblTotal.setText(String.format("<html>Total: R$ %.2f <s style='color:red;'>R$ %.2f</s></html>",
                    pedido.getValorTotal(),
                    pedido.getValorTotal() + pedido.getValorDesconto()));
        }
        lblTotal.setFont(new Font("Arial", Font.BOLD, 18));
        lblTotal.setForeground(TemaLouquitos.VERDE_PIZZARIA);
        painelPedido.add(lblTotal);

        return painelPedido;
    }
}