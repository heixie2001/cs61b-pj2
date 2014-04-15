/* MachinePlayer.java */

package player;



/**
 *  An implementation of an automatic Network player. Keeps track of moves
 *  made by both players. Can select a move for itself.
 **/

public class MachinePlayer extends Player {

  /**
   *  DEPTH references the default searchDepth the first constructor chooses.
   *  board references the current Board the game is at.
   *  searchDepth references the number of moves the game tree search makes
   *  before it calls an evaluation function to evaluate the board.
   *  color references the color this MachinePlayer is assigned to.
   *  MACHINE_WIN references the maximum score which is assigned when the board
   *  has an immediate win for this MachinePlayer.
   *  OPPONENT_WIN references the minimum score which is assigned when the board
   *  has an immediate win for the opponent.
   **/

  final static private int DEPTH = 4;
  private Board board;
  private int searchDepth;
  private int color;
  final static private int MACHINE_WIN = 100;
  final static private int OPPONENT_WIN = -100;

  /**
   *  Creates a machine player with the given color. Color is either 0 (black)
   *  or 1 (white). (White has the first move.)
   *  @param color is the color this MachinePlayer is assigned to.
   **/ 
  public MachinePlayer(int color) {
    this.color = color;
    board = new Board(color);
    searchDepth = DEPTH; 
  }

  /**
   *  Creates a machine player with the given color and search depth. Color is
   *  either 0 (black) or 1 (white). (White has the first move.)
   *  @param color is the color this MachinePlayer is assigned to.
   *  @param searchDepth is the searchDepth to be stored.
   **/
  public MachinePlayer(int color, int searchDepth) {
    this.color = color;
    board = new Board(color);
    this.searchDepth = searchDepth;
  }

  /**
   *  Returns a new move by "this" player. Internally records the move (updates
   *  the internal game board) as a move by "this" player.
   *  @return a new best move chosen by this MachinePlayer.
   **/
  public Move chooseMove() {
    if (board.totalChips(color) == 0) {
      if (color == 0) {
        Move first = new Move(3,0);
        board.updateBoard(first,color);
        return first;
      } else {
        Move first = new Move(0,3);
        board.updateBoard(first,color);
        return first;
      }
    } else if(board.totalChips(color) == 1) {
      if (color == 0) {
        Move second = new Move(4,7);
        board.updateBoard(second,color);
        return second;
      } else {
        Move second = new Move(7,4);
        board.updateBoard(second,color);
        return second;
      }
    }
    int alpha = OPPONENT_WIN;
    int beta = MACHINE_WIN;
    boolean side = true;
    if (board.totalChips(color) == 10) {
      this.searchDepth = 1;
    }
    try {
      Best myBest = abTree(side, color, searchDepth, alpha, beta);	 
      board.updateBoard(myBest.getMove(), this.color);			     
      return myBest.getMove();
    } catch (InvalidNodeException e) {
      return new Move();
    }									     
  }

  /**
   *  abTree() uses minimax algorithm and alpha-beta pruning to search game tree
   *  to the given search depth and returns a Best object which contains the 
   *  best Move and the score of the board.
   *  @param side is true if it is this MachinePlayer's turn to make a move, 
   *  and false if it is the opponent's turn.
   *  @param color is the color to make the next move.
   *  @param depth is the search depth in game tree search.
   *  @param alpha is the initial value of alpha in alpha-beta pruning.
   *  @param beta is the initial value of beta in alpha-beta pruning.
   *  @return Best object which contains the best Move and the score of the board.
   **/
  private Best abTree(boolean side, int color, int depth, int alpha, int beta) throws InvalidNodeException {
    Best myBest = new Best();
    DList allValidMoves = board.allValidMoves(color);
    DListNode current = (DListNode) allValidMoves.front();
    myBest.setMove((Move)current.item());
    Best reply;
    if (depth == 0 || board.hasNetwork(color) || board.hasNetwork(1-color)) {
      myBest.setScore(board.score(searchDepth-depth, 1-color));
      return myBest;   
    } 
    if (side) {				 
      myBest.setScore(alpha);  	
    } else {
      myBest.setScore(beta);	
    }
    try {
      for (int i = 0; i < allValidMoves.length(); i ++) { 
        Move currentMove = (Move) current.item();
        board.updateBoard(currentMove, color);
        reply = abTree(!side, opponentColor(color), depth-1, alpha, beta );
        board.retrieveBoard(currentMove, color);
        if (side && reply.getScore() > myBest.getScore()) {
	  myBest.setMove(currentMove);
          myBest.setScore((int) reply.getScore());
          alpha = (int) reply.getScore();
        } else if(!side && reply.getScore() < myBest.getScore()) {
	  myBest.setMove(currentMove);
          myBest.setScore((int)reply.getScore());
          beta = (int) reply.getScore();
        }
        if (alpha >= beta) {
          return myBest;
        }
        current = (DListNode)current.next();
      }
    } catch (InvalidNodeException e) {
      System.err.println(e);
    }
    return myBest;
  }

  /**
   *  If the Move m is legal, records the move as a move by the opponent
   *  (updates the internal game board) and returns true. If the move is
   *  illegal, returns false without modifying the internal state of "this"
   *  player. This method allows your opponents to inform you of their moves.
   *  @param m is the Move by the opponent.
   **/
  public boolean opponentMove(Move m) {
    if (board.isValidMove(m, 1 - board.color)) {
      board.updateBoard(m, 1 - board.color);
      return true;
    } else {
      return false;
    }
  }

  /**
   *  If the Move m is legal, records the move as a move by "this" player
   *  (updates the internal game board) and returns true.  If the move is
   *  illegal, returns false without modifying the internal state of "this"
   *  player. This method is used to help set up "Network problems" for your
   *  player to solve.
   *  @param m is the desired Move to be made.
   **/
  public boolean forceMove(Move m) {
    if (board.isValidMove(m, board.color)) {
      board.updateBoard(m, board.color);
      return true;
    } else {
      return false;
    }
  }

  /**
   *  Returns the color of the other side.
   *  @param color is either 0 (black) or 1 (white).
   **/
  private int opponentColor(int color) {
    if (color == Board.BLACK) {
      return Board.WHITE;
    }
    return Board.BLACK;
  }
}











