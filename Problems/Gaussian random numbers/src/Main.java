import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        long k = scanner.nextLong();
        int n = scanner.nextInt();
        double m = scanner.nextDouble();
        boolean end = false;

        do {
            Random random = new Random(k);
            for (int i = 0; i < n; i++) {
                if (random.nextGaussian() > m) {
                    k++;
                    break;
                } else if (i == n - 1) {
                    System.out.println(k);
                    end = true;
                }
            }
        } while (!end);
    }
}