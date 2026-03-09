package CompanyBasedQuestions;

public class SumOfTwoFractions {
    private static int gcd(int a , int b){
        if(b != 0)
         return gcd(b, a%b);
        return a;
    }

    private static String sumOfTwoFractions(String fractions){
        String[] f = fractions.split("\\+");
        String[] f1 = f[0].split("/");
        String[] f2 = f[1].split("/");
        int a = Integer.parseInt(f1[0]) *  Integer.parseInt(f2[1] )+ Integer.parseInt(f1[1]) * Integer.parseInt(f2[0]);
        int b = Integer.parseInt(f1[1]) * Integer.parseInt(f2[1]);
        int g = gcd(a, b);
        StringBuilder res = new StringBuilder();
        res.append(a/g);
        res.append("/");
        res.append(b/g);
        return res.toString();

    }

    public static void main(String[] args){
        System.out.println(sumOfTwoFractions("2/6+2/6"));

    }
}
