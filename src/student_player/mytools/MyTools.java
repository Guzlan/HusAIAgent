package student_player.mytools;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ArrayDeque;


public class MyTools {
    public static double getSomething(){
        return Math.random();
    }
    public static int[][] getPitScores(int [] enemyPits){  
    	int [][] scores = new int[16][1];
    	int index = 0; 
    	for (int i = 16; i < 32; i++){
    		scores[index][0] = enemyPits[i] >0 ? enemyPits[i]+enemyPits[31-i] : -1;
    	}
    	Arrays.sort(scores, new java.util.Comparator<int[]>() {
            public int compare(int[] a, int[] b) {
                return Double.compare(a[0], b[0]);
            }
        });
    	return scores; 
    }
    
}
