package hus;

import hus.HusBoardState;
import hus.HusPlayer;
import hus.HusMove;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.Random;

/** A random Hus player. */
public class RandomHusPlayer extends HusPlayer {
    Random rand = new Random();

    public RandomHusPlayer() { super("RandomHusPlayer"); }
    /** You must modify this constructor to return your student number.
     * This is important, because this is what the code that runs the
     * competition uses to associate you with your agent.
     * The constructor should do nothing else. */
  
    /** This is the primary method that you need to implement.
     * The ``board_state`` object contains the current state of the game,
     * which your agent can use to make decisions. See the class hus.RandomHusPlayer
     * for another example agent. */
 
    /** Choose moves randomly. */
    public HusMove chooseMove(HusBoardState board_state)
    {
        // Pick a random move from the set of legal moves.
        ArrayList<HusMove> moves = board_state.getLegalMoves();
        HusMove move = moves.get(rand.nextInt(moves.size()));
        return move;
    }
}
