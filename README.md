# 🌳 Expression Tree Visualizer

A **JavaFX-based Expression Tree Visualizer** project developed as part of the **Data Structures and Algorithms (DSA)** .  
This standalone desktop application demonstrates the construction and visualization of **expression trees** — an essential concept in compiler design and data structures.

---

## 📘 Project Overview

The **Expression Tree Visualizer** allows users to:
- Input an **infix expression** (e.g., `(A+B)*C`).
- Convert it into **postfix form** automatically.
- Build and display the corresponding **expression tree** structure.
- Visualize the **traversals** — Inorder, Preorder, and Postorder.
- Understand how operators and operands are arranged in hierarchical form.

---

## 🛠️ Technologies Used

| Component | Technology |
|------------|-------------|
| Language | Java (JDK 17 or above recommended) |
| GUI Framework | JavaFX |
| Database (optional) | MySQL (via JDBC) |
| IDE Used | IntelliJ IDEA / VS Code / NetBeans / Eclipse |
| Version Control | Git & GitHub |

---

## 🧩 Features

✅ Infix to Postfix Expression Conversion  
✅ Expression Tree Construction  
✅ Graphical Tree Visualization using JavaFX  
✅ Traversal Operations (Inorder, Preorder, Postorder)  
✅ Clear Tree & Reset Options  
✅ Optional: Save user expressions in a database using JDBC  

---

## 📂 Project Structure

Dsa_project/
│
├── src/
│ ├── Main.java
│ ├── ExpressionTree.java
│ ├── Node.java
│ ├── ExpressionConverter.java
│ └── TreeVisualizer.java
│
├── assets/
│ └── icons, images (if any)
│
├── database/
│ └── db_connection.sql (optional)
│
└── README.md

yaml
Copy code

---

## 🚀 How to Run

    1. **Clone or download** this repository  
       ```bash
       git clone https://github.com/richapatel28/ExpressionTreeVisualizer.git
       
    2. Open the project in your preferred IDE (like VS Code or IntelliJ IDEA).
    
    
    3. Set up JavaFX:
    
    Add JavaFX SDK path to your run configuration.
    Example VM arguments:
    cpp
    Copy code
    --module-path "C:\javafx-sdk\lib" --add-modules javafx.controls,javafx.fxml
    
    4. Run the Main.java file.

🧠 DSA Concepts Covered :

Stack-based expression conversion (Infix → Postfix)
Binary Tree construction
Tree Traversals (Inorder, Preorder, Postorder)
Recursion
Graphical representation of data structures

🎯 Learning Outcome :

This project strengthens the understanding of:
Data structure implementation in real applications.
Visualizing how compilers interpret mathematical expressions.
Integrating frontend (JavaFX) with backend logic (Java + DSA).


