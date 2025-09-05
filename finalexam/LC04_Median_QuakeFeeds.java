package finalexam;
import java.util.*;
public class LC04_Median_QuakeFeeds {
    public static double findMedianSortedArrays(double[] A,double[] B){
        //確保A是短的
        if(A.length > B.length){
            double[] temp = A;
            A = B;
            B = temp;
        }
        int m = A.length, n = B.length;
        int totalLeft = (m+n+1)/2;

        int left =0,right =m;
        while(left <= right){
            int i=(left+right)/2; //A取i個
            int j=totalLeft-i; //B取j個

            double Aleft = (i==0)?Double.NEGATIVE_INFINITY:A[i-1];
            double Aright = (i==m)?Double.POSITIVE_INFINITY:A[i];
            double Bleft = (j==0)?Double.NEGATIVE_INFINITY:B[j-1];
            double Bright = (j==n)?Double.POSITIVE_INFINITY:B[j];

            if(Aleft <= Bright && Bleft <= Aright){
                //切割
                if((m+n)%2==1){
                    return Math.max(Aleft, Bleft);
                }else{
                    return (Math.max(Aleft, Bleft) + Math.min(Aright, Bright)) / 2.0;
                }
            }else if(Aleft>Bright){
                right = i - 1; //A左邊太大，往左縮
            }else{
                left = i + 1;  //A左邊太小，往右擴
            }
        }
        throw new IllegalArgumentException("");
    }
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt(), m = sc.nextInt();
        double[] arr1 = new double[n];
        double[] arr2 = new double[m];
        for (int i = 0; i < n; i++) arr1[i] = sc.nextDouble();
        for (int i = 0; i < m; i++) arr2[i] = sc.nextDouble();
        double median = findMedianSortedArrays(arr1, arr2);
        System.out.printf("%.1f\n", median);
        sc.close();
    }
}
