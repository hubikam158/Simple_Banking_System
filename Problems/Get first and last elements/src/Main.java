import java.util.*;

public class Main {

    // write a method here
    public static int[] getFirstAndLast(int[] ints) {
        return new int[] {ints[0], ints[ints.length - 1]};
    }

    /* Do not change code below */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int[] array = Arrays.stream(scanner.nextLine().split(" "))
                .mapToInt(Integer::parseInt)
                .toArray();
        int[] result = getFirstAndLast(array);
        Arrays.stream(result).forEach(e -> System.out.print(e + " "));
    }
}