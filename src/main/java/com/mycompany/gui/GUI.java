package com.mycompany.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Virtual Bank Simulator - a beginner-friendly Java Swing OOP project.
 * Demo credentials: khadiza242 / 242
 */
public class GUI {
    private static final String USERNAME = "khadiza242";
    private static final String PASSWORD = "242";
    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final JFrame frame = new JFrame("Virtual Bank Simulator");
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel pages = new JPanel(cardLayout);
    private final BankService bankService = new BankService();

    private JLabel balanceLabel;
    private JTextArea historyArea;
    private JLabel statusLabel;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            setLookAndFeel();
            new GUI().showLogin();
        });
    }

    private static void setLookAndFeel() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ignored) {
            // The application still works with the default look and feel.
        }
    }

    private GUI() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(760, 520));
        frame.setSize(900, 620);
        frame.setLocationRelativeTo(null);
        pages.add(createLoginPage(), "login");
        pages.add(createDashboardPage(), "dashboard");
        frame.setContentPane(pages);
    }

    private void showLogin() {
        cardLayout.show(pages, "login");
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private JPanel createLoginPage() {
        JPanel outer = new JPanel(new GridBagLayout());
        outer.setBackground(new Color(238, 243, 249));

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(new EmptyBorder(34, 42, 34, 42));
        card.setPreferredSize(new Dimension(390, 360));

        JLabel icon = new JLabel("VIRTUAL BANK", SwingConstants.CENTER);
        icon.setFont(new Font("SansSerif", Font.BOLD, 26));
        icon.setForeground(new Color(26, 74, 130));
        icon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Secure • Simple • Convenient", SwingConstants.CENTER);
        subtitle.setForeground(new Color(95, 108, 124));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        styleField(usernameField);
        styleField(passwordField);

        JButton loginButton = primaryButton("LOG IN");
        JLabel message = new JLabel(" ", SwingConstants.CENTER);
        message.setForeground(new Color(190, 45, 45));
        message.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(icon);
        card.add(Box.createVerticalStrut(8));
        card.add(subtitle);
        card.add(Box.createVerticalStrut(28));
        card.add(leftLabel("Username"));
        card.add(Box.createVerticalStrut(5));
        card.add(usernameField);
        card.add(Box.createVerticalStrut(14));
        card.add(leftLabel("Password"));
        card.add(Box.createVerticalStrut(5));
        card.add(passwordField);
        card.add(Box.createVerticalStrut(20));
        card.add(loginButton);
        card.add(Box.createVerticalStrut(10));
        card.add(message);

        loginButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());
            if (USERNAME.equals(username) && PASSWORD.equals(password)) {
                refreshDashboard();
                cardLayout.show(pages, "dashboard");
                message.setText(" ");
            } else {
                message.setText("Incorrect username or password.");
            }
        });
        passwordField.addActionListener(e -> loginButton.doClick());

        outer.add(card);
        return outer;
    }

    private JPanel createDashboardPage() {
        JPanel rootPanel = new JPanel(new BorderLayout(18, 18));
        rootPanel.setBackground(new Color(245, 247, 251));
        rootPanel.setBorder(new EmptyBorder(22, 24, 22, 24));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        JLabel title = new JLabel("Account Dashboard");
        title.setFont(new Font("SansSerif", Font.BOLD, 27));
        title.setForeground(new Color(30, 55, 85));
        JLabel account = new JLabel("Account: " + USERNAME);
        account.setForeground(new Color(95, 108, 124));
        header.add(title, BorderLayout.NORTH);
        header.add(account, BorderLayout.SOUTH);

        JButton logoutButton = secondaryButton("Log out");
        logoutButton.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(frame, "Log out of your account?",
                    "Confirm logout", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) showLogin();
        });
        header.add(logoutButton, BorderLayout.EAST);

        JPanel balanceCard = new JPanel();
        balanceCard.setLayout(new BoxLayout(balanceCard, BoxLayout.Y_AXIS));
        balanceCard.setBackground(new Color(28, 77, 130));
        balanceCard.setBorder(new EmptyBorder(20, 24, 20, 24));
        JLabel balanceTitle = new JLabel("AVAILABLE BALANCE");
        balanceTitle.setForeground(new Color(220, 232, 246));
        balanceTitle.setFont(new Font("SansSerif", Font.BOLD, 13));
        balanceLabel = new JLabel("৳ 0.00");
        balanceLabel.setForeground(Color.WHITE);
        balanceLabel.setFont(new Font("SansSerif", Font.BOLD, 32));
        balanceCard.add(balanceTitle);
        balanceCard.add(Box.createVerticalStrut(8));
        balanceCard.add(balanceLabel);

        JPanel actions = new JPanel(new GridLayout(1, 3, 12, 12));
        actions.setOpaque(false);
        JButton depositButton = primaryButton("Deposit");
        JButton withdrawButton = primaryButton("Withdraw");
        JButton transferButton = primaryButton("Transfer");
        actions.add(depositButton);
        actions.add(withdrawButton);
        actions.add(transferButton);

        depositButton.addActionListener(e -> performTransaction("DEPOSIT"));
        withdrawButton.addActionListener(e -> performTransaction("WITHDRAW"));
        transferButton.addActionListener(e -> performTransaction("TRANSFER"));

        JPanel top = new JPanel(new BorderLayout(0, 16));
        top.setOpaque(false);
        top.add(header, BorderLayout.NORTH);
        top.add(balanceCard, BorderLayout.CENTER);
        top.add(actions, BorderLayout.SOUTH);

        historyArea = new JTextArea();
        historyArea.setEditable(false);
        historyArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        historyArea.setBorder(new EmptyBorder(12, 12, 12, 12));
        JScrollPane historyScroll = new JScrollPane(historyArea);
        historyScroll.setBorder(BorderFactory.createTitledBorder("Recent Transactions"));

        statusLabel = new JLabel("Your transactions are saved locally on this computer.");
        statusLabel.setForeground(new Color(95, 108, 124));

        rootPanel.add(top, BorderLayout.NORTH);
        rootPanel.add(historyScroll, BorderLayout.CENTER);
        rootPanel.add(statusLabel, BorderLayout.SOUTH);
        return rootPanel;
    }

    private void performTransaction(String type) {
        String prompt = type.equals("DEPOSIT") ? "Enter deposit amount:"
                : type.equals("WITHDRAW") ? "Enter withdrawal amount:"
                : "Enter transfer amount:";
        String input = JOptionPane.showInputDialog(frame, prompt, type, JOptionPane.PLAIN_MESSAGE);
        if (input == null) return;

        try {
            double amount = Double.parseDouble(input.trim());
            bankService.transact(type, amount);
            refreshDashboard();
            statusLabel.setText(type + " completed successfully.");
            JOptionPane.showMessageDialog(frame, type + " completed successfully.",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            showError("Please enter a valid numeric amount.");
        } catch (IllegalArgumentException | IOException ex) {
            showError(ex.getMessage());
        }
    }

    private void refreshDashboard() {
        try {
            balanceLabel.setText(String.format("৳ %.2f", bankService.getBalance()));
            List<Transaction> transactions = bankService.getTransactions();
            StringBuilder text = new StringBuilder();
            if (transactions.isEmpty()) {
                text.append("No transactions yet. Make a deposit to get started.");
            } else {
                text.append(String.format("%-20s %-12s %12s %14s%n",
                        "DATE", "TYPE", "AMOUNT", "BALANCE"));
                text.append("---------------------------------------------------------------\n");
                double balance = 0;
                for (Transaction transaction : transactions) {
                    if (transaction.type.equals("DEPOSIT")) balance += transaction.amount;
                    else balance -= transaction.amount;
                    text.append(String.format("%-20s %-12s %12.2f %14.2f%n",
                            transaction.date, transaction.type, transaction.amount, balance));
                }
            }
            historyArea.setText(text.toString());
            historyArea.setCaretPosition(historyArea.getDocument().getLength());
        } catch (IOException ex) {
            showError("Could not read transaction history: " + ex.getMessage());
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(frame, message, "Unable to complete action",
                JOptionPane.ERROR_MESSAGE);
    }

    private static JLabel leftLabel(String text) {
        JLabel label = new JLabel(text);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        label.setFont(new Font("SansSerif", Font.BOLD, 13));
        return label;
    }

    private static void styleField(JTextField field) {
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        field.setPreferredSize(new Dimension(300, 38));
        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(205, 214, 224)),
                new EmptyBorder(6, 9, 6, 9)));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    private static JButton primaryButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 13));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(32, 89, 150));
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(11, 18, 11, 18));
        return button;
    }

    private static JButton secondaryButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 12));
        button.setFocusPainted(false);
        return button;
    }

    private static class Transaction {
        private final String date;
        private final String type;
        private final double amount;

        private Transaction(String date, String type, double amount) {
            this.date = date;
            this.type = type;
            this.amount = amount;
        }
    }

    /**
     * Handles transaction rules and persistence separately from the GUI.
     * This separation is an example of basic OOP design.
     */
    private static class BankService {
        private final Path dataFile;

        private BankService() {
            String home = System.getProperty("user.home");
            Path dataDirectory = Paths.get(home, ".virtual-bank-simulator");
            this.dataFile = dataDirectory.resolve("transactions.csv");
        }

        private void transact(String type, double amount) throws IOException {
            if (!type.equals("DEPOSIT") && !type.equals("WITHDRAW") && !type.equals("TRANSFER")) {
                throw new IllegalArgumentException("Unknown transaction type.");
            }
            if (!Double.isFinite(amount) || amount <= 0) {
                throw new IllegalArgumentException("Amount must be greater than zero.");
            }
            if (!type.equals("DEPOSIT") && amount > getBalance()) {
                throw new IllegalArgumentException("Insufficient balance.");
            }

            Files.createDirectories(dataFile.getParent());
            String line = LocalDateTime.now().format(DATE_FORMAT) + "," + type + "," +
                    String.format(java.util.Locale.US, "%.2f", amount) + System.lineSeparator();
            Files.write(dataFile, line.getBytes(StandardCharsets.UTF_8),
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        }

        private List<Transaction> getTransactions() throws IOException {
            List<Transaction> result = new ArrayList<>();
            if (!Files.exists(dataFile)) return result;

            for (String line : Files.readAllLines(dataFile, StandardCharsets.UTF_8)) {
                String[] parts = line.split(",", -1);
                if (parts.length != 3) continue;
                try {
                    double amount = Double.parseDouble(parts[2]);
                    if (amount > 0) result.add(new Transaction(parts[0], parts[1], amount));
                } catch (NumberFormatException ignored) {
                    // Ignore malformed records instead of crashing the application.
                }
            }
            return result;
        }

        private double getBalance() throws IOException {
            double balance = 0;
            for (Transaction transaction : getTransactions()) {
                if (transaction.type.equals("DEPOSIT")) balance += transaction.amount;
                else balance -= transaction.amount;
            }
            return balance;
        }
    }
}
