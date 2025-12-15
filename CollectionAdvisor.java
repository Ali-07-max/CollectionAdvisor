import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.*;
import java.util.*;
import java.util.List;
import java.io.*;

public class CollectionAdvisor extends JFrame {

    // UI Components
    private JPanel mainPanel, requirementsPanel, resultPanel, methodsPanel;
    private JCheckBox maintainOrder, allowDuplicates, fastSearch, fastInsertion,
            fastDeletion, keyValuePairs, threadSafe, sortedOrder;
    private JButton analyzeBtn, resetBtn, exportBtn;
    private JLabel classNameLabel, collectionSizeLabel;
    private JTextArea detailsArea;
    private Object collectionInstance;
    private JScrollPane methodsScrollPane;
    private JPanel statsPanel;

    // Modern Color Palette
    private final Color PRIMARY_COLOR = new Color(99, 102, 241);
    private final Color PRIMARY_DARK = new Color(79, 70, 229);
    private final Color ACCENT_COLOR = new Color(16, 185, 129);
    private final Color BACKGROUND_COLOR = new Color(249, 250, 251);
    private final Color CARD_COLOR = Color.WHITE;
    private final Color TEXT_PRIMARY = new Color(17, 24, 39);
    private final Color TEXT_SECONDARY = new Color(107, 114, 128);
    private final Color BORDER_COLOR = new Color(229, 231, 235);
    private final Color HOVER_COLOR = new Color(243, 244, 246);

    public CollectionAdvisor() {
        setTitle("Java Collection Framework Advisor");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Set modern look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        initializeComponents();
        setVisible(true);
    }

    private void initializeComponents() {
        mainPanel = new JPanel(new BorderLayout(0, 0));
        mainPanel.setBackground(BACKGROUND_COLOR);

        mainPanel.add(createHeaderPanel(), BorderLayout.NORTH);
        mainPanel.add(createContentPanel(), BorderLayout.CENTER);
        
        add(mainPanel);
    }

    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(PRIMARY_COLOR);
        header.setBorder(new EmptyBorder(30, 40, 30, 40));

        // Title section
        JPanel titlePanel = new JPanel(new GridLayout(2, 1, 0, 5));
        titlePanel.setOpaque(false);

        JLabel title = new JLabel("Collection Framework Advisor");
        title.setFont(new Font("Segoe UI", Font.BOLD, 32));
        title.setForeground(Color.WHITE);

        JLabel subtitle = new JLabel("Find the perfect Java collection for your needs");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitle.setForeground(new Color(224, 231, 255));

        titlePanel.add(title);
        titlePanel.add(subtitle);

        // Icon
        JLabel iconLabel = new JLabel("☕");
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 48));
        iconLabel.setBorder(new EmptyBorder(0, 0, 0, 20));

        header.add(iconLabel, BorderLayout.WEST);
        header.add(titlePanel, BorderLayout.CENTER);
        
        return header;
    }

    private JPanel createContentPanel() {
        JPanel content = new JPanel(new BorderLayout(20, 20));
        content.setBackground(BACKGROUND_COLOR);
        content.setBorder(new EmptyBorder(30, 40, 30, 40));

        JPanel mainContent = new JPanel(new GridBagLayout());
        mainContent.setOpaque(false);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.insets = new Insets(0, 0, 0, 15);
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainContent.add(createRequirementsPanel(), gbc);
        
        gbc.gridx = 1;
        gbc.insets = new Insets(0, 0, 0, 0);
        mainContent.add(createResultPanel(), gbc);

        content.add(mainContent, BorderLayout.CENTER);
        return content;
    }

    private JPanel createRequirementsPanel() {
        JPanel panel = createCard("Requirements");
        panel.setLayout(new BorderLayout(0, 20));

        // Checkboxes
        JPanel checkboxPanel = new JPanel(new GridLayout(8, 1, 0, 12));
        checkboxPanel.setOpaque(false);

        maintainOrder = createStyledCheckbox(" Maintain Insertion Order");
        allowDuplicates = createStyledCheckbox(" Allow Duplicate Values");
        fastSearch = createStyledCheckbox(" Fast Search (O(1) lookup)");
        fastInsertion = createStyledCheckbox(" Fast Insertion");
        fastDeletion = createStyledCheckbox(" Fast Deletion");
        keyValuePairs = createStyledCheckbox(" Store Key-Value Pairs");
        threadSafe = createStyledCheckbox(" Thread Safe");
        sortedOrder = createStyledCheckbox(" Sorted Order");

        JCheckBox[] boxes = {maintainOrder, allowDuplicates, fastSearch, fastInsertion,
                fastDeletion, keyValuePairs, threadSafe, sortedOrder};

        for (JCheckBox box : boxes) {
            checkboxPanel.add(box);
        }

        // Buttons
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(new EmptyBorder(20, 0, 0, 0));

        analyzeBtn = createStyledButton("Analyze", PRIMARY_COLOR);
        analyzeBtn.addActionListener(e -> analyzeRequirements());

        resetBtn = createStyledButton("Reset", TEXT_SECONDARY);
        resetBtn.addActionListener(e -> resetForm());

        buttonPanel.add(analyzeBtn);
        buttonPanel.add(resetBtn);

        panel.add(checkboxPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createResultPanel() {
        JPanel panel = createCard("Recommended Collection");
        panel.setLayout(new BorderLayout(0, 20));

        // Result header
        JPanel headerPanel = new JPanel(new BorderLayout(10, 10));
        headerPanel.setOpaque(false);

        classNameLabel = new JLabel("Select requirements to begin", SwingConstants.CENTER);
        classNameLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        classNameLabel.setForeground(PRIMARY_COLOR);

        collectionSizeLabel = new JLabel("Size: 0", SwingConstants.CENTER);
        collectionSizeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        collectionSizeLabel.setForeground(TEXT_SECONDARY);

        headerPanel.add(classNameLabel, BorderLayout.CENTER);
        headerPanel.add(collectionSizeLabel, BorderLayout.SOUTH);

        // Details area
        detailsArea = new JTextArea();
        detailsArea.setEditable(false);
        detailsArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        detailsArea.setLineWrap(true);
        detailsArea.setWrapStyleWord(true);
        detailsArea.setBackground(HOVER_COLOR);
        detailsArea.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        JScrollPane detailsScroll = new JScrollPane(detailsArea);
        detailsScroll.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        detailsScroll.setPreferredSize(new Dimension(0, 150));

        // Methods panel
        methodsPanel = new JPanel();
        methodsPanel.setLayout(new BoxLayout(methodsPanel, BoxLayout.Y_AXIS));
        methodsPanel.setOpaque(false);

        methodsScrollPane = new JScrollPane(methodsPanel);
        methodsScrollPane.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(BORDER_COLOR),
            "Available Operations",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 12),
            TEXT_SECONDARY
        ));
        methodsScrollPane.setVisible(false);
        methodsScrollPane.getViewport().setBackground(CARD_COLOR);

        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(detailsScroll, BorderLayout.CENTER);
        panel.add(methodsScrollPane, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createCard(String title) {
        JPanel card = new JPanel();
        card.setBackground(CARD_COLOR);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            new EmptyBorder(25, 25, 25, 25)
        ));

        // Add shadow effect
        card.setBorder(BorderFactory.createCompoundBorder(
            new ShadowBorder(),
            new EmptyBorder(25, 25, 25, 25)
        ));

        return card;
    }

    private JCheckBox createStyledCheckbox(String text) {
        JCheckBox checkbox = new JCheckBox(text);
        checkbox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        checkbox.setForeground(TEXT_PRIMARY);
        checkbox.setBackground(CARD_COLOR);
        checkbox.setFocusPainted(false);
        checkbox.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return checkbox;
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(0, 45));
        
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.darker());
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }

    private void analyzeRequirements() {
        String className = determineCollectionClass();
        if (className == null) {
            JOptionPane.showMessageDialog(this, 
                "Please select at least one requirement", 
                "No Requirements Selected", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        classNameLabel.setText(className);
        displayCollectionDetails(className);
        generateMethodButtons(className);
    }

    private String determineCollectionClass() {
        // Map-based collections
        if (keyValuePairs.isSelected()) {
            if (threadSafe.isSelected()) return "ConcurrentHashMap";
            if (sortedOrder.isSelected()) return "TreeMap";
            if (maintainOrder.isSelected()) return "LinkedHashMap";
            return "HashMap";
        }
        
        // Set-based collections
        if (!allowDuplicates.isSelected()) {
            if (sortedOrder.isSelected()) return "TreeSet";
            if (maintainOrder.isSelected()) return "LinkedHashSet";
            return "HashSet";
        }
        
        // List-based collections
        if (threadSafe.isSelected()) return "Vector";
        if (fastInsertion.isSelected() && fastDeletion.isSelected()) return "LinkedList";
        
        return "ArrayList";
    }

    private void displayCollectionDetails(String className) {
        StringBuilder details = new StringBuilder();
        
        details.append("Collection Type: ").append(className).append("\n\n");
        details.append("Characteristics:\n");
        
        switch (className) {
            case "ArrayList":
                details.append("• Dynamic array implementation\n");
                details.append("• Fast random access O(1)\n");
                details.append("• Slow insertion/deletion O(n)\n");
                details.append("• Allows duplicates and null\n");
                break;
            case "LinkedList":
                details.append("• Doubly-linked list implementation\n");
                details.append("• Fast insertion/deletion O(1)\n");
                details.append("• Slow random access O(n)\n");
                details.append("• Allows duplicates and null\n");
                break;
            case "HashSet":
                details.append("• Hash table implementation\n");
                details.append("• Fast operations O(1)\n");
                details.append("• No duplicates allowed\n");
                details.append("• Unordered collection\n");
                break;
            case "LinkedHashSet":
                details.append("• Hash table + linked list\n");
                details.append("• Maintains insertion order\n");
                details.append("• Fast operations O(1)\n");
                details.append("• No duplicates allowed\n");
                break;
            case "TreeSet":
                details.append("• Red-Black tree implementation\n");
                details.append("• Sorted order (natural/comparator)\n");
                details.append("• Operations O(log n)\n");
                details.append("• No duplicates allowed\n");
                break;
            case "HashMap":
                details.append("• Hash table implementation\n");
                details.append("• Fast key-value operations O(1)\n");
                details.append("• Allows one null key\n");
                details.append("• Unordered collection\n");
                break;
            case "LinkedHashMap":
                details.append("• Hash table + linked list\n");
                details.append("• Maintains insertion order\n");
                details.append("• Fast operations O(1)\n");
                details.append("• Allows one null key\n");
                break;
            case "TreeMap":
                details.append("• Red-Black tree implementation\n");
                details.append("• Sorted by keys\n");
                details.append("• Operations O(log n)\n");
                details.append("• No null keys allowed\n");
                break;
            case "Vector":
                details.append("• Synchronized ArrayList\n");
                details.append("• Thread-safe operations\n");
                details.append("• Legacy collection\n");
                details.append("• Slower due to synchronization\n");
                break;
            case "ConcurrentHashMap":
                details.append("• Thread-safe HashMap\n");
                details.append("• Segment-level locking\n");
                details.append("• High concurrency\n");
                details.append("• No null keys/values\n");
                break;
        }
        
        detailsArea.setText(details.toString());
    }

    private void generateMethodButtons(String className) {
        methodsPanel.removeAll();
        
        try {
            Class<?> clazz = getClassForName(className);
            collectionInstance = clazz.getDeclaredConstructor().newInstance();
            updateCollectionSize();

            // Add method button
            JButton addBtn = createMethodButton("+ Add Element", ACCENT_COLOR);
            addBtn.addActionListener(e -> executeAddMethod(className));
            methodsPanel.add(addBtn);
            methodsPanel.add(Box.createVerticalStrut(10));

            // Remove method button
            JButton removeBtn = createMethodButton("- Remove Element", new Color(239, 68, 68));
            removeBtn.addActionListener(e -> executeRemoveMethod(className));
            methodsPanel.add(removeBtn);
            methodsPanel.add(Box.createVerticalStrut(10));

            // Search method button
            JButton searchBtn = createMethodButton("* Search Element", new Color(245, 158, 11));
            searchBtn.addActionListener(e -> executeSearchMethod(className));
            methodsPanel.add(searchBtn);
            methodsPanel.add(Box.createVerticalStrut(10));

            // Display method button
            JButton displayBtn = createMethodButton("/>/ View Contents", PRIMARY_COLOR);
            displayBtn.addActionListener(e -> displayCollection());
            methodsPanel.add(displayBtn);
            methodsPanel.add(Box.createVerticalStrut(10));

            // Clear method button
            JButton clearBtn = createMethodButton("🗑️ Clear All", TEXT_SECONDARY);
            clearBtn.addActionListener(e -> clearCollection());
            methodsPanel.add(clearBtn);

            methodsScrollPane.setVisible(true);
            methodsPanel.revalidate();
            methodsPanel.repaint();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error initializing collection: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private JButton createMethodButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btn.setForeground(Color.WHITE);
        btn.setBackground(color);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(color.darker());
            }
            public void mouseExited(MouseEvent e) {
                btn.setBackground(color);
            }
        });
        
        return btn;
    }

    private void executeAddMethod(String className) {
        String[] options = {"Single Value", "Bulk from CSV"};
        int choice = JOptionPane.showOptionDialog(
            this,
            "How would you like to add data?",
            "Add Data",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[0]
        );

        if (choice == -1) return;

        try {
            if (choice == 0) {
                addSingleValue(className);
            } else {
                addFromCSV(className);
            }
            updateCollectionSize();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error: " + e.getMessage(),
                "Operation Failed",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addSingleValue(String className) throws Exception {
        if (className.contains("Map")) {
            JTextField keyField = new JTextField();
            JTextField valueField = new JTextField();
            Object[] message = {
                "Key:", keyField,
                "Value:", valueField
            };
            
            int option = JOptionPane.showConfirmDialog(this, message, 
                "Add Key-Value Pair", JOptionPane.OK_CANCEL_OPTION);
            
            if (option == JOptionPane.OK_OPTION) {
                String key = keyField.getText();
                String value = valueField.getText();
                if (!key.isEmpty()) {
                    Method putMethod = collectionInstance.getClass().getMethod("put", Object.class, Object.class);
                    putMethod.invoke(collectionInstance, key, value);
                    JOptionPane.showMessageDialog(this, "Added successfully!");
                }
            }
        } else {
            String value = JOptionPane.showInputDialog(this, "Enter value:");
            if (value != null && !value.trim().isEmpty()) {
                Method addMethod = collectionInstance.getClass().getMethod("add", Object.class);
                addMethod.invoke(collectionInstance, value.trim());
                JOptionPane.showMessageDialog(this, "Added successfully!");
            }
        }
    }

    private void addFromCSV(String className) throws Exception {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Select CSV File");
        
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            BufferedReader br = new BufferedReader(new FileReader(chooser.getSelectedFile()));
            String line;
            int count = 0;
            
            if (className.contains("Map")) {
                Method putMethod = collectionInstance.getClass().getMethod("put", Object.class, Object.class);
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length >= 2) {
                        putMethod.invoke(collectionInstance, parts[0].trim(), parts[1].trim());
                        count++;
                    }
                }
            } else {
                Method addMethod = collectionInstance.getClass().getMethod("add", Object.class);
                while ((line = br.readLine()) != null) {
                    if (!line.trim().isEmpty()) {
                        addMethod.invoke(collectionInstance, line.trim());
                        count++;
                    }
                }
            }
            
            br.close();
            JOptionPane.showMessageDialog(this, 
                count + " items added from CSV",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void executeRemoveMethod(String className) {
        try {
            if (className.contains("Map")) {
                String key = JOptionPane.showInputDialog(this, "Enter key to remove:");
                if (key != null && !key.trim().isEmpty()) {
                    Method removeMethod = collectionInstance.getClass().getMethod("remove", Object.class);
                    Object removed = removeMethod.invoke(collectionInstance, key.trim());
                    if (removed != null) {
                        JOptionPane.showMessageDialog(this, "Removed: " + removed);
                    } else {
                        JOptionPane.showMessageDialog(this, "Key not found");
                    }
                }
            } else {
                String value = JOptionPane.showInputDialog(this, "Enter value to remove:");
                if (value != null && !value.trim().isEmpty()) {
                    Method removeMethod = collectionInstance.getClass().getMethod("remove", Object.class);
                    boolean removed = (Boolean) removeMethod.invoke(collectionInstance, value.trim());
                    if (removed) {
                        JOptionPane.showMessageDialog(this, "Removed successfully");
                    } else {
                        JOptionPane.showMessageDialog(this, "Value not found");
                    }
                }
            }
            updateCollectionSize();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void executeSearchMethod(String className) {
        try {
            if (className.contains("Map")) {
                String key = JOptionPane.showInputDialog(this, "Enter key to search:");
                if (key != null && !key.trim().isEmpty()) {
                    Method containsKeyMethod = collectionInstance.getClass().getMethod("containsKey", Object.class);
                    boolean found = (Boolean) containsKeyMethod.invoke(collectionInstance, key.trim());
                    
                    if (found) {
                        Method getMethod = collectionInstance.getClass().getMethod("get", Object.class);
                        Object value = getMethod.invoke(collectionInstance, key.trim());
                        JOptionPane.showMessageDialog(this, 
                            "✓ Key Found!\n\nKey: " + key.trim() + "\nValue: " + value,
                            "Search Result",
                            JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, 
                            "✗ Key not found in the collection",
                            "Search Result",
                            JOptionPane.WARNING_MESSAGE);
                    }
                }
            } else {
                String value = JOptionPane.showInputDialog(this, "Enter value to search:");
                if (value != null && !value.trim().isEmpty()) {
                    Method containsMethod = collectionInstance.getClass().getMethod("contains", Object.class);
                    boolean found = (Boolean) containsMethod.invoke(collectionInstance, value.trim());
                    
                    if (found) {
                        // For List collections, also show the index
                        String message = "✓ Value Found!";
                        if (collectionInstance instanceof List) {
                            Method indexOfMethod = collectionInstance.getClass().getMethod("indexOf", Object.class);
                            int index = (Integer) indexOfMethod.invoke(collectionInstance, value.trim());
                            message += "\n\nValue: " + value.trim() + "\nIndex: " + index;
                        } else {
                            message += "\n\nValue: " + value.trim() + " exists in the collection";
                        }
                        
                        JOptionPane.showMessageDialog(this, message,
                            "Search Result",
                            JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, 
                            "✗ Value not found in the collection",
                            "Search Result",
                            JOptionPane.WARNING_MESSAGE);
                    }
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error during search: " + e.getMessage(),
                "Search Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void displayCollection() {
        JTextArea displayArea = new JTextArea(20, 40);
        displayArea.setEditable(false);
        displayArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        displayArea.setText(collectionInstance.toString());
        
        JScrollPane scrollPane = new JScrollPane(displayArea);
        JOptionPane.showMessageDialog(this, scrollPane, 
            "Collection Contents", JOptionPane.INFORMATION_MESSAGE);
    }

    private void clearCollection() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to clear all elements?",
            "Confirm Clear",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                Method clearMethod = collectionInstance.getClass().getMethod("clear");
                clearMethod.invoke(collectionInstance);
                updateCollectionSize();
                JOptionPane.showMessageDialog(this, "Collection cleared!");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }
        }
    }

    private void updateCollectionSize() {
        try {
            Method sizeMethod = collectionInstance.getClass().getMethod("size");
            int size = (Integer) sizeMethod.invoke(collectionInstance);
            collectionSizeLabel.setText("Size: " + size + " element" + (size != 1 ? "s" : ""));
        } catch (Exception e) {
            collectionSizeLabel.setText("Size: N/A");
        }
    }

    private Class<?> getClassForName(String name) throws Exception {
        switch (name) {
            case "ArrayList": return ArrayList.class;
            case "LinkedList": return LinkedList.class;
            case "HashSet": return HashSet.class;
            case "LinkedHashSet": return LinkedHashSet.class;
            case "TreeSet": return TreeSet.class;
            case "HashMap": return HashMap.class;
            case "LinkedHashMap": return LinkedHashMap.class;
            case "TreeMap": return TreeMap.class;
            case "Vector": return Vector.class;
            case "ConcurrentHashMap": return java.util.concurrent.ConcurrentHashMap.class;
            default: return ArrayList.class;
        }
    }

    private void resetForm() {
        // Reset checkboxes
        maintainOrder.setSelected(false);
        allowDuplicates.setSelected(false);
        fastSearch.setSelected(false);
        fastInsertion.setSelected(false);
        fastDeletion.setSelected(false);
        keyValuePairs.setSelected(false);
        threadSafe.setSelected(false);
        sortedOrder.setSelected(false);
        
        // Reset display
        classNameLabel.setText("Select requirements to begin");
        collectionSizeLabel.setText("Size: 0");
        detailsArea.setText("");
        methodsPanel.removeAll();
        methodsScrollPane.setVisible(false);
        collectionInstance = null;
        
        methodsPanel.revalidate();
        methodsPanel.repaint();
    }

    // Custom shadow border for modern look
    class ShadowBorder extends AbstractBorder {
        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Draw shadow
            g2.setColor(new Color(0, 0, 0, 10));
            g2.fillRoundRect(x + 2, y + 2, width - 4, height - 4, 8, 8);
            
            g2.dispose();
        }
        
        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(2, 2, 2, 2);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CollectionAdvisor::new);
    }
}