package org.fortenike;

import java.util.*;
import java.text.SimpleDateFormat;

public class Main {
    // Define the hash table size (prime number for better distribution)
    private static final int HASH_TABLE_SIZE = 997;

    // Create array of binary trees to handle collisions
    private static TreeNode[] hashTable = new TreeNode[HASH_TABLE_SIZE];
    private static int idCounter = 1;

    // Custom hash function
    private static int hashFunction(int id) {
        return id % HASH_TABLE_SIZE;
    }

    private static void addItem(Scanner scanner) {
        System.out.print("Enter stock label: ");
        String stockLabel = scanner.nextLine();
        System.out.print("Enter brand: ");
        String brand = scanner.nextLine();
        System.out.print("Enter engine number: ");
        String engineNumber = scanner.nextLine();
        System.out.print("Enter status: ");
        String status = scanner.nextLine();

        InventoryItem item = new InventoryItem(idCounter++, new Date(), stockLabel, brand, engineNumber, status);
        int hashIndex = hashFunction(item.getId());
        hashTable[hashIndex] = insertIntoTree(hashTable[hashIndex], item);
        System.out.println("Item added successfully! Hash index: " + hashIndex);
    }

    private static TreeNode insertIntoTree(TreeNode root, InventoryItem item) {
        if (root == null) {
            return new TreeNode(item);
        }
        if (item.getId() < root.item.getId()) {
            root.left = insertIntoTree(root.left, item);
        } else {
            root.right = insertIntoTree(root.right, item);
        }
        return root;
    }

    private static void viewInventory() {
        System.out.println("\nInventory List:");
        for (int i = 0; i < HASH_TABLE_SIZE; i++) {
            inOrderTraversal(hashTable[i], i);
        }
    }

    private static void inOrderTraversal(TreeNode node, int hashIndex) {
        if (node != null) {
            inOrderTraversal(node.left, hashIndex);
            System.out.println("Hash Index: " + hashIndex + " | ID: " + node.item.getId() +
                    ", Date Entered: " + node.item.getDateEntered() +
                    ", Stock Label: " + node.item.getStockLabel() +
                    ", Brand: " + node.item.getBrand() +
                    ", Engine Number: " + node.item.getEngineNumber() +
                    ", Status: " + node.item.getStatus());
            inOrderTraversal(node.right, hashIndex);
        }
    }

    private static InventoryItem findItem(int id) {
        int hashIndex = hashFunction(id);
        return searchTree(hashTable[hashIndex], id);
    }

    private static InventoryItem searchTree(TreeNode root, int id) {
        if (root == null) {
            return null;
        }
        if (id == root.item.getId()) {
            return root.item;
        } else if (id < root.item.getId()) {
            return searchTree(root.left, id);
        } else {
            return searchTree(root.right, id);
        }
    }

    private static void updateItem(Scanner scanner) {
        System.out.print("Enter ID of item to update: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        InventoryItem item = findItem(id);
        if (item != null) {
            System.out.print("Enter new stock label (leave blank to keep current): ");
            String newLabel = scanner.nextLine();
            if (!newLabel.isEmpty()) item.setStockLabel(newLabel);

            System.out.print("Enter new brand (leave blank to keep current): ");
            String newBrand = scanner.nextLine();
            if (!newBrand.isEmpty()) item.setBrand(newBrand);

            System.out.print("Enter new engine number (leave blank to keep current): ");
            String newEngine = scanner.nextLine();
            if (!newEngine.isEmpty()) item.setEngineNumber(newEngine);

            System.out.print("Enter new status (leave blank to keep current): ");
            String newStatus = scanner.nextLine();
            if (!newStatus.isEmpty()) item.setStatus(newStatus);

            System.out.println("Item updated successfully!");
        } else {
            System.out.println("Item with ID " + id + " not found.");
        }
    }

    private static void deleteItem(Scanner scanner) {
        System.out.print("Enter ID of item to delete: ");
        int id = scanner.nextInt();

        int hashIndex = hashFunction(id);
        hashTable[hashIndex] = deleteFromTree(hashTable[hashIndex], id);
    }

    private static TreeNode deleteFromTree(TreeNode root, int id) {
        if (root == null) {
            return null;
        }
        if (id < root.item.getId()) {
            root.left = deleteFromTree(root.left, id);
        } else if (id > root.item.getId()) {
            root.right = deleteFromTree(root.right, id);
        } else {
            if (root.left == null) {
                return root.right;
            } else if (root.right == null) {
                return root.left;
            }
            root.item = findMin(root.right).item;
            root.right = deleteFromTree(root.right, root.item.getId());
        }
        return root;
    }

    private static TreeNode findMin(TreeNode root) {
        while (root.left != null) {
            root = root.left;
        }
        return root;
    }

    private static List<InventoryItem> getAllItems() {
        List<InventoryItem> itemList = new ArrayList<>();
        for (TreeNode root : hashTable) {
            inOrderCollect(root, itemList);
        }
        return itemList;
    }

    private static void inOrderCollect(TreeNode node, List<InventoryItem> itemList) {
        if (node != null) {
            inOrderCollect(node.left, itemList);
            itemList.add(node.item);
            inOrderCollect(node.right, itemList);
        }
    }

    private static void sortMenu(Scanner scanner) {
        List<InventoryItem> itemList = getAllItems();
        int choice;

        do {
            System.out.println("\nSort using available information as criteria");
            System.out.println("1. Sort by ID");
            System.out.println("2. Sort by Date Entered");
            System.out.println("3. Sort by Stock Label");
            System.out.println("4. Sort by Brand");
            System.out.println("5. Sort by Engine Number");
            System.out.println("6. Sort by Status");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    itemList.sort(Comparator.comparingInt(InventoryItem::getId));
                    break;
                case 2:
                    itemList.sort(Comparator.comparing(InventoryItem::getDateEntered));
                    break;
                case 3:
                    itemList.sort(Comparator.comparing(InventoryItem::getStockLabel));
                    break;
                case 4:
                    itemList.sort(Comparator.comparing(InventoryItem::getBrand));
                    break;
                case 5:
                    itemList.sort(Comparator.comparing(InventoryItem::getEngineNumber));
                    break;
                case 6:
                    itemList.sort(Comparator.comparing(InventoryItem::getStatus));
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid choice, please try again.");
                    continue;
            }

            for (InventoryItem item : itemList) {
                System.out.println(item);
            }
        } while (choice != 0);
    }

    // Search methods modified to use hash table
    private static void searchMenu(Scanner scanner) {
        int choice;
        do {
            System.out.println("\nSearch using available information as criteria");
            System.out.println("1. Search by ID");
            System.out.println("2. Search by Date Entered");
            System.out.println("3. Search by Stock Label");
            System.out.println("4. Search by Brand");
            System.out.println("5. Search by Engine Number");
            System.out.println("6. Search by Status");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    searchID(scanner);
                    break;
                case 2:
                    searchDateEntered(scanner);
                    break;
                case 3:
                    searchStockLabel(scanner);
                    break;
                case 4:
                    searchBrand(scanner

                    );
                    break;
                case 5:
                    searchEngineNumber(scanner);
                    break;
                case 6:
                    searchStatus(scanner);
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid choice, please try again.");
            }
        } while (choice != 0);
    }

    private static void searchID(Scanner scanner) {
        System.out.print("Enter ID to search: ");
        int id = scanner.nextInt();
        InventoryItem item = findItem(id);
        if (item != null) {
            System.out.println("Found: " + item);
        } else {
            System.out.println("No item found with ID: " + id);
        }
    }

    private static void searchDateEntered(Scanner scanner) {
        System.out.print("Enter Date Entered to search (yyyy-MM-dd): ");
        String dateStr = scanner.nextLine();
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
            List<InventoryItem> itemList = getAllItems();
            for (InventoryItem item : itemList) {
                if (item.getDateEntered().equals(date)) {
                    System.out.println("Found: " + item);
                }
            }
        } catch (Exception e) {
            System.out.println("Invalid date format.");
        }
    }

    private static void searchStockLabel(Scanner scanner) {
        System.out.print("Enter Stock Label to search: ");
        String stockLabel = scanner.nextLine();
        List<InventoryItem> itemList = getAllItems();
        for (InventoryItem item : itemList) {
            if (item.getStockLabel().equalsIgnoreCase(stockLabel)) {
                System.out.println("Found: " + item);
            }
        }
    }

    private static void searchBrand(Scanner scanner) {
        System.out.print("Enter Brand to search: ");
        String brand = scanner.nextLine();
        List<InventoryItem> itemList = getAllItems();
        for (InventoryItem item : itemList) {
            if (item.getBrand().equalsIgnoreCase(brand)) {
                System.out.println("Found: " + item);
            }
        }
    }

    private static void searchEngineNumber(Scanner scanner) {
        System.out.print("Enter Engine Number to search: ");
        String engineNumber = scanner.nextLine();
        List<InventoryItem> itemList = getAllItems();
        for (InventoryItem item : itemList) {
            if (item.getEngineNumber().equalsIgnoreCase(engineNumber)) {
                System.out.println("Found: " + item);
            }
        }
    }

    private static void searchStatus(Scanner scanner) {
        System.out.print("Enter Status to search: ");
        String status = scanner.nextLine();
        List<InventoryItem> itemList = getAllItems();
        for (InventoryItem item : itemList) {
            if (item.getStatus().equalsIgnoreCase(status)) {
                System.out.println("Found: " + item);
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\nInventory Management System");
            System.out.println("1. Add Item");
            System.out.println("2. View Inventory");
            System.out.println("3. Update Item");
            System.out.println("4. Delete Item");
            System.out.println("5. Sort Item");
            System.out.println("6. Search Item");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    addItem(scanner);
                    break;
                case 2:
                    viewInventory();
                    break;
                case 3:
                    updateItem(scanner);
                    break;
                case 4:
                    deleteItem(scanner);
                    break;
                case 5:
                    sortMenu(scanner);
                    break;
                case 6:
                    searchMenu(scanner);
                    break;
                case 0:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice, please try again.");
            }
        } while (choice != 0);

        scanner.close();
    }
}

class TreeNode {
    InventoryItem item;
    TreeNode left, right;

    TreeNode(InventoryItem item) {
        this.item = item;
        this.left = this.right = null;
    }
}

class InventoryItem {
    private int id;
    private Date dateEntered;
    private String stockLabel;
    private String brand;
    private String engineNumber;
    private String status;

    public InventoryItem(int id, Date dateEntered, String stockLabel, String brand, String engineNumber, String status) {
        this.id = id;
        this.dateEntered = dateEntered;
        this.stockLabel = stockLabel;
        this.brand = brand;
        this.engineNumber = engineNumber;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public Date getDateEntered() {
        return dateEntered;
    }

    public String getStockLabel() {
        return stockLabel;
    }

    public void setStockLabel(String stockLabel) {
        this.stockLabel = stockLabel;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getEngineNumber() {
        return engineNumber;
    }

    public void setEngineNumber(String engineNumber) {
        this.engineNumber = engineNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ID: " + id +
                ", Date Entered: " + dateEntered +
                ", Stock Label: " + stockLabel +
                ", Brand: " + brand +
                ", Engine Number: " + engineNumber +
                ", Status: " + status;
    }
}