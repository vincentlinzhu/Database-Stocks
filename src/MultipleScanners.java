import java.util.Scanner;

public class MultipleScanners {
    public static void main(String[] args) {
        // First Scanner instance
        Scanner scanner1 = new Scanner(System.in);

        System.out.print("Enter input for Scanner 1: ");
        one(scanner1);

        // Close the Scanners when done
        scanner1.close();
    }
    public static void one (Scanner sc) {
        String input1 = sc.nextLine();
        System.out.println("Scanner 1 input: " + input1);

        // Second Scanner instance
        Scanner scanner2 = new Scanner(System.in);

        System.out.print("Enter input for Scanner 2: ");
        String input2 = scanner2.nextLine();
        System.out.println("Scanner 2 input: " + input2);
        scanner2.close();
    }
}