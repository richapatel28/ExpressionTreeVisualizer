import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.*;

// Node class for Expression Tree
class Node {
    String value;
    Node left, right;
    
    Node(String value) {
        this.value = value;
        this.left = this.right = null;
    }
    
    boolean isOperator() {
        return value.equals("+") || value.equals("-") || 
               value.equals("*") || value.equals("/") || value.equals("^");
    }
}

// Main Expression Tree class
class ExpressionTree {
    private Node root;
    
    // Convert infix to postfix using stack
    public String infixToPostfix(String infix) {
        StringBuilder postfix = new StringBuilder();
        Stack<Character> stack = new Stack<>();
        
        for (int i = 0; i < infix.length(); i++) {
            char c = infix.charAt(i);
            
            // Skip whitespace
            if (c == ' ') continue;
            
            // If operand (digit or multi-digit number)
            if (Character.isDigit(c)) {
                while (i < infix.length() && (Character.isDigit(infix.charAt(i)) || infix.charAt(i) == '.')) {
                    postfix.append(infix.charAt(i++));
                }
                postfix.append(' ');
                i--;
            }
            // If opening bracket
            else if (c == '(') {
                stack.push(c);
            }
            // If closing bracket
            else if (c == ')') {
                while (!stack.isEmpty() && stack.peek() != '(') {
                    postfix.append(stack.pop()).append(' ');
                }
                if (!stack.isEmpty()) stack.pop(); // Remove '('
            }
            // If operator
            else {
                while (!stack.isEmpty() && precedence(c) <= precedence(stack.peek())) {
                    postfix.append(stack.pop()).append(' ');
                }
                stack.push(c);
            }
        }
        
        // Pop remaining operators
        while (!stack.isEmpty()) {
            postfix.append(stack.pop()).append(' ');
        }
        
        return postfix.toString().trim();
    }
    
    // Determine operator precedence
    private int precedence(char op) {
        switch (op) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
                return 2;
            case '^':
                return 3;
            default:
                return -1;
        }
    }
    
    // Construct expression tree from postfix
    public Node constructTree(String postfix) {
        Stack<Node> stack = new Stack<>();
        String[] tokens = postfix.split("\\s+");
        
        for (String token : tokens) {
            Node node = new Node(token);
            
            // If operator, pop two nodes and make them children
            if (node.isOperator()) {
                node.right = stack.pop();
                node.left = stack.pop();
            }
            
            stack.push(node);
        }
        
        root = stack.isEmpty() ? null : stack.pop();
        return root;
    }
    
    // Evaluate expression tree recursively (postorder)
    public double evaluate(Node node) {
        if (node == null) return 0;
        
        // If leaf node (operand)
        if (!node.isOperator()) {
            return Double.parseDouble(node.value);
        }
        
        // Recursively evaluate left and right subtrees
        double left = evaluate(node.left);
        double right = evaluate(node.right);
        
        // Apply operator
        switch (node.value) {
            case "+": return left + right;
            case "-": return left - right;
            case "*": return left * right;
            case "/": return left / right;
            case "^": return Math.pow(left, right);
            default: return 0;
        }
    }
    
    // Print tree structure (console visualization)
    public void printTree(Node node, String prefix, boolean isLeft) {
        if (node == null) return;
        
        System.out.println(prefix + (isLeft ? "├── " : "└── ") + node.value);
        
        if (node.left != null || node.right != null) {
            if (node.left != null) {
                printTree(node.left, prefix + (isLeft ? "│   " : "    "), true);
            } else {
                System.out.println(prefix + (isLeft ? "│   " : "    ") + "├── null");
            }
            
            if (node.right != null) {
                printTree(node.right, prefix + (isLeft ? "│   " : "    "), false);
            } else {
                System.out.println(prefix + (isLeft ? "│   " : "    ") + "└── null");
            }
        }
    }
    
    public Node getRoot() {
        return root;
    }
}

// GUI Panel for tree visualization
class TreePanel extends JPanel {
    private Node root;
    private Map<Node, Point> nodePositions;
    private Node highlightedNode;
    private static final int NODE_RADIUS = 30;
    private static final int LEVEL_GAP = 80;
    
    public TreePanel() {
        nodePositions = new HashMap<>();
        setPreferredSize(new Dimension(850, 500));
        setBackground(Color.WHITE);
    }
    
    public void setTree(Node root) {
        this.root = root;
        nodePositions.clear();
        if (root != null) {
            calculatePositions(root, getWidth() / 2, 50, getWidth() / 4);
        }
        repaint();
    }
    
    public void highlightNode(Node node) {
        this.highlightedNode = node;
        repaint();
    }
    
    private void calculatePositions(Node node, int x, int y, int xOffset) {
        if (node == null) return;
        
        nodePositions.put(node, new Point(x, y));
        
        if (node.left != null) {
            calculatePositions(node.left, x - xOffset, y + LEVEL_GAP, xOffset / 2);
        }
        if (node.right != null) {
            calculatePositions(node.right, x + xOffset, y + LEVEL_GAP, xOffset / 2);
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        if (root == null) return;
        
        // Draw edges first
        drawEdges(g2, root);
        
        // Draw nodes
        drawNodes(g2, root);
    }
    
    private void drawEdges(Graphics2D g2, Node node) {
        if (node == null) return;
        
        Point nodePos = nodePositions.get(node);
        g2.setStroke(new BasicStroke(2.5f));
        g2.setColor(new Color(100, 100, 100));
        
        if (node.left != null) {
            Point leftPos = nodePositions.get(node.left);
            g2.drawLine(nodePos.x, nodePos.y, leftPos.x, leftPos.y);
            drawEdges(g2, node.left);
        }
        
        if (node.right != null) {
            Point rightPos = nodePositions.get(node.right);
            g2.drawLine(nodePos.x, nodePos.y, rightPos.x, rightPos.y);
            drawEdges(g2, node.right);
        }
    }
    
    private void drawNodes(Graphics2D g2, Node node) {
        if (node == null) return;
        
        Point pos = nodePositions.get(node);
        
        // Simple solid colors
        Color nodeColor;
        if (node == highlightedNode) {
            nodeColor = new Color(255, 215, 0); // Gold
        } else if (node.isOperator()) {
            nodeColor = new Color(100, 149, 237); // Cornflower blue
        } else {
            nodeColor = new Color(60, 179, 113); // Medium sea green
        }
        
        // Draw simple circle
        g2.setColor(nodeColor);
        g2.fillOval(pos.x - NODE_RADIUS, pos.y - NODE_RADIUS, NODE_RADIUS * 2, NODE_RADIUS * 2);
        
        // Draw border
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(2));
        g2.drawOval(pos.x - NODE_RADIUS, pos.y - NODE_RADIUS, NODE_RADIUS * 2, NODE_RADIUS * 2);
        
        // Draw text
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 16));
        FontMetrics fm = g2.getFontMetrics();
        int textWidth = fm.stringWidth(node.value);
        int textHeight = fm.getAscent();
        g2.drawString(node.value, pos.x - textWidth / 2, pos.y + textHeight / 4);
        
        // Recursively draw children
        drawNodes(g2, node.left);
        drawNodes(g2, node.right);
    }
}

// Main application
public class ExpressionTreeVisualizer extends JFrame {
    private ExpressionTree tree;
    private TreePanel treePanel;
    private JTextField inputField;
    private JTextArea outputArea;
    
    public ExpressionTreeVisualizer() {
        tree = new ExpressionTree();
        setupGUI();
    }
    
    private void setupGUI() {
        setTitle("Expression Tree Visualizer - DSA Project");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(5, 5));
        
        // Simple color scheme
        Color headerColor = new Color(70, 130, 180); // Steel blue
        Color buttonColor = new Color(100, 149, 237); // Cornflower blue
        
        // Top panel for input
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setBackground(headerColor);
        topPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Title label
        JLabel titleLabel = new JLabel("Expression Tree Visualizer");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        
        // Input label
        JLabel label = new JLabel("Enter Expression:");
        label.setFont(new Font("Arial", Font.PLAIN, 13));
        label.setForeground(Color.WHITE);
        
        // Input field
        inputField = new JTextField("(3+5)*(2-8)");
        inputField.setFont(new Font("Monospaced", Font.PLAIN, 14));
        inputField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        
        // Simple buttons
        JButton visualizeBtn = new JButton("Visualize");
        JButton evaluateBtn = new JButton("Step-by-Step");
        JButton clearBtn = new JButton("Clear");
        
        visualizeBtn.setBackground(buttonColor);
        visualizeBtn.setForeground(Color.BLACK);
        visualizeBtn.setFocusPainted(false);
        
        evaluateBtn.setBackground(new Color(60, 179, 113));
        evaluateBtn.setForeground(Color.BLACK);
        evaluateBtn.setFocusPainted(false);
        
        clearBtn.setBackground(new Color(220, 90, 90));
        clearBtn.setForeground(Color.black);
        clearBtn.setFocusPainted(false);
        
        visualizeBtn.addActionListener(e -> visualizeExpression());
        evaluateBtn.addActionListener(e -> evaluateStepByStep());
        clearBtn.addActionListener(e -> {
            inputField.setText("");
            outputArea.setText("");
            treePanel.setTree(null);
        });
        
        // Input panel
        JPanel inputPanel = new JPanel(new BorderLayout(5, 5));
        inputPanel.setOpaque(false);
        inputPanel.add(titleLabel, BorderLayout.NORTH);
        
        JPanel fieldPanel = new JPanel(new BorderLayout(5, 5));
        fieldPanel.setOpaque(false);
        fieldPanel.add(label, BorderLayout.NORTH);
        fieldPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(fieldPanel, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));
        buttonPanel.setOpaque(false);
        buttonPanel.add(visualizeBtn);
        buttonPanel.add(evaluateBtn);
        buttonPanel.add(clearBtn);
        
        topPanel.add(inputPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Center panel for tree visualization
        treePanel = new TreePanel();
        treePanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            "Tree Structure",
            javax.swing.border.TitledBorder.CENTER,
            javax.swing.border.TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 12)
        ));
        JScrollPane scrollPane = new JScrollPane(treePanel);
        
        // Bottom panel for output
        JPanel outputPanel = new JPanel(new BorderLayout(5, 5));
        outputPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
        
        JLabel outputLabel = new JLabel("Output:");
        outputLabel.setFont(new Font("Arial", Font.BOLD, 13));
        
        outputArea = new JTextArea(10, 50);
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        outputArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        JScrollPane outputScroll = new JScrollPane(outputArea);
        outputScroll.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        
        outputPanel.add(outputLabel, BorderLayout.NORTH);
        outputPanel.add(outputScroll, BorderLayout.CENTER);
        
        // Add example expressions panel
        JPanel examplesPanel = createExamplesPanel();
        
        JPanel mainTop = new JPanel(new BorderLayout());
        mainTop.add(topPanel, BorderLayout.CENTER);
        mainTop.add(examplesPanel, BorderLayout.SOUTH);
        
        // Add panels to frame
        add(mainTop, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(outputPanel, BorderLayout.SOUTH);
        
        setSize(900, 750);
        setLocationRelativeTo(null);
    }
    
    private JPanel createExamplesPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        panel.setBackground(new Color(240, 240, 240));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, Color.GRAY),
            BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        
        JLabel exLabel = new JLabel("Try examples:");
        exLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        panel.add(exLabel);
        
        String[] examples = {
            "(3+5)*(2-8)",
            "2^3+5*4",
            "((15/(7-(1+1)))*3)-(2+(1+1))",
            "10+20*30/2"
        };
        
        for (String ex : examples) {
            JButton exBtn = new JButton(ex);
            exBtn.setFont(new Font("Monospaced", Font.PLAIN, 10));
            exBtn.setFocusPainted(false);
            exBtn.addActionListener(e -> inputField.setText(ex));
            panel.add(exBtn);
        }
        
        return panel;
    }
    
    private void visualizeExpression() {
        try {
            String infix = inputField.getText().trim();
            outputArea.setText("");
            
            // Convert to postfix
            String postfix = tree.infixToPostfix(infix);
            outputArea.append("Infix Expression: " + infix + "\n");
            outputArea.append("Postfix Expression: " + postfix + "\n\n");
            
            // Construct tree
            Node root = tree.constructTree(postfix);
            treePanel.setTree(root);
            
            outputArea.append("Expression Tree Structure:\n");
            tree.printTree(root, "", false);
            
            // Evaluate
            double result = tree.evaluate(root);
            outputArea.append("\n\nResult: " + result + "\n");
            
        } catch (Exception ex) {
            outputArea.setText("Error: " + ex.getMessage());
        }
    }
    
    private void evaluateStepByStep() {
        try {
            String infix = inputField.getText().trim();
            String postfix = tree.infixToPostfix(infix);
            Node root = tree.constructTree(postfix);
            treePanel.setTree(root);
            
            outputArea.setText("Step-by-Step Evaluation (Postorder Traversal):\n\n");
            
            // Create a thread to animate evaluation
            new Thread(() -> {
                evaluateWithAnimation(root, 0);
            }).start();
            
        } catch (Exception ex) {
            outputArea.setText("Error: " + ex.getMessage());
        }
    }
    
    private double evaluateWithAnimation(Node node, int depth) {
        if (node == null) return 0;
        
        try {
            Thread.sleep(800);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            treePanel.highlightNode(node);
            outputArea.append("  ".repeat(depth) + "Visiting: " + node.value + "\n");
        });
        
        if (!node.isOperator()) {
            double value = Double.parseDouble(node.value);
            SwingUtilities.invokeLater(() -> {
                outputArea.append("  ".repeat(depth) + "  → Operand value: " + value + "\n\n");
            });
            return value;
        }
        
        double left = evaluateWithAnimation(node.left, depth + 1);
        double right = evaluateWithAnimation(node.right, depth + 1);
        
        double result = 0;
        switch (node.value) {
            case "+": result = left + right; break;
            case "-": result = left - right; break;
            case "*": result = left * right; break;
            case "/": result = left / right; break;
            case "^": result = Math.pow(left, right); break;
        }
        
        final double finalResult = result;
        SwingUtilities.invokeLater(() -> {
            outputArea.append("  ".repeat(depth) + "  → Computing: " + left + " " + 
                            node.value + " " + right + " = " + finalResult + "\n\n");
        });
        
        return result;
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ExpressionTreeVisualizer visualizer = new ExpressionTreeVisualizer();
            visualizer.setVisible(true);
        });
    }
}