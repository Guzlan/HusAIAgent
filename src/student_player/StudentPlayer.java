// Amro Gazlan 
// 260553379
package student_player;

import hus.HusBoardState;
import hus.HusPlayer;
import hus.HusMove;
import hus.HusMove.MoveType;

import java.util.ArrayList;

import student_player.mytools.MyTools;

/** A Hus player submitted by a student. */
public class StudentPlayer extends HusPlayer {

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
    	
    	if(board_state.getTurnNumber()==0) return new HusMove(8);

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
        int myScore=0; 
        int opScore=0;
        
        for (int i= 0 ; i <16; i++) {
        	if(my_pits[i] >1 ) myScore+=my_pits[i];
        	if(op_pits[i]>1) opScore += op_pits[i];
        }
        
    	return myScore-opScore; 
    }
}