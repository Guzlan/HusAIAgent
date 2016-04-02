package hus;

import hus.HusBoardState;
import hus.HusPlayer;
import hus.HusMove;

import java.util.ArrayList;
import java.util.Random;

/** A random Hus player. */
public class RandomHusPlayer extends HusPlayer {
    Random rand = new Random();

    public RandomHusPlayer() { super("RandomHusPlayer"); }

    /** Choose moves randomly. */


    public HusMove chooseMove(HusBoardState board_state)
    {
    	
    	if(board_state.firstPlayer()== player_id && board_state.getTurnNumber()==0) return new HusMove(8);

        // Get the legal moves for the current board state.
        ArrayList<HusMove> moves = board_state.getLegalMoves();
        HusMove move= null;
        int score = Integer.MIN_VALUE; // start with having score of minus infinity. 
        for (HusMove tempMove : moves ){
        	if(tempMove.getPlayerID()==player_id){
        		HusBoardState decisionBoard = (HusBoardState) board_state.clone();
        		decisionBoard.move(tempMove);
        		int temp = miniMax_AlphaBeta(5,player_id,opponent_id,false,Integer.MIN_VALUE, Integer.MAX_VALUE, decisionBoard);
        		if ( temp > score){
        			score = temp; 
        			move = tempMove;
        		}
        	}
        }
        return move;
    }
    
    public int miniMax_AlphaBeta(int level,int player_id, int opponent_id, boolean maximizingPlayer, int alpha, int beta, HusBoardState boardState){
    	if (level == 0 || boardState.gameOver()) { // in case we reach the depth of our search tree of if the game is over. 
    		return evaluationFunction(boardState);  
    	}
    	if (maximizingPlayer){ // my player / computer's turn to play now
    		int score = Integer.MIN_VALUE;
			ArrayList<HusMove> moves = boardState.getLegalMoves();
			for (HusMove move : moves){
				HusBoardState decisionBoard = (HusBoardState) boardState.clone(); // cloning board to see effect. 
				if (move.getPlayerID()==player_id && decisionBoard.move(move)){  
    				score =Math.max(score, miniMax_AlphaBeta(level-1,player_id,opponent_id, false, alpha,beta,decisionBoard));
    				alpha = Math.max(alpha, score); 
    				if(alpha>=beta) break ; // cut off for beta. pruning happens here. 
    			}
    		}
    		return score;
    	}
    	else { //mimic opponents play 
    		int score = Integer.MAX_VALUE;
			ArrayList<HusMove> moves = boardState.getLegalMoves();
			for (HusMove move : moves){
				HusBoardState decisionBoard = (HusBoardState) boardState.clone(); // cloning board to see effect. 
    			if (move.getPlayerID()==opponent_id && decisionBoard.move(move)){ 
    				score =Math.min(score, miniMax_AlphaBeta(level-1,player_id,opponent_id, true, alpha,beta,decisionBoard));
    				beta = Math.min(beta, score); 
    				if(alpha>=beta) break ; // cut off for alpha. pruning happens here. 
    			}
    		}
    		return score;
    	}
    }
   
    public int evaluationFunction(HusBoardState boardState){
    	// initial evaluation function is to add all the number of seeds in pits with larger than 1 seed of myAgent 
    	// subtract from it the sum of the number of seeds in pits that have larger than 1 of enemy. 
    	int[][] pits = boardState.getPits();
        int[] my_pits = pits[player_id];
        int[] op_pits = pits[opponent_id];
        int h1 = h1(my_pits,op_pits);
        int h2h3 = h2h3(my_pits,op_pits);
        return h2h3+h1; 
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
//*FIRST SMART AGENT    
//    
//    public HusMove chooseMove(HusBoardState board_state)
//    {
//    	
//    	if(board_state.getTurnNumber()==0) return new HusMove(8);
//
//        // Get the legal moves for the current board state.
//        ArrayList<HusMove> moves = board_state.getLegalMoves();
//        HusMove move= null;
//        int score = Integer.MIN_VALUE; // start with having score of minus infinity. 
//        for (HusMove tempMove : moves ){
//        	if(tempMove.getPlayerID()==player_id){
//        		HusBoardState decisionBoard = (HusBoardState) board_state.clone();
//        		decisionBoard.move(tempMove);
//        		int temp = miniMax_AlphaBeta(5,player_id,opponent_id,false,Integer.MIN_VALUE, Integer.MAX_VALUE, decisionBoard);
//        		if ( temp > score){
//        			score = temp; 
//        			move = tempMove;
//        		}
//        	}
//        }
//        return move;
//    }
//    
//    public int miniMax_AlphaBeta(int level,int player_id, int opponent_id, boolean maximizingPlayer, int alpha, int beta, HusBoardState boardState){
//    	if (level == 0 || boardState.gameOver()) { // in case we reach the depth of our search tree of if the game is over. 
//    		return evaluationFunction(boardState);  
//    	}
//    	if (maximizingPlayer){ // my player / computer's turn to play now
//    		int score = Integer.MIN_VALUE;
//			ArrayList<HusMove> moves = boardState.getLegalMoves();
//			for (HusMove move : moves){
//				HusBoardState decisionBoard = (HusBoardState) boardState.clone(); // cloning board to see effect. 
//				if (move.getPlayerID()==player_id && decisionBoard.move(move)){  
//    				score =Math.max(score, miniMax_AlphaBeta(level-1,player_id,opponent_id, false, alpha,beta,decisionBoard));
//    				alpha = Math.max(alpha, score); 
//    				if(alpha>=beta) break ; // cut off for beta. pruning happens here. 
//    			}
//    		}
//    		return score;
//    	}
//    	else { //mimic opponents play 
//    		int score = Integer.MAX_VALUE;
//			ArrayList<HusMove> moves = boardState.getLegalMoves();
//			for (HusMove move : moves){
//				HusBoardState decisionBoard = (HusBoardState) boardState.clone(); // cloning board to see effect. 
//    			if (move.getPlayerID()==opponent_id && decisionBoard.move(move)){ 
//    				score =Math.min(score, miniMax_AlphaBeta(level-1,player_id,opponent_id, true, alpha,beta,decisionBoard));
//    				beta = Math.min(beta, score); 
//    				if(alpha>=beta) break ; // cut off for alpha. pruning happens here. 
//    			}
//    		}
//    		return score;
//    	}
//    }
//   
//    public int evaluationFunction(HusBoardState boardState){
//    	// initial evaluation function is to add all the number of seeds in pits with larger than 1 seed of myAgent 
//    	// subtract from it the sum of the number of seeds in pits that have larger than 1 of enemy. 
//    	int[][] pits = boardState.getPits();
//        int[] my_pits = pits[player_id];
//        int[] op_pits = pits[opponent_id];
//        int myScore=0; 
//        int opScore=0;
//        
//        for (int i= 0 ; i <16; i++) {
//        	if(my_pits[i] >1 ) myScore+=my_pits[i];
//        	if(op_pits[i]>1) opScore += op_pits[i];
//        }
//        
//    	return myScore-opScore; 
//    }
//    public HusMove chooseMove(HusBoardState board_state)
//    {
//        // Pick a random move from the set of legal moves.
//        ArrayList<HusMove> moves = board_state.getLegalMoves();
//        HusMove move = moves.get(rand.nextInt(moves.size()));
//        return move;
//    }
}
