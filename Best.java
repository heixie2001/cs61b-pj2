/* Best.java */

package player;

/**
 The Best class stores a move and its utility score.
**/

public class Best {

    private Move move;
    private int score;
    
    /**
    Best creates a new object that stores no move an 0 utility value.
    @params: None.
    @return: None.
    **/
    Best() {
        this.move = null;
        this.score = 0;
    }

    /**
    setScore sets a given score to this Best.
    @params: double score -- The desired score.
    @return: None.
    **/
    void setScore(int score) {
        this.score = score;
    }
    
    /**
    setMove sets a given move to this Best.
    @params: Move move -- The desired move.
    @return: None.
    **/
    void setMove(Move move) {
        this.move = move;
    }
    
    /**
    getScore retrieves the score of this Best.
    @params: None.
    @return: double this.score -- The score of this Best.
    **/
    int getScore() {
        return this.score;
    }
    
    /**
    getMove retrieves the move of this Best.
    @params: None.
    @return: Move this.move -- The move of this Best.
    **/
    Move getMove() {
        return this.move;
    }
}


