package librarymanagementsystem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class LibraryGUI extends JFrame {
    private LibrarySystem library;
    private User currentUser;

    private CardLayout cardLayout;
    private JPanel mainPanel;

    // Login components
    private JTextField nameField;
    private JComboBox<String> roleComboBox;

    // Admin components
    private JTable bookTable;
    private DefaultTableModel bookTableModel;
    private JTable userTable;
    private DefaultTableModel userTableModel;

    // Librarian components
    private JComboBox<String> issueBookComboBox;
    private JComboBox<String> issueMemberComboBox;
    private JComboBox<String> returnTransactionComboBox;

    // Member components
    private JTable searchResultTable;
    private DefaultTableModel searchResultTableModel;
    private JTable myTransactionsTable;
    private DefaultTableModel myTransactionsTableModel;

    public LibraryGUI() {
        library = new LibrarySystem();
        setTitle("Library Management System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(createLoginPanel(), "login");
        mainPanel.add(createAdminPanel(), "admin");
        mainPanel.add(createLibrarianPanel(), "librarian");
        mainPanel.add(createMemberPanel(), "member");

        add(mainPanel);
        cardLayout.show(mainPanel, "login");
    }

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel titleLabel = new JLabel("Library Management System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));

        JLabel nameLabel = new JLabel("Enter your name:");
        nameField = new JTextField(20);

        JLabel roleLabel = new JLabel("Select role:");
        String[] roles = {"Admin", "Librarian", "Member"};
        roleComboBox = new JComboBox<>(roles);

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> login());

        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(nameLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        panel.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(roleLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 2;
        panel.add(roleComboBox, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        panel.add(loginButton, gbc);

        return panel;
    }

    private JPanel createAdminPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JLabel welcomeLabel = new JLabel();
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(welcomeLabel, BorderLayout.NORTH);

        JTabbedPane tabbedPane = new JTabbedPane();

        // Book Management Tab
        JPanel bookPanel = new JPanel(new BorderLayout());
        bookTableModel = new DefaultTableModel(new Object[]{"ID", "Title", "Author", "Genre", "ISBN", "Copies"}, 0);
        bookTable = new JTable(bookTableModel);
        bookPanel.add(new JScrollPane(bookTable), BorderLayout.CENTER);

        JPanel bookButtonPanel = new JPanel();
        JButton addBookButton = new JButton("Add Book");
        addBookButton.addActionListener(e -> showBookForm(null));
        JButton editBookButton = new JButton("Edit Book");
        editBookButton.addActionListener(e -> editSelectedBook());
        JButton removeBookButton = new JButton("Remove Book");
        removeBookButton.addActionListener(e -> removeSelectedBook());
        bookButtonPanel.add(addBookButton);
        bookButtonPanel.add(editBookButton);
        bookButtonPanel.add(removeBookButton);
        bookPanel.add(bookButtonPanel, BorderLayout.SOUTH);

        tabbedPane.addTab("Manage Books", bookPanel);

        // User Management Tab
        JPanel userPanel = new JPanel(new BorderLayout());
        userTableModel = new DefaultTableModel(new Object[]{"ID", "Name", "Role"}, 0);
        userTable = new JTable(userTableModel);
        userPanel.add(new JScrollPane(userTable), BorderLayout.CENTER);

        JPanel userButtonPanel = new JPanel();
        JButton addUserButton = new JButton("Add User");
        addUserButton.addActionListener(e -> showUserForm());
        JButton removeUserButton = new JButton("Remove User");
        removeUserButton.addActionListener(e -> removeSelectedUser());
        userButtonPanel.add(addUserButton);
        userButtonPanel.add(removeUserButton);
        userPanel.add(userButtonPanel, BorderLayout.SOUTH);

        tabbedPane.addTab("Manage Users", userPanel);

        panel.add(tabbedPane, BorderLayout.CENTER);

        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> logout());
        panel.add(logoutButton, BorderLayout.SOUTH);

        // Update welcome label when panel is shown
        panel.addComponentListener(new ComponentAdapter() {
            public void componentShown(ComponentEvent e) {
                welcomeLabel.setText("Welcome, " + currentUser.getName() + " (Admin)");
                refreshBookTable();
                refreshUserTable();
            }
        });

        return panel;
    }

    private JPanel createLibrarianPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JLabel welcomeLabel = new JLabel();
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(welcomeLabel, BorderLayout.NORTH);

        JTabbedPane tabbedPane = new JTabbedPane();

        // Issue Book Tab
        JPanel issuePanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel bookLabel = new JLabel("Select Book:");
        issueBookComboBox = new JComboBox<>();
        JLabel memberLabel = new JLabel("Select Member:");
        issueMemberComboBox = new JComboBox<>();
        JButton issueButton = new JButton("Issue Book");
        issueButton.addActionListener(e -> issueBook());

        gbc.insets = new Insets(5,5,5,5);
        gbc.gridx = 0; gbc.gridy = 0;
        issuePanel.add(bookLabel, gbc);
        gbc.gridx = 1;
        issuePanel.add(issueBookComboBox, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        issuePanel.add(memberLabel, gbc);
        gbc.gridx = 1;
        issuePanel.add(issueMemberComboBox, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        issuePanel.add(issueButton, gbc);

        tabbedPane.addTab("Issue Book", issuePanel);

        // Return Book Tab
        JPanel returnPanel = new JPanel(new GridBagLayout());
        JLabel transactionLabel = new JLabel("Select Transaction:");
        returnTransactionComboBox = new JComboBox<>();
        JButton returnButton = new JButton("Return Book");
        returnButton.addActionListener(e -> returnBook());

        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.gridx = 0; gbc.gridy = 0;
        returnPanel.add(transactionLabel, gbc);
        gbc.gridx = 1;
        returnPanel.add(returnTransactionComboBox, gbc);
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
        returnPanel.add(returnButton, gbc);

        tabbedPane.addTab("Return Book", returnPanel);

        // View Transactions Tab
        JPanel transactionsPanel = new JPanel(new BorderLayout());
        String[] columns = {"ID", "Book", "Member", "Issue Date", "Due Date", "Return Date", "Fine"};
        DefaultTableModel transactionsTableModel = new DefaultTableModel(columns, 0);
        JTable transactionsTable = new JTable(transactionsTableModel);
        transactionsPanel.add(new JScrollPane(transactionsTable), BorderLayout.CENTER);

        tabbedPane.addTab("View Transactions", transactionsPanel);

        panel.add(tabbedPane, BorderLayout.CENTER);

        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> logout());
        panel.add(logoutButton, BorderLayout.SOUTH);

        // Update data when panel is shown
        panel.addComponentListener(new ComponentAdapter() {
            public void componentShown(ComponentEvent e) {
                welcomeLabel.setText("Welcome, " + currentUser.getName() + " (Librarian)");
                refreshIssueBookComboBox();
                refreshIssueMemberComboBox();
                refreshReturnTransactionComboBox();
                // Update transactions table
                transactionsTableModel.setRowCount(0);
                for (Transaction t : library.getAllTransactions()) {
                    transactionsTableModel.addRow(new Object[]{
                        t.getTransactionId(),
                        t.getBook().getTitle(),
                        t.getMember().getName(),
                        t.getIssueDate(),
                        t.getDueDate(),
                        t.getReturnDate() != null ? t.getReturnDate() : "Not returned",
                        "$" + t.getFine()
                    });
                }
            }
        });

        return panel;
    }

    private JPanel createMemberPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JLabel welcomeLabel = new JLabel();
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(welcomeLabel, BorderLayout.NORTH);

        JTabbedPane tabbedPane = new JTabbedPane();

        // Search Books Tab
        JPanel searchPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel titleLabel = new JLabel("Title:");
        JTextField titleField = new JTextField(15);
        JLabel authorLabel = new JLabel("Author:");
        JTextField authorField = new JTextField(15);
        JLabel genreLabel = new JLabel("Genre:");
        JTextField genreField = new JTextField(15);
        JButton searchButton = new JButton("Search");

        String[] columns = {"ID", "Title", "Author", "Genre", "ISBN", "Copies"};
        searchResultTableModel = new DefaultTableModel(columns, 0);
        searchResultTable = new JTable(searchResultTableModel);

        gbc.insets = new Insets(5,5,5,5);
        gbc.gridx = 0; gbc.gridy = 0;
        searchPanel.add(titleLabel, gbc);
        gbc.gridx = 1;
        searchPanel.add(titleField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        searchPanel.add(authorLabel, gbc);
        gbc.gridx = 1;
        searchPanel.add(authorField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        searchPanel.add(genreLabel, gbc);
        gbc.gridx = 1;
        searchPanel.add(genreField, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        searchPanel.add(searchButton, gbc);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        searchPanel.add(new JScrollPane(searchResultTable), gbc);

        tabbedPane.addTab("Search Books", searchPanel);

        // My Transactions Tab
        JPanel myTransactionsPanel = new JPanel(new BorderLayout());
        String[] myTransColumns = {"ID", "Book", "Issue Date", "Due Date", "Return Date", "Fine"};
        myTransactionsTableModel = new DefaultTableModel(myTransColumns, 0);
        myTransactionsTable = new JTable(myTransactionsTableModel);
        myTransactionsPanel.add(new JScrollPane(myTransactionsTable), BorderLayout.CENTER);

        tabbedPane.addTab("My Transactions", myTransactionsPanel);

        panel.add(tabbedPane, BorderLayout.CENTER);

        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> logout());
        panel.add(logoutButton, BorderLayout.SOUTH);

        // Add action listeners
        searchButton.addActionListener(e -> {
            String title = titleField.getText().trim();
            String author = authorField.getText().trim();
            String genre = genreField.getText().trim();
            List<Book> results = library.searchBooks(title, author, genre);
            searchResultTableModel.setRowCount(0);
            for (Book b : results) {
                searchResultTableModel.addRow(new Object[]{
                    b.getBookId(), b.getTitle(), b.getAuthor(), b.getGenre(), b.getIsbn(), b.getCopiesAvailable()
                });
            }
        });

        // Update data when panel is shown
        panel.addComponentListener(new ComponentAdapter() {
            public void componentShown(ComponentEvent e) {
                welcomeLabel.setText("Welcome, " + currentUser.getName() + " (Member)");
                refreshMyTransactionsTable();
            }
        });

        return panel;
    }

    private void login() {
        String name = nameField.getText().trim();
        String role = (String) roleComboBox.getSelectedItem();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter your name.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        User user = library.addUser(name, role);
        if (user == null) {
            JOptionPane.showMessageDialog(this, "Invalid role selected.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        currentUser = user;
        switch (role) {
            case "Admin":
                cardLayout.show(mainPanel, "admin");
                break;
            case "Librarian":
                cardLayout.show(mainPanel, "librarian");
                break;
            case "Member":
                cardLayout.show(mainPanel, "member");
                break;
        }
    }

    private void logout() {
        currentUser = null;
        cardLayout.show(mainPanel, "login");
    }

    // Admin methods
    private void refreshBookTable() {
        bookTableModel.setRowCount(0);
        for (Book b : library.getAllBooks()) {
            bookTableModel.addRow(new Object[]{
                b.getBookId(), b.getTitle(), b.getAuthor(), b.getGenre(), b.getIsbn(), b.getCopiesAvailable()
            });
        }
    }

    private void refreshUserTable() {
        userTableModel.setRowCount(0);
        for (User u : library.getAllUsers()) {
            userTableModel.addRow(new Object[]{
                u.getUserId(), u.getName(), u.getRole()
            });
        }
    }

    private void showBookForm(Book book) {
        JDialog dialog = new JDialog(this, (book == null ? "Add Book" : "Edit Book"), true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel titleLabel = new JLabel("Title:");
        JTextField titleField = new JTextField(20);
        JLabel authorLabel = new JLabel("Author:");
        JTextField authorField = new JTextField(20);
        JLabel genreLabel = new JLabel("Genre:");
        JTextField genreField = new JTextField(20);
        JLabel isbnLabel = new JLabel("ISBN:");
        JTextField isbnField = new JTextField(20);
        JLabel copiesLabel = new JLabel("Copies:");
        JTextField copiesField = new JTextField(5);

        if (book != null) {
            titleField.setText(book.getTitle());
            authorField.setText(book.getAuthor());
            genreField.setText(book.getGenre());
            isbnField.setText(book.getIsbn());
            copiesField.setText(String.valueOf(book.getCopiesAvailable()));
        }

        gbc.insets = new Insets(5,5,5,5);
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST;
        dialog.add(titleLabel, gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        dialog.add(titleField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.EAST;
        dialog.add(authorLabel, gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        dialog.add(authorField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.anchor = GridBagConstraints.EAST;
        dialog.add(genreLabel, gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        dialog.add(genreField, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.anchor = GridBagConstraints.EAST;
        dialog.add(isbnLabel, gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        dialog.add(isbnField, gbc);

        gbc.gridx = 0; gbc.gridy = 4; gbc.anchor = GridBagConstraints.EAST;
        dialog.add(copiesLabel, gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        dialog.add(copiesField, gbc);

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            String title = titleField.getText().trim();
            String author = authorField.getText().trim();
            String genre = genreField.getText().trim();
            String isbn = isbnField.getText().trim();
            String copiesStr = copiesField.getText().trim();

            if (title.isEmpty() || author.isEmpty() || genre.isEmpty() || isbn.isEmpty() || copiesStr.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please fill all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int copies;
            try {
                copies = Integer.parseInt(copiesStr);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Copies must be a number.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (book == null) {
                library.addBook(title, author, genre, isbn, copies);
            } else {
                library.editBook(book.getBookId(), title, author, genre, isbn, copies);
            }
            refreshBookTable();
            dialog.dispose();
        });

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dialog.dispose());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(submitButton);
        buttonPanel.add(cancelButton);

        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        dialog.add(buttonPanel, gbc);

        dialog.setVisible(true);
    }

    private void editSelectedBook() {
        int selectedRow = bookTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a book to edit.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int bookId = (int) bookTableModel.getValueAt(selectedRow, 0);
        Book book = library.getBookById(bookId);
        if (book != null) {
            showBookForm(book);
        }
    }

    private void removeSelectedBook() {
        int selectedRow = bookTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a book to remove.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int bookId = (int) bookTableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to remove this book?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            library.removeBook(bookId);
            refreshBookTable();
        }
    }

    private void showUserForm() {
        JDialog dialog = new JDialog(this, "Add User", true);
        dialog.setSize(300, 200);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField(20);
        JLabel roleLabel = new JLabel("Role:");
        String[] roles = {"Admin", "Librarian", "Member"};
        JComboBox<String> roleComboBox = new JComboBox<>(roles);

        gbc.insets = new Insets(5,5,5,5);
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST;
        dialog.add(nameLabel, gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        dialog.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.EAST;
        dialog.add(roleLabel, gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        dialog.add(roleComboBox, gbc);

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String role = (String) roleComboBox.getSelectedItem();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please enter a name.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            User user = library.addUser(name, role);
            if (user == null) {
                JOptionPane.showMessageDialog(dialog, "Invalid role.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            refreshUserTable();
            dialog.dispose();
        });

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dialog.dispose());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(submitButton);
        buttonPanel.add(cancelButton);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        dialog.add(buttonPanel, gbc);

        dialog.setVisible(true);
    }

    private void removeSelectedUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a user to remove.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int userId = (int) userTableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to remove this user?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            library.removeUser(userId);
            refreshUserTable();
        }
    }

    // Librarian methods
    private void refreshIssueBookComboBox() {
        issueBookComboBox.removeAllItems();
        for (Book b : library.getAllBooks()) {
            if (b.getCopiesAvailable() > 0) {
                issueBookComboBox.addItem(b.getBookId() + " - " + b.getTitle());
            }
        }
    }

    private void refreshIssueMemberComboBox() {
        issueMemberComboBox.removeAllItems();
        for (User u : library.getAllUsers()) {
            if ("Member".equals(u.getRole())) {
                issueMemberComboBox.addItem(u.getUserId() + " - " + u.getName());
            }
        }
    }

    private void refreshReturnTransactionComboBox() {
        returnTransactionComboBox.removeAllItems();
        for (Transaction t : library.getAllTransactions()) {
            if (t.getReturnDate() == null) {
                returnTransactionComboBox.addItem(t.getTransactionId() + " - " + t.getBook().getTitle() + " (Member: " + t.getMember().getName() + ")");
            }
        }
    }

    private void issueBook() {
        String bookStr = (String) issueBookComboBox.getSelectedItem();
        String memberStr = (String) issueMemberComboBox.getSelectedItem();
        if (bookStr == null || memberStr == null) {
            JOptionPane.showMessageDialog(this, "Please select both book and member.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int bookId = Integer.parseInt(bookStr.split(" - ")[0]);
        int memberId = Integer.parseInt(memberStr.split(" - ")[0]);
        Transaction transaction = library.issueBook(bookId, memberId);
        if (transaction != null) {
            JOptionPane.showMessageDialog(this, "Book issued. Due date: " + transaction.getDueDate());
            refreshIssueBookComboBox();
            refreshReturnTransactionComboBox();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to issue book.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void returnBook() {
        String transactionStr = (String) returnTransactionComboBox.getSelectedItem();
        if (transactionStr == null) {
            JOptionPane.showMessageDialog(this, "Please select a transaction.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int transactionId = Integer.parseInt(transactionStr.split(" - ")[0]);
        boolean success = library.returnBook(transactionId);
        if (success) {
            JOptionPane.showMessageDialog(this, "Book returned successfully.");
            refreshIssueBookComboBox();
            refreshReturnTransactionComboBox();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to return book.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Member methods
    private void refreshMyTransactionsTable() {
        myTransactionsTableModel.setRowCount(0);
        List<Transaction> transactions = library.getTransactionHistory(currentUser.getUserId());
        for (Transaction t : transactions) {
            myTransactionsTableModel.addRow(new Object[]{
                t.getTransactionId(),
                t.getBook().getTitle(),
                t.getIssueDate(),
                t.getDueDate(),
                t.getReturnDate() != null ? t.getReturnDate() : "Not returned",
                "$" + t.getFine()
            });
        }
    }
}
