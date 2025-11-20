package pizzaria.view;

import pizzaria.dao.CupomDAO;
import pizzaria.dao.EnderecoDAO;
import pizzaria.dao.PedidoDAO;
import pizzaria.model.Cupom;
import pizzaria.model.Endereco;
import pizzaria.model.ItemCarrinho;
import pizzaria.model.Pedido;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class PainelCarrinho extends JPanel {

    private MainFrame mainFrame;
    private JTable tabelaCarrinho;
    private DefaultTableModel tableModel;
    private JLabel lblNomeCliente;
    private JLabel lblValorTotal;
    private JRadioButton radioRetirada;
    private JRadioButton radioEntrega;
    private JComboBox<String> comboPagamento;
    private PedidoDAO pedidoDAO;
    private EnderecoDAO enderecoDAO;
    private CupomDAO cupomDAO;

    private JPanel painelSelecaoEndereco;
    private JComboBox<Endereco> comboEnderecos;

    private JTextField txtCupom;
    private JButton btnAplicarCupom;
    private JLabel lblDesconto;
    private Cupom cupomAplicado;
    private double valorDesconto;

    public PainelCarrinho(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.pedidoDAO = new PedidoDAO();
        this.enderecoDAO = new EnderecoDAO();
        this.cupomDAO = new CupomDAO();
        setLayout(new BorderLayout(10, 10));
        setBackground(TemaLouquitos.CINZA_CLARO);
        setBorder(new EmptyBorder(10, 10, 10, 10));

        criarHeader();
        criarTabela();
        criarPainelFinalizacao();
    }

    private void criarHeader() {
        JPanel painelHeader = new JPanel(new BorderLayout());
        painelHeader.setOpaque(false);

        JLabel lblTitulo = new JLabel("Meu Carrinho");
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

    private void criarTabela() {
        String[] colunas = {"Pizza", "Quantidade", "Preço Unit.", "Subtotal"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabelaCarrinho = new JTable(tableModel);
        tabelaCarrinho.setFont(new Font("Arial", Font.PLAIN, 14));
        tabelaCarrinho.setRowHeight(25);
        tabelaCarrinho.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
        tabelaCarrinho.getTableHeader().setBackground(TemaLouquitos.VERDE_PIZZARIA);
        tabelaCarrinho.getTableHeader().setForeground(TemaLouquitos.BRANCO_PADRAO);

        JScrollPane scrollPane = new JScrollPane(tabelaCarrinho);
        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel criarPainelSelecaoEndereco() {
        painelSelecaoEndereco = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelSelecaoEndereco.setOpaque(false);
        painelSelecaoEndereco.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(TemaLouquitos.VERDE_PIZZARIA),
                "Dados de Entrega",
                0, 0, new Font("Arial", Font.BOLD, 14), TemaLouquitos.VERDE_PIZZARIA
        ));

        comboEnderecos = new JComboBox<>();
        comboEnderecos.setFont(new Font("Arial", Font.PLAIN, 14));

        JButton btnNovoEndereco = new JButton("Cadastrar Novo");
        btnNovoEndereco.setFont(new Font("Arial", Font.BOLD, 12));
        btnNovoEndereco.setBackground(TemaLouquitos.CINZA_ESCURO);
        btnNovoEndereco.setForeground(TemaLouquitos.BRANCO_PADRAO);

        btnNovoEndereco.addActionListener(e -> mainFrame.showPanel("Enderecos"));

        painelSelecaoEndereco.add(new JLabel("Selecione o Endereço:"));
        painelSelecaoEndereco.add(comboEnderecos);
        painelSelecaoEndereco.add(btnNovoEndereco);

        painelSelecaoEndereco.setVisible(false);
        return painelSelecaoEndereco;
    }

    private JPanel criarPainelCupom() {
        JPanel painelCupom = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelCupom.setOpaque(false);

        painelCupom.add(new JLabel("Cupom de Desconto:")).setFont(new Font("Arial", Font.BOLD, 16));
        txtCupom = new JTextField(10);
        txtCupom.setFont(new Font("Arial", Font.PLAIN, 16));

        btnAplicarCupom = new JButton("Aplicar");
        btnAplicarCupom.setFont(new Font("Arial", Font.BOLD, 12));
        btnAplicarCupom.setBackground(TemaLouquitos.VERDE_PIZZARIA);
        btnAplicarCupom.setForeground(TemaLouquitos.BRANCO_PADRAO);

        lblDesconto = new JLabel();
        lblDesconto.setFont(new Font("Arial", Font.BOLD, 14));
        lblDesconto.setForeground(TemaLouquitos.VERMELHO_PIZZARIA);

        btnAplicarCupom.addActionListener(e -> aplicarCupom());

        painelCupom.add(txtCupom);
        painelCupom.add(btnAplicarCupom);
        painelCupom.add(lblDesconto);

        return painelCupom;
    }

    private void criarPainelFinalizacao() {
        JPanel painelSul = new JPanel();
        painelSul.setLayout(new BoxLayout(painelSul, BoxLayout.Y_AXIS));
        painelSul.setOpaque(false);

        JPanel painelOpcoes = new JPanel(new GridLayout(0, 2, 10, 10));
        painelOpcoes.setOpaque(false);
        painelOpcoes.setBorder(new EmptyBorder(10, 0, 0, 0));

        lblNomeCliente = new JLabel("Cliente: ");
        lblNomeCliente.setFont(new Font("Arial", Font.BOLD, 16));

        radioRetirada = new JRadioButton("Peça e Retire");
        radioRetirada.setFont(new Font("Arial", Font.PLAIN, 16));
        radioRetirada.setOpaque(false);
        radioRetirada.setSelected(true);

        radioEntrega = new JRadioButton("Entrega");
        radioEntrega.setFont(new Font("Arial", Font.PLAIN, 16));
        radioEntrega.setOpaque(false);

        ButtonGroup grupoEntrega = new ButtonGroup();
        grupoEntrega.add(radioRetirada);
        grupoEntrega.add(radioEntrega);

        JPanel painelEntrega = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelEntrega.setOpaque(false);
        painelEntrega.add(new JLabel("Entrega:")).setFont(new Font("Arial", Font.BOLD, 16));
        painelEntrega.add(radioRetirada);
        painelEntrega.add(radioEntrega);

        String[] formasPagamento = {"Cartão", "PIX", "Dinheiro"};
        comboPagamento = new JComboBox<>(formasPagamento);
        comboPagamento.setFont(new Font("Arial", Font.PLAIN, 16));

        JPanel painelPagamento = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelPagamento.setOpaque(false);
        painelPagamento.add(new JLabel("Pagamento:")).setFont(new Font("Arial", Font.BOLD, 16));
        painelPagamento.add(comboPagamento);

        painelOpcoes.add(lblNomeCliente);
        painelOpcoes.add(painelEntrega);
        painelOpcoes.add(criarPainelCupom());
        painelOpcoes.add(painelPagamento);

        radioRetirada.addActionListener(e -> painelSelecaoEndereco.setVisible(false));
        radioEntrega.addActionListener(e -> {
            painelSelecaoEndereco.setVisible(true);
            carregarEnderecos();
        });

        JPanel painelTotal = new JPanel(new BorderLayout());
        painelTotal.setOpaque(false);
        painelTotal.setBorder(new EmptyBorder(10, 0, 0, 0));

        lblValorTotal = new JLabel("Total: R$ 0,00");
        lblValorTotal.setFont(new Font("Arial", Font.BOLD, 22));
        lblValorTotal.setForeground(TemaLouquitos.VERDE_PIZZARIA);

        JButton btnFinalizar = new JButton("Finalizar Pedido");
        btnFinalizar.setFont(new Font("Arial", Font.BOLD, 20));
        btnFinalizar.setBackground(TemaLouquitos.VERMELHO_PIZZARIA);
        btnFinalizar.setForeground(TemaLouquitos.BRANCO_PADRAO);
        btnFinalizar.setFocusPainted(false);

        btnFinalizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                finalizarPedido();
            }
        });

        painelTotal.add(lblValorTotal, BorderLayout.WEST);
        painelTotal.add(btnFinalizar, BorderLayout.EAST);

        painelSul.add(painelOpcoes);
        painelSul.add(criarPainelSelecaoEndereco());
        painelSul.add(painelTotal);

        add(painelSul, BorderLayout.SOUTH);
    }

    public void carregarCarrinho() {
        tableModel.setRowCount(0);
        cupomAplicado = null;
        valorDesconto = 0;
        lblDesconto.setText("");
        txtCupom.setText("");

        if (mainFrame.getUsuarioLogado() == null) return;

        lblNomeCliente.setText("Cliente: " + mainFrame.getUsuarioLogado().getNome());

        ArrayList<ItemCarrinho> carrinho = mainFrame.getCarrinho();
        double total = 0;

        for (ItemCarrinho item : carrinho) {
            double subtotal = item.getSubtotal();
            tableModel.addRow(new Object[]{
                    item.getPizza().getNome(),
                    item.getQuantidade(),
                    String.format("%.2f", item.getPizza().getPreco()),
                    String.format("%.2f", subtotal)
            });
            total += subtotal;
        }

        atualizarValorTotal();

        if (radioEntrega.isSelected()) {
            carregarEnderecos();
        }
    }

    private void carregarEnderecos() {
        if (mainFrame.getClienteLogado() == null) return;

        comboEnderecos.removeAllItems();
        List<Endereco> enderecos = enderecoDAO.listarEnderecos(mainFrame.getClienteLogado().getIdCliente());

        if (enderecos.isEmpty()) {
            comboEnderecos.addItem(null);
            comboEnderecos.setEnabled(false);
        } else {
            for (Endereco end : enderecos) {
                comboEnderecos.addItem(end);
            }
            comboEnderecos.setEnabled(true);
        }
    }

    private void aplicarCupom() {
        String codigo = txtCupom.getText();
        if (codigo.isEmpty()) {
            return;
        }

        Cupom cupom = cupomDAO.validarCupom(codigo);

        if (cupom != null) {
            cupomAplicado = cupom;
            double subtotal = mainFrame.getValorTotalCarrinho();

            if (cupom.getTipoDesconto().equals("porcentagem")) {
                valorDesconto = subtotal * (cupom.getValor() / 100.0);
                lblDesconto.setText(String.format("- R$ %.2f (%.0f%%)", valorDesconto, cupom.getValor()));
            } else {
                valorDesconto = cupom.getValor();
                lblDesconto.setText(String.format("- R$ %.2f", valorDesconto));
            }

            if (valorDesconto > subtotal) {
                valorDesconto = subtotal;
            }

            JOptionPane.showMessageDialog(this, "Cupom aplicado com sucesso!");
        } else {
            cupomAplicado = null;
            valorDesconto = 0;
            lblDesconto.setText("");
            JOptionPane.showMessageDialog(this, "Cupom inválido ou expirado.", "Erro", JOptionPane.WARNING_MESSAGE);
        }
        atualizarValorTotal();
    }

    private void atualizarValorTotal() {
        double total = mainFrame.getValorTotalCarrinho();
        double totalFinal = total - valorDesconto;

        if (valorDesconto > 0) {
            lblValorTotal.setText(String.format("<html>Total: R$ %.2f <s style='color:red;'>R$ %.2f</s></html>", totalFinal, total));
        } else {
            lblValorTotal.setText(String.format("Total: R$ %.2f", totalFinal));
        }
    }

    private void finalizarPedido() {
        if (mainFrame.getCarrinho().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seu carrinho está vazio!", "Erro", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String tipoEntrega = radioRetirada.isSelected() ? "Retirada" : "Entrega";

        Pedido pedido = new Pedido();

        if (tipoEntrega.equals("Entrega")) {
            Endereco endSelecionado = (Endereco) comboEnderecos.getSelectedItem();
            if (endSelecionado == null) {
                JOptionPane.showMessageDialog(this, "Por favor, selecione um endereço para entrega ou cadastre um novo.", "Erro", JOptionPane.WARNING_MESSAGE);
                return;
            }
            pedido.setIdEnderecoEntrega(endSelecionado.getIdEndereco());
        }

        pedido.setIdUsuarioCliente(mainFrame.getUsuarioLogado().getIdUsuario());
        pedido.setItens(mainFrame.getCarrinho());

        double totalFinal = mainFrame.getValorTotalCarrinho() - valorDesconto;
        pedido.setValorTotal(totalFinal);
        pedido.setValorDesconto(valorDesconto);
        if (cupomAplicado != null) {
            pedido.setIdCupomRef(cupomAplicado.getIdCupom());
        }

        pedido.setTipoEntrega(tipoEntrega);

        String formaPagamento = (String) comboPagamento.getSelectedItem();
        pedido.setFormaPagamento(formaPagamento);

        boolean sucesso = pedidoDAO.salvarPedido(pedido);

        if (sucesso) {
            JOptionPane.showMessageDialog(this,
                    "Pedido finalizado com sucesso!\nStatus: Pendente\nTipo: " + tipoEntrega,
                    "Pedido Enviado",
                    JOptionPane.INFORMATION_MESSAGE);
            mainFrame.limparCarrinho();
            mainFrame.showPanel("Cliente");
        } else {
            JOptionPane.showMessageDialog(this, "Houve um erro ao processar seu pedido.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}