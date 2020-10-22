/* Please, do not rename it */
class Problem {

    public static void main(String[] args) {
        String operator = args[0];
        // write your code here
        int res = Integer.parseInt(args[1]);
        switch (operator) {
            case "MAX":
                for (int i = 1; i < args.length; i++) {
                    if (Integer.parseInt(args[i]) > res) {
                        res = Integer.parseInt(args[i]);
                    }
                }
                break;
            case "MIN":
                for (int i = 1; i < args.length; i++) {
                    if (Integer.parseInt(args[i]) < res) {
                        res = Integer.parseInt(args[i]);
                    }
                }
                break;
            case "SUM":
                for (int i = 2; i < args.length; i++) {
                    res += Integer.parseInt(args[i]);
                }
                break;
            default:
                break;
        }
        System.out.println(res);
    }
}