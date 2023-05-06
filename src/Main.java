import java.util.Scanner;
import java.util.stream.StreamSupport;

public class Main {
    private static Scanner sc;
    public static void main(String[] args) {
        sc = new Scanner(System.in);
        int menuItem;
        AVLTree example = new AVLTree();
        Node root = null;
        int temp;

        while((menuItem = menu()) != 0)
            switch(menuItem)
            {
                case 1:
                    System.out.println("Введите ключ узла для добавления");
                    temp = sc.nextInt();
                    if (root == null)
                    {
                        root = new Node(temp);
                        example.setRoot(root);
                    }
                    else example.insert(example.getRoot(), temp);
                    break;
                case 2:
                    System.out.println("Введите ключ узла для удаления");
                    temp = sc.nextInt();
                    if (example.find(temp) != null)
                        example.delete(example.getRoot(), temp);
                    else System.out.println("Такого ключа нет!");
                    break;
                case 3:

                    System.out.println("Введите ключ для поиска");
                    temp = sc.nextInt();

                    if (example.find(temp) != null)
                        example.find(temp).print();
                    else System.out.println("Такого ключа нет!");
                    break;
                case 4:
                    if(example.getRoot() != null)
                        example.printTree(example.getRoot(), "");
                    else System.out.println("Дерево пустое!");
                    break;
            }

    }

    public static int menu()
    {
        System.out.println("\n------------------------------------------------");
        System.out.println("1 - Добавить узел");
        System.out.println("2 - Удалить узел");
        System.out.println("3 - Найти узел");
        System.out.println("4 - Вывести АВЛ-дерево");
        System.out.println("0 - Завершить работу программы");
        System.out.println("------------------------------------------------");
        System.out.print("Ваш выбор? ");
        int a = sc.nextInt();
        sc.nextLine();
        return a;
    }

    public static class Node {
        int key;
        Node left;
        Node right;

        int height;

        void print(){
            System.out.println("Ключ: " + this.key);
            if (right != null)
            System.out.println("Правый потомок: " + right.key);
            if (left != null)
            System.out.println("Левый потомок: " + left.key);
        }
        public Node(int data) {
            this.key = data;
        }
        public Node(){}
    }

    public static class AVLTree {

        private Node root;

        Node getRoot() {
        return root;
        }
        void setRoot(Node root)
        {
            this.root = root;
        }
        void updateHeight(Node n) {
            n.height = 1 + Math.max(height(n.left), height(n.right));
        }

        int height(Node n) {
            return n == null ? -1 : n.height;
        }

        int getBalance(Node n) {
            return (n == null) ? 0 : height(n.right) - height(n.left);
        }

        Node rotateRight(Node y) {
            Node x = y.left;
            Node z = x.right;
            x.right = y;
            y.left = z;
            updateHeight(y);
            updateHeight(x);
            return x;
        }

        Node rotateLeft(Node y) {
            Node x = y.right;
            Node z = x.left;
            x.left = y;
            y.right = z;
            updateHeight(y);
            updateHeight(x);
            return x;
        }

        Node rebalance(Node z) {
            updateHeight(z);
            int balance = getBalance(z);
            if (balance > 1) {
                if (height(z.right.right) > height(z.right.left)) {
                    z = rotateLeft(z);
                } else {
                    z.right = rotateRight(z.right);
                    z = rotateLeft(z);
                }
            } else if (balance < -1) {
                if (height(z.left.left) > height(z.left.right))
                    z = rotateRight(z);
                else {
                    z.left = rotateLeft(z.left);
                    z = rotateRight(z);
                }
            }
            return z;
        }

        Node insert(Node node, int key) {
            if (node == null) {
                return new Node(key);
            } else if (node.key > key) {
                node.left = insert(node.left, key);
            } else if (node.key < key) {
                node.right = insert(node.right, key);
            } else {
                throw new RuntimeException("duplicate Key!");
            }
            return rebalance(node);
        }

        Node find(int key) {
            Node current = root;
            while (current != null) {
                if (current.key == key) {
                    break;
                }
                current = current.key < key ? current.right : current.left;
            }
            return current;
        }

        public Node delete(Node root, int key) {
            if (root == null) {
                return root;
            }
            if (key < root.key) {
                root.left = delete(root.left, key);
            } else if (key > root.key) {
                root.right = delete(root.right, key);
            } else {
                if ((root.left == null) || (root.right == null)) {
                    Node temp = null;
                    if (temp == root.left) {
                        temp = root.right;
                    } else {
                        temp = root.left;
                    }
                    if (temp == null) {
                        temp = root;
                        root = null;
                    } else {
                        root = temp;
                    }
                } else {
                    Node temp = minValueNode(root.right);
                    root.key = temp.key;
                    root.right = delete(root.right, temp.key);
                }
            }
            if (root == null) {
                return root;
            }
            root.height = Math.max(height(root.left), height(root.right)) + 1;
            int balance = getBalance(root);
            if (balance > 1 && getBalance(root.left) >= 0) {
                return rotateRight(root);
            }
            if (balance > 1 && getBalance(root.left) < 0) {
                root.left = rotateLeft(root.left);
                return rotateRight(root);
            }
            if (balance < -1 && getBalance(root.right) <= 0) {
                return rotateLeft(root);
            }
            if (balance < -1 && getBalance(root.right) > 0) {
                root.right = rotateRight(root.right);
                return rotateLeft(root);
            }
            return root;
        }

        private Node minValueNode(Node node) {
            Node current = node;
            while (current.left != null) {
                current = current.left;
            }
            return current;
        }

        public void printTree(Node node, String prefix) {
            if (node != null) {
                printTree(node.right, prefix + "    ");
                System.out.println(prefix + "+-- " + node.key);
                printTree(node.left, prefix + "    ");
            }

        }
    }
}



