import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int sum = 0;

        while (true) {
            int number = sc.nextInt();
            sum += number;

            if (number == 0) {
                System.out.println(sum);
                break;
            } else if (sum >= 1000) {
                System.out.println(sum - 1000);
                break;
            }
        }
    }
}