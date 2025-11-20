package pizzaria.view;

import pizzaria.dao.ClienteDAO;
import pizzaria.dao.EnderecoDAO;
import pizzaria.model.Cliente;
import pizzaria.model.Endereco;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class PainelEnderecos extends JPanel {

    private MainFrame mainFrame;
    private EnderecoDAO enderecoDAO;
    private ClienteDAO clienteDAO;

    private JPanel painelListaEnderecos;
    private JTextField txtRotulo, txtRua, txtNumero, txtBairro, txtCep, txtComplemento;
    private Cliente clienteAtual;

    public PainelEnderecos(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.enderecoDAO = new EnderecoDAO();
        this.clienteDAO = new ClienteDAO();

        setLayout(new BorderLayout(10, 10));
        setBackground(TemaLouquitos.CINZA_CLARO);
        setBorder(new EmptyBorder(10, 10, 10, 10));

        criarHeader();

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                criarPainelFormulario(),
                criarPainelLista());
        splitPane.setDividerLocation(350);
        add(splitPane, BorderLayout.CENTER);
    }

    private void criarHeader() {
        JPanel painelHeader = new JPanel(new BorderLayout());
        painelHeader.setOpaque(false);

        JLabel lblTitulo = new JLabel("Meus Endereços");
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

    private JPanel criarPainelFormulario() {
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBackground(TemaLouquitos.BRANCO_PADRAO);
        painel.setBorder(BorderFactory.createTitledBorder("Adicionar Novo Endereço"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        painel.add(new JLabel("Rótulo (ex: Casa):"), gbc);
        txtRotulo = new JTextField(20);
        gbc.gridx = 1; painel.add(txtRotulo, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        painel.add(new JLabel("CEP:"), gbc);
        txtCep = new JTextField(20);
        gbc.gridx = 1; painel.add(txtCep, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        painel.add(new JLabel("Rua:"), gbc);
        txtRua = new JTextField(20);
        gbc.gridx = 1; painel.add(txtRua, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        painel.add(new JLabel("Número:"), gbc);
        txtNumero = new JTextField(20);
        gbc.gridx = 1; painel.add(txtNumero, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        painel.add(new JLabel("Bairro:"), gbc);
        txtBairro = new JTextField(20);
        gbc.gridx = 1; painel.add(txtBairro, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        painel.add(new JLabel("Complemento:"), gbc);
        txtComplemento = new JTextField(20);
        gbc.gridx = 1; painel.add(txtComplemento, gbc);

        JButton btnSalvar = new JButton("Salvar Endereço");
        btnSalvar.setFont(new Font("Arial", Font.BOLD, 14));
        btnSalvar.setBackground(TemaLouquitos.VERDE_PIZZARIA);
        btnSalvar.setForeground(TemaLouquitos.BRANCO_PADRAO);
        gbc.gridx = 1; gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.EAST;
        painel.add(btnSalvar, gbc);

        btnSalvar.addActionListener(e -> salvarNovoEndereco());

        return painel;
    }

    private JScrollPane criarPainelLista() {
        painelListaEnderecos = new JPanel();
        painelListaEnderecos.setLayout(new BoxLayout(painelListaEnderecos, BoxLayout.Y_AXIS));
        painelListaEnderecos.setBackground(TemaLouquitos.BRANCO_PADRAO);

        JScrollPane scrollPane = new JScrollPane(painelListaEnderecos);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Meus Endereços Salvos"));
        return scrollPane;
    }

    public void carregarEnderecos() {
        if (mainFrame.getUsuarioLogado() == null) return;

        this.clienteAtual = clienteDAO.getClientePorUsuarioId(mainFrame.getUsuarioLogado().getIdUsuario());
        if (this.clienteAtual == null) return;

        painelListaEnderecos.removeAll();
        List<Endereco> enderecos = enderecoDAO.listarEnderecos(clienteAtual.getIdCliente());

        if (enderecos.isEmpty()) {
            painelListaEnderecos.add(new JLabel("  Nenhum endereço cadastrado."));
        } else {
            for (Endereco end : enderecos) {
                painelListaEnderecos.add(criarItemEndereco(end));
                painelListaEnderecos.add(Box.createRigidArea(new Dimension(0, 5)));
            }
        }

        painelListaEnderecos.revalidate();
        painelListaEnderecos.repaint();
    }

    private void salvarNovoEndereco() {
        if (this.clienteAtual == null) return;

        String rotulo = txtRotulo.getText();
        String rua = txtRua.getText();
        String numero = txtNumero.getText();
        String cep = txtCep.getText();

        if (rotulo.isEmpty() || rua.isEmpty() || numero.isEmpty() || cep.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Rótulo, Rua, Número e CEP são obrigatórios.", "Erro", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Endereco end = new Endereco();
        end.setIdClienteRef(clienteAtual.getIdCliente());
        end.setRotulo(rotulo);
        end.setRua(rua);
        end.setNumero(numero);
        end.setCep(cep);
        end.setBairro(txtBairro.getText());
        end.setComplemento(txtComplemento.getText());

        enderecoDAO.salvarEndereco(end);

        txtRotulo.setText("");
        txtRua.setText("");
        txtNumero.setText("");
        txtBairro.setText("");
        txtCep.setText("");
        txtComplemento.setText("");

        carregarEnderecos();
    }

    private JPanel criarItemEndereco(Endereco end) {
        JPanel painel = new JPanel(new BorderLayout(10, 2));
        painel.setBackground(TemaLouquitos.CINZA_CLARO);
        painel.setBorder(new EmptyBorder(5, 10, 5, 10));
        painel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));

        JLabel lblRotulo = new JLabel(end.getRotulo());
        lblRotulo.setFont(new Font("Arial", Font.BOLD, 16));
        lblRotulo.setForeground(TemaLouquitos.VERMELHO_PIZZARIA);

        String desc = String.format("<html>%s, %s - %s</html>", end.getRua(), end.getNumero(), end.getCep());
        JLabel lblDesc = new JLabel(desc);
        lblDesc.setFont(new Font("Arial", Font.PLAIN, 12));

        painel.add(lblRotulo, BorderLayout.NORTH);
        painel.add(lblDesc, BorderLayout.CENTER);

        return painel;
    }
}