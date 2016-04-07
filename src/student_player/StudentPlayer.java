// Amro Gazlan 
// 260553379
package student_player;

import java.util.ArrayList;

import hus.HusBoardState;
import hus.HusPlayer;
import hus.HusMove;


/** A Hus player submitted by a student. */
public class StudentPlayer extends HusPlayer {
	private static final int firstToPlay = 23; // this is the best move to play if you're going first. 
	private static final int secondToPlay = 16; // this is the best move to play if you're going second. 
	private static final int moveCountDeep =60; // I'm willing to increase the depth of my search every 60 moves.
	private static final int timeOut = 1800; // I want to catch my best move before I hit 2 seconds. I play within 1.8 seconds. 
    /** You must modify this constructor to return your student number.
     * This is important, because this is what the code that runs the
     * competition uses to associate you with your agent.
     * The constructor should do nothing else. */
    public StudentPlayer() { super("260553379"); }

    /** This is the primary method that you need to implement.
     * The ``board_state`` object contains the current state of the game,
     * which your agent can use to make decisions. See the class hus.RandomHusPlayer
     * for another example agent. */
    public HusMove chooseMove(HusBoardState board_state)
    {
	    int turn =board_state.getTurnNumber();
	   	if (board_state.firstPlayer()== player_id && turn==0){ // This is actually heuristic 0 or h0. Which basically tells me what move to make if I was going first. 
	   		return new HusMove(firstToPlay);
	   	}
	   	if (board_state.firstPlayer()!= player_id && turn==0){ // This is actually heuristic 0 or h0. Which basically tells me what move to make if I was going first. 
	   		return new HusMove(secondToPlay);
	   	}
		
    
	   	
        // Get the legal moves for the current board state.
        ArrayList<HusMove> moves = board_state.getLegalMoves();
        HusMove move= null;
        double score = Integer.MIN_VALUE; // start with having score of minus infinity.
        long currentTime = System.currentTimeMillis();
        for (HusMove tempMove : moves ){ // getting all the valid moves of the current configuration and choosing the best one out of them based on mini max
        	if(tempMove.getPlayerID()==player_id){
        		HusBoardState decisionBoard = (HusBoardState) board_state.clone();
        		decisionBoard.move(tempMove);
        		int depth =((turn+moveCountDeep-1)/moveCountDeep)+4;
        		double temp = miniMax_AlphaBeta(depth<7? depth:5  ,player_id,opponent_id,false,Integer.MIN_VALUE, Integer.MAX_VALUE, decisionBoard,currentTime); // runing minimax. 
        		if ( temp > score){
        			score = temp; 
        			move = tempMove;
        		}
        	}
        	if(System.currentTimeMillis()-currentTime>timeOut) { // in case i almost time out I play whatever best move I have found so far. 
        		return move; 
        	}
        }

        return move;
    }
    
    
    
    public double miniMax_AlphaBeta(int level,int player_id, int opponent_id, boolean maximizingPlayer, double alpha, double beta, HusBoardState boardState,long currentTime){
    	if (level == 0 || boardState.gameOver()) { // in case we reach the depth of our search tree of if the game is over. 
    		return evaluationFunction(boardState);  
    	}
    	if(System.currentTimeMillis()-currentTime>timeOut) {// in case i almost time out I play whatever best move I have found so far. 
    		return evaluationFunction(boardState); 
    	}
    	if (maximizingPlayer){ // my player / computer's turn to play now
    		double score = Integer.MIN_VALUE;
			ArrayList<HusMove> moves = boardState.getLegalMoves();
			for (HusMove move : moves){
				HusBoardState decisionBoard = (HusBoardState) boardState.clone(); // cloning board to see effect. 
				if (move.getPlayerID()==player_id && decisionBoard.move(move)){  
    				score =Math.max(score, miniMax_AlphaBeta(level-1,player_id,opponent_id, false, alpha,beta,decisionBoard,currentTime));
    				alpha = Math.max(alpha, score); 
    				if(alpha>=beta) break ; // cut off for beta. pruning happens here. 
    			}
    		}
    		return score;
    	}
    	else { //mimic opponents play 
    		double score = Integer.MAX_VALUE;
			ArrayList<HusMove> moves = boardState.getLegalMoves();
			for (HusMove move : moves){
				HusBoardState decisionBoard = (HusBoardState) boardState.clone(); // cloning board to see effect.
    			if (move.getPlayerID()==opponent_id && decisionBoard.move(move)){ 
    				score =Math.min(score, miniMax_AlphaBeta(level-1,player_id,opponent_id, true, alpha,beta,decisionBoard,currentTime));
    				beta = Math.min(beta, score); 
    				if(alpha>=beta) break ; // cut off for alpha. pruning happens here. 
    			}
    		}
    		return score;
    	}
    }
   
    
    private double evaluationFunction(HusBoardState boardState){
    	// initial evaluation function is to add all the number of seeds in pits with larger than 1 seed of myAgent 
    	// subtract from it the sum of the number of seeds in pits that have larger than 1 of enemy. 
    	int[][] pits = boardState.getPits();
        int[] my_pits = pits[player_id];
        int[] op_pits = pits[opponent_id];
        // getting individual scores from my heuristics. 
        int h1 = h1(my_pits,op_pits); // counts the sum of seeds in pits that contain more than 1 seeds and takes the difference between my opponent and I.  
        int h2h3 = h2h3(my_pits,op_pits);// Measures how far I am from having more than half the number of seeds (48) and how far my opponent is and takes the difference. 
        int h4 = h4(my_pits);// Measures how many of my seeds are in defense mode(outer row) and how many are in attack mode (inner row)
        int h5 = h4(op_pits);// Measures how many of opponents seeds are in defense mode(outer row) and how many are in attack mode (inner row)
        return h2h3+h1+h4-1.5*h5; //return the evaluation function score of current stare of the board. 
    }
    private int h1 (int[] my_pits,int[] op_pits){ 
        int myScore=0; 
        int opScore=0;
        
        for (int i= 0 ; i <32; i++) {
        	if(my_pits[i] >1 ) myScore+=my_pits[i];
        	if(op_pits[i]>1) opScore += op_pits[i];
        }
        
    	return myScore-opScore;
    }
    private int h2h3 (int[] my_pits,int[] op_pits){
    	int half = 48;
        int myScore=0; 
        int opScore=0;
        
        for (int i= 0 ; i <32; i++) {
        	if(my_pits[i] >0 ) myScore+=my_pits[i];
        	if(op_pits[i]>0) opScore += op_pits[i];
        }
        myScore = myScore-half; 
        opScore = opScore-half;
    	return myScore-opScore;
    }
    private int h4(int[] my_pits){
    	int defendedStones=0; 
    	int exposedStones = 0; 
    	for (int i= 0 ; i <16; i++) {
        	if(my_pits[i] >0 ) defendedStones+=my_pits[i];
        }
    	for (int i= 17 ; i <32; i++) {
        	if(my_pits[i] >0 ) exposedStones+=my_pits[i];
        }
    	return defendedStones-exposedStones;
    }
   
}