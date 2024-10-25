import java.util.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.stream.Collectors;

public class InventoryManagementSystem {

    // Create a hash map for quick access
    private static Map<Integer, InventoryItem> inventoryMap = new HashMap<>();
    // Create the root of the BST
    private static TreeNode bstRoot = null;
    private static int idCounter = 1;

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
        // Add item to the hash map
        inventoryMap.put(item.getId(), item);
        // Add item to the BST
        bstRoot = insertIntoBST(bstRoot, item);
        System.out.println("Item added successfully! ID: " + item.getId());
    }

    private static TreeNode insertIntoBST(TreeNode root, InventoryItem item) {
        if (root == null) {
            return new TreeNode(item);
        }
        if (item.getId() < root.item.getId()) {
            root.left = insertIntoBST(root.left, item);
        } else {
            root.right = insertIntoBST(root.right, item);
        }
        return root;
    }

   private static void viewInventory() {
    System.out.println("\nInventory List:");
    System.out.println("1. Sort by Brand (Ascending)");
    System.out.println("2. Display All Items");
    System.out.print("Enter your choice (1 or 2): ");

    Scanner scanner = new Scanner(System.in);
    int choice = scanner.nextInt();
    scanner.nextLine();

    List<InventoryItem> items = new ArrayList<>(inventoryMap.values());

    if (choice == 1) {
        // Sort by brand using a custom comparator
        items.sort(Comparator.comparing(InventoryItem::getBrand));
        System.out.println("\nInventory List (Sorted by Brand):");
    }

    // Print the list (sorted or unsorted)
    for (InventoryItem item : items) {
        System.out.println(item);
    }
}

    private static void inOrderTraversal(TreeNode node) {
        if (node != null) {
            inOrderTraversal(node.left);
            System.out.println(node.item);
            inOrderTraversal(node.right);
        }
    }

    private static InventoryItem searchItem(int id) {
        return inventoryMap.get(id); // O(1) lookup
    }

    private static InventoryItem searchItem(Scanner scanner) {
    System.out.println("Enter search criteria [ Y / N ]:");
    String choice = scanner.nextLine().toLowerCase();

        switch (choice) {
            case "y":
                System.out.print("Stock Label (leave blank to ignore): ");
                String stockLabel = scanner.nextLine();
                if (stockLabel.isEmpty());
                
                System.out.print("Brand (leave blank to ignore): ");
                String brand = scanner.nextLine();
                if (brand.isEmpty());
                
                System.out.print("Engine Number (leave blank to ignore): ");
                String engineNumber = scanner.nextLine();
                if (engineNumber.isEmpty());
                
                System.out.print("Status (leave blank to ignore): ");
                String status = scanner.nextLine();
                
                // Find the item using multiple criteria
                List<InventoryItem> foundItems = inventoryMap.values().stream()
                    .filter(item -> (stockLabel.isEmpty() || item.getStockLabel().contains(stockLabel)) &&
                        (brand.isEmpty() || item.getBrand().contains(brand)) &&
                        (engineNumber.isEmpty() || item.getEngineNumber().contains(engineNumber)) &&
                        (status.isEmpty() || item.getStatus().contains(status)))
                    .collect(Collectors.toList());

                // Print all found items (if any)
                if (!foundItems.isEmpty()) {
                  System.out.println("Found Items:");
                  for (InventoryItem item : foundItems) {
                    System.out.println(item);
                  }
                } else {
                  System.out.println("No items found matching the criteria.");
                } break;
                
                
            case "n":
                System.out.print("Enter ID of item to search: ");
                int id = scanner.nextInt();
                InventoryItem item = searchItem(id);
                if (item != null) {
                    System.out.println("Item found: " + item);
                } else {
                    System.out.println("Item with ID " + id + " not found.");
                }       break;
            default:
                System.out.println("Wrong Input.");
                break;
        }

    return null;
}

    private static void updateItem(Scanner scanner) {
        System.out.print("Enter ID of item to update: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        InventoryItem item = searchItem(id);
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

        if (inventoryMap.remove(id) != null) {
            bstRoot = deleteFromBST(bstRoot, id);
            System.out.println("Item deleted successfully!");
        } else {
            System.out.println("Item with ID " + id + " not found.");
        }
    }

    private static TreeNode deleteFromBST(TreeNode root, int id) {
        if (root == null) {
            return null;
        }
        if (id < root.item.getId()) {
            root.left = deleteFromBST(root.left, id);
        } else if (id > root.item.getId()) {
            root.right = deleteFromBST(root.right, id);
        } else {
            if (root.left == null) {
                return root.right;
            } else if (root.right == null) {
                return root.left;
            }
            root.item = findMin(root.right).item;
            root.right = deleteFromBST(root.right, root.item.getId());
        }
        return root;
    }

    private static TreeNode findMin(TreeNode root) {
        while (root.left != null) {
            root = root.left;
        }
        return root;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\nInventory Management System");
            System.out.println("1. Add Item");
            System.out.println("2. View Inventory");
            System.out.println("3. Search Item");
            System.out.println("4. Update Item");
            System.out.println("5. Delete Item");
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
                    searchItem(scanner);
                    break;
                case 4:
                    updateItem(scanner);
                    break;
                case 5:
                    deleteItem(scanner);
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
