package pizzaria.view;

import pizzaria.dao.AvaliacaoDAO;
import pizzaria.dao.CupomDAO;
import pizzaria.dao.PedidoDAO;
import pizzaria.dao.PizzaDAO;
import pizzaria.dao.UsuarioDAO;
import pizzaria.model.Avaliacao;
import pizzaria.model.Cupom;
import pizzaria.model.Pedido;
import pizzaria.model.Pizza;
import pizzaria.model.Usuario;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.List;

public class PainelAdmin extends JPanel {

    private MainFrame mainFrame;
    private PedidoDAO pedidoDAO;
    private UsuarioDAO usuarioDAO;
    private PizzaDAO pizzaDAO;
    private CupomDAO cupomDAO;
    private AvaliacaoDAO avaliacaoDAO;

    private JLabel lblNomeAdmin;
    private JTabbedPane tabbedPane;

    private JLabel lblTotalVendas;
    private JLabel lblTotalPedidos;
    private JLabel lblTotalPizzas;
    private JLabel lblTotalClientes;

    private DefaultTableModel pedidosTableModel;
    private JTable tabelaPedidos;
    private DefaultTableModel clientesTableModel;
    private JTable tabelaClientes;
    private DefaultTableModel cuponsTableModel;
    private JTable tabelaCupons;
    private DefaultTableModel avaliacoesTableModel;
    private JTable tabelaAvaliacoes;

    private JPanel painelControleStatus;
    private JLabel lblPedidoSelecionado;
    private JComboBox<String> comboStatus;
    private JButton btnSalvarStatus;
    private int idPedidoSelecionado = -1;

    public PainelAdmin(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.pedidoDAO = new PedidoDAO();
        this.usuarioDAO = new UsuarioDAO();
        this.pizzaDAO = new PizzaDAO();
        this.cupomDAO = new CupomDAO();
        this.avaliacaoDAO = new AvaliacaoDAO();

        setLayout(new BorderLayout());
        setBackground(TemaLouquitos.CINZA_CLARO);

        criarHeader();
        criarAbas();
    }

    private void criarHeader() {
        JPanel painelHeader = new JPanel(new BorderLayout());
        painelHeader.setBackground(TemaLouquitos.CINZA_ESCURO);
        painelHeader.setBorder(new EmptyBorder(10, 20, 10, 20));

        JLabel lblTitulo = new JLabel("Painel Administrativo");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setForeground(TemaLouquitos.BRANCO_PADRAO);
        painelHeader.add(lblTitulo, BorderLayout.WEST);

        JPanel painelUsuario = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        painelUsuario.setOpaque(false);

        lblNomeAdmin = new JLabel("Olá, Admin");
        lblNomeAdmin.setFont(new Font("Arial", Font.BOLD, 16));
        lblNomeAdmin.setForeground(TemaLouquitos.BRANCO_PADRAO);

        JButton btnSair = new JButton("Sair");
        btnSair.setFont(new Font("Arial", Font.BOLD, 14));
        btnSair.setBackground(TemaLouquitos.VERMELHO_PIZZARIA);
        btnSair.setForeground(TemaLouquitos.BRANCO_PADRAO);
        btnSair.setFocusPainted(false);

        painelUsuario.add(lblNomeAdmin);
        painelUsuario.add(new JSeparator(SwingConstants.VERTICAL));
        painelUsuario.add(btnSair);

        painelHeader.add(painelUsuario, BorderLayout.EAST);
        add(painelHeader, BorderLayout.NORTH);

        btnSair.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.fazerLogout();
            }
        });
    }

    private void criarAbas() {
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 16));

        JPanel abaDashboard = criarAbaDashboard();
        JPanel abaPedidos = criarAbaPedidos();
        JPanel abaPizzas = criarAbaPizzas();
        JPanel abaClientes = criarAbaClientes();
        JPanel abaCupons = criarAbaCupons();
        JPanel abaAvaliacoes = criarAbaAvaliacoes();

        tabbedPane.addTab("Dashboard", abaDashboard);
        tabbedPane.addTab("Pedidos", abaPedidos);
        tabbedPane.addTab("Catálogo", abaPizzas);
        tabbedPane.addTab("Clientes", abaClientes);
        tabbedPane.addTab("Cupons", abaCupons);
        tabbedPane.addTab("Avaliações", abaAvaliacoes);

        add(tabbedPane, BorderLayout.CENTER);

        tabbedPane.addChangeListener(e -> {
            int index = tabbedPane.getSelectedIndex();
            if (index == 0) {
                carregarDashboard();
            } else if (index == 1) {
                carregarTodosPedidos();
            } else if (index == 3) {
                carregarTodosClientes();
            } else if (index == 4) {
                carregarTodosCupons();
            } else if (index == 5) {
                carregarTodasAvaliacoes();
            }
        });
    }

    private JPanel criarAbaDashboard() {
        JPanel painel = new JPanel(new GridLayout(2, 2, 20, 20));
        painel.setBorder(new EmptyBorder(20, 20, 20, 20));
        painel.setBackground(TemaLouquitos.BRANCO_PADRAO);

        lblTotalVendas = criarCaixaInfo("Faturamento Total", "R$ 0,00", TemaLouquitos.VERDE_PIZZARIA);
        lblTotalPedidos = criarCaixaInfo("Total de Pedidos", "0", TemaLouquitos.VERMELHO_PIZZARIA);
        lblTotalPizzas = criarCaixaInfo("Total de Pizzas Vendidas", "0", TemaLouquitos.VERDE_PIZZARIA);
        lblTotalClientes = criarCaixaInfo("Total de Clientes", "0", TemaLouquitos.VERMELHO_PIZZARIA);

        painel.add(lblTotalVendas);
        painel.add(lblTotalPedidos);
        painel.add(lblTotalPizzas);
        painel.add(lblTotalClientes);

        return painel;
    }

    private JPanel criarAbaPedidos() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(new EmptyBorder(10, 10, 10, 10));
        painel.setBackground(TemaLouquitos.BRANCO_PADRAO);

        String[] colunas = {"ID", "Cliente(ID)", "Status", "Tipo", "Pagamento", "Desconto", "Total", "Endereço"};
        pedidosTableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabelaPedidos = new JTable(pedidosTableModel);
        tabelaPedidos.setFont(new Font("Arial", Font.PLAIN, 14));
        tabelaPedidos.setRowHeight(25);
        tabelaPedidos.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
        tabelaPedidos.getTableHeader().setBackground(TemaLouquitos.VERDE_PIZZARIA);
        tabelaPedidos.getTableHeader().setForeground(TemaLouquitos.BRANCO_PADRAO);
        tabelaPedidos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        tabelaPedidos.getColumnModel().getColumn(0).setMaxWidth(50);
        tabelaPedidos.getColumnModel().getColumn(1).setMaxWidth(100);
        tabelaPedidos.getColumnModel().getColumn(2).setMaxWidth(100);
        tabelaPedidos.getColumnModel().getColumn(3).setMaxWidth(80);

        JScrollPane scrollPane = new JScrollPane(tabelaPedidos);
        painel.add(scrollPane, BorderLayout.CENTER);

        painelControleStatus = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelControleStatus.setBackground(TemaLouquitos.CINZA_CLARO);
        painelControleStatus.setBorder(BorderFactory.createTitledBorder("Alterar Status do Pedido"));

        lblPedidoSelecionado = new JLabel("Nenhum pedido selecionado");
        lblPedidoSelecionado.setFont(new Font("Arial", Font.BOLD, 14));

        String[] statusOptions = {"Pendente", "Em Preparo", "Concluído", "Cancelado"};
        comboStatus = new JComboBox<>(statusOptions);
        comboStatus.setFont(new Font("Arial", Font.PLAIN, 14));

        btnSalvarStatus = new JButton("Salvar Status");
        btnSalvarStatus.setFont(new Font("Arial", Font.BOLD, 14));
        btnSalvarStatus.setBackground(TemaLouquitos.VERDE_PIZZARIA);
        btnSalvarStatus.setForeground(TemaLouquitos.BRANCO_PADRAO);

        painelControleStatus.add(lblPedidoSelecionado);
        painelControleStatus.add(new JSeparator(SwingConstants.VERTICAL));
        painelControleStatus.add(comboStatus);
        painelControleStatus.add(btnSalvarStatus);

        painelControleStatus.setVisible(false);
        painel.add(painelControleStatus, BorderLayout.SOUTH);

        tabelaPedidos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = tabelaPedidos.getSelectedRow();
                if (selectedRow != -1) {
                    idPedidoSelecionado = (int) pedidosTableModel.getValueAt(selectedRow, 0);
                    String statusAtual = (String) pedidosTableModel.getValueAt(selectedRow, 2);

                    lblPedidoSelecionado.setText(String.format("Pedido #%d:", idPedidoSelecionado));
                    comboStatus.setSelectedItem(statusAtual);
                    painelControleStatus.setVisible(true);
                }
            }
        });

        btnSalvarStatus.addActionListener(e -> salvarNovoStatus());

        return painel;
    }

    private void salvarNovoStatus() {
        if (idPedidoSelecionado == -1) {
            return;
        }

        String novoStatus = (String) comboStatus.getSelectedItem();
        pedidoDAO.atualizarStatusPedido(idPedidoSelecionado, novoStatus);

        JOptionPane.showMessageDialog(this, "Status do Pedido #" + idPedidoSelecionado + " atualizado para " + novoStatus);

        carregarTodosPedidos();
        painelControleStatus.setVisible(false);
        idPedidoSelecionado = -1;
    }

    private JPanel criarAbaPizzas() {
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBorder(new EmptyBorder(20, 20, 20, 20));
        painel.setBackground(TemaLouquitos.BRANCO_PADRAO);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        JLabel lblNome = new JLabel("Nome da Pizza:");
        gbc.gridx = 0; gbc.gridy = 0;
        painel.add(lblNome, gbc);

        JTextField txtNome = new JTextField(30);
        gbc.gridx = 1; gbc.gridy = 0;
        painel.add(txtNome, gbc);

        JLabel lblDescricao = new JLabel("Descrição (Ingredientes):");
        gbc.gridx = 0; gbc.gridy = 1;
        painel.add(lblDescricao, gbc);

        JTextArea txtDescricao = new JTextArea(3, 30);
        txtDescricao.setLineWrap(true);
        txtDescricao.setWrapStyleWord(true);
        JScrollPane scrollDesc = new JScrollPane(txtDescricao);
        gbc.gridx = 1; gbc.gridy = 1;
        painel.add(scrollDesc, gbc);

        JLabel lblPreco = new JLabel("Preço (ex: 45.50):");
        gbc.gridx = 0; gbc.gridy = 2;
        painel.add(lblPreco, gbc);

        JTextField txtPreco = new JTextField(10);
        gbc.gridx = 1; gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        painel.add(txtPreco, gbc);

        JLabel lblImagem = new JLabel("Nome Imagem (ex: nome.png):");
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        painel.add(lblImagem, gbc);

        JTextField txtImagem = new JTextField(30);
        gbc.gridx = 1; gbc.gridy = 3;
        painel.add(txtImagem, gbc);

        JButton btnSalvarPizza = new JButton("Adicionar ao Cardápio");
        btnSalvarPizza.setFont(new Font("Arial", Font.BOLD, 16));
        btnSalvarPizza.setBackground(TemaLouquitos.VERDE_PIZZARIA);
        btnSalvarPizza.setForeground(TemaLouquitos.BRANCO_PADRAO);
        gbc.gridx = 1; gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        painel.add(btnSalvarPizza, gbc);

        btnSalvarPizza.addActionListener(e -> {
            String nome = txtNome.getText();
            String descricao = txtDescricao.getText();
            String precoStr = txtPreco.getText();
            String imagem = txtImagem.getText();

            if (nome.isEmpty() || descricao.isEmpty() || precoStr.isEmpty()) {
                JOptionPane.showMessageDialog(painel, "Nome, Descrição e Preço são obrigatórios.", "Erro", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (imagem.isEmpty()) {
                imagem = "padrao.png";
            }

            try {
                double preco = Double.parseDouble(precoStr);
                Pizza novaPizza = new Pizza();
                novaPizza.setNome(nome);
                novaPizza.setDescricao(descricao);
                novaPizza.setPreco(preco);
                novaPizza.setImagem(imagem);

                pizzaDAO.adicionarPizza(novaPizza);

                txtNome.setText("");
                txtDescricao.setText("");
                txtPreco.setText("");
                txtImagem.setText("");

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(painel, "Preço inválido. Use ponto (ex: 45.50)", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        return painel;
    }

    private JPanel criarAbaClientes() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(new EmptyBorder(10, 10, 10, 10));
        painel.setBackground(TemaLouquitos.BRANCO_PADRAO);

        String[] colunas = {"ID Cliente", "Nome", "E-mail", "Telefone"};
        clientesTableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabelaClientes = new JTable(clientesTableModel);
        tabelaClientes.setFont(new Font("Arial", Font.PLAIN, 14));
        tabelaClientes.setRowHeight(25);
        tabelaClientes.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
        tabelaClientes.getTableHeader().setBackground(TemaLouquitos.VERDE_PIZZARIA);
        tabelaClientes.getTableHeader().setForeground(TemaLouquitos.BRANCO_PADRAO);

        tabelaClientes.getColumnModel().getColumn(0).setMaxWidth(100);

        JScrollPane scrollPane = new JScrollPane(tabelaClientes);
        painel.add(scrollPane, BorderLayout.CENTER);

        JButton btnRefresh = new JButton("Atualizar Lista");
        btnRefresh.setFont(new Font("Arial", Font.BOLD, 14));
        btnRefresh.setBackground(TemaLouquitos.CINZA_ESCURO);
        btnRefresh.setForeground(TemaLouquitos.BRANCO_PADRAO);
        btnRefresh.addActionListener(e -> carregarTodosClientes());

        painel.add(btnRefresh, BorderLayout.SOUTH);

        return painel;
    }

    private JPanel criarAbaCupons() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(new EmptyBorder(10, 10, 10, 10));
        painel.setBackground(TemaLouquitos.BRANCO_PADRAO);

        String[] colunas = {"ID", "Código", "Tipo", "Valor", "Validade", "Ativo"};
        cuponsTableModel = new DefaultTableModel(colunas, 0);
        tabelaCupons = new JTable(cuponsTableModel);
        painel.add(new JScrollPane(tabelaCupons), BorderLayout.CENTER);

        JPanel painelForm = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField txtCodigo = new JTextField(10);
        JComboBox<String> comboTipo = new JComboBox<>(new String[]{"porcentagem", "fixo"});
        JTextField txtValor = new JTextField(5);
        JTextField txtValidade = new JTextField(10);
        JButton btnAddCupom = new JButton("Adicionar Cupom");

        painelForm.add(new JLabel("Código:"));
        painelForm.add(txtCodigo);
        painelForm.add(new JLabel("Tipo:"));
        painelForm.add(comboTipo);
        painelForm.add(new JLabel("Valor:"));
        painelForm.add(txtValor);
        painelForm.add(new JLabel("Validade (AAAA-MM-DD):"));
        painelForm.add(txtValidade);
        painelForm.add(btnAddCupom);

        btnAddCupom.addActionListener(e -> {
            try {
                Cupom cupom = new Cupom();
                cupom.setCodigo(txtCodigo.getText());
                cupom.setTipoDesconto((String) comboTipo.getSelectedItem());
                cupom.setValor(Double.parseDouble(txtValor.getText()));
                if (!txtValidade.getText().isEmpty()) {
                    cupom.setDataValidade(java.sql.Date.valueOf(txtValidade.getText()));
                }
                cupom.setAtivo(true);
                cupomDAO.adicionarCupom(cupom);
                carregarTodosCupons();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(painel, "Erro nos dados: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        painel.add(painelForm, BorderLayout.SOUTH);
        return painel;
    }

    private JPanel criarAbaAvaliacoes() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(new EmptyBorder(10, 10, 10, 10));
        painel.setBackground(TemaLouquitos.BRANCO_PADRAO);

        String[] colunas = {"ID", "ID Pizza", "ID Cliente", "Nota", "Comentário"};
        avaliacoesTableModel = new DefaultTableModel(colunas, 0);
        tabelaAvaliacoes = new JTable(avaliacoesTableModel);
        tabelaAvaliacoes.getColumnModel().getColumn(4).setMinWidth(300);
        painel.add(new JScrollPane(tabelaAvaliacoes), BorderLayout.CENTER);

        JButton btnRefresh = new JButton("Atualizar Lista");
        btnRefresh.addActionListener(e -> carregarTodasAvaliacoes());
        painel.add(btnRefresh, BorderLayout.SOUTH);

        return painel;
    }

    private JLabel criarCaixaInfo(String titulo, String valor, Color corFundo) {
        JLabel label = new JLabel("<html><div style='text-align: center;'>" +
                "<p style='font-size: 14px;'>" + titulo + "</p>" +
                "<h2 style='margin-top: 10px;'>" + valor + "</h2>" +
                "</div></html>");
        label.setOpaque(true);
        label.setBackground(corFundo);
        label.setForeground(TemaLouquitos.BRANCO_PADRAO);
        label.setFont(new Font("Arial", Font.BOLD, 20));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);
        label.setBorder(BorderFactory.createLineBorder(TemaLouquitos.CINZA_ESCURO, 2));
        return label;
    }

    public void carregarDashboard() {
        if (mainFrame.getUsuarioLogado() != null) {
            lblNomeAdmin.setText("Admin: " + mainFrame.getUsuarioLogado().getNome().split(" ")[0]);
        }

        double faturamento = pedidoDAO.calcularFaturamentoTotal();
        int totalPedidos = pedidoDAO.contarTotalPedidos();
        int totalPizzas = pedidoDAO.contarTotalPizzasVendidas();
        int totalClientes = usuarioDAO.contarTotalClientes();

        lblTotalVendas.setText(String.format("<html><div style='text-align: center;'><p style='font-size: 14px;'>Faturamento Total</p><h2 style='margin-top: 10px;'>R$ %.2f</h2></div></html>", faturamento));
        lblTotalPedidos.setText(String.format("<html><div style='text-align: center;'><p style='font-size: 14px;'>Total de Pedidos</p><h2 style='margin-top: 10px;'>%d</h2></div></html>", totalPedidos));
        lblTotalPizzas.setText(String.format("<html><div style='text-align: center;'><p style='font-size: 14px;'>Total de Pizzas Vendidas</p><h2 style='margin-top: 10px;'>%d</h2></div></html>", totalPizzas));
        lblTotalClientes.setText(String.format("<html><div style='text-align: center;'><p style='font-size: 14px;'>Total de Clientes</p><h2 style='margin-top: 10px;'>%d</h2></div></html>", totalClientes));
    }

    private void carregarTodosPedidos() {
        pedidosTableModel.setRowCount(0);
        List<Pedido> pedidos = pedidoDAO.listarTodosPedidos();

        for (Pedido pedido : pedidos) {
            String enderecoStr = pedido.getEnderecoCompleto() != null ? pedido.getEnderecoCompleto() : "Retirada";
            pedidosTableModel.addRow(new Object[]{
                    pedido.getIdPedido(),
                    pedido.getIdUsuarioCliente(),
                    pedido.getStatusPedido(),
                    pedido.getTipoEntrega(),
                    pedido.getFormaPagamento(),
                    String.format("%.2f", pedido.getValorDesconto()),
                    String.format("%.2f", pedido.getValorTotal()),
                    enderecoStr
            });
        }
    }

    private void carregarTodosClientes() {
        clientesTableModel.setRowCount(0);
        List<Usuario> clientes = usuarioDAO.listarTodosClientes();

        for (Usuario usuario : clientes) {
            clientesTableModel.addRow(new Object[]{
                    usuario.getCliente().getIdCliente(),
                    usuario.getNome(),
                    usuario.getEmail(),
                    usuario.getCliente().getTelefone() != null ? usuario.getCliente().getTelefone() : "N/A"
            });
        }
    }

    private void carregarTodosCupons() {
        cuponsTableModel.setRowCount(0);
        List<Cupom> cupons = cupomDAO.listarTodosCupons();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        for (Cupom cupom : cupons) {
            String validade = (cupom.getDataValidade() != null) ? sdf.format(cupom.getDataValidade()) : "N/A";
            cuponsTableModel.addRow(new Object[]{
                    cupom.getIdCupom(),
                    cupom.getCodigo(),
                    cupom.getTipoDesconto(),
                    cupom.getValor(),
                    validade,
                    cupom.isAtivo()
            });
        }
    }

    private void carregarTodasAvaliacoes() {
        avaliacoesTableModel.setRowCount(0);
        List<Avaliacao> avaliacoes = avaliacaoDAO.listarTodasAvaliacoes();

        for (Avaliacao a : avaliacoes) {
            avaliacoesTableModel.addRow(new Object[]{
                    a.getIdAvaliacao(),
                    a.getIdPizzaRef(),
                    a.getIdClienteRef(),
                    a.getNota(),
                    a.getComentario()
            });
        }
    }
}