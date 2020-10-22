import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int[] box1 = new int[] {scanner.nextInt(), scanner.nextInt(), scanner.nextInt()};
        int[] box2 = new int[] {scanner.nextInt(), scanner.nextInt(), scanner.nextInt()};

        Arrays.sort(box1);
        Arrays.sort(box2);

        if (box1[0] > box2[0] && box1[1] > box2[1] && box1[2] > box2[2]) {
            System.out.println("Box 1 > Box 2");
        } else if (box1[0] < box2[0] && box1[1] < box2[1] && box1[2] < box2[2]) {
            System.out.println("Box 1 < Box 2");
        } else {
            System.out.println("Incompatible");
        }
    }
}