/* Board.java */
package player;


/**
 *  Implements the representation of a game-board.
 **/

public class Board {

	/**
	 *  BLACK references the integer representation of color black
	 *  WHITE references the integer representation of color white
	 *  EMPTY references the integer representation of an empty cell
	 *  DIMENSION references the dimension of the game-board
	 *  board references a game-board, which is essentially a 2-D array
	 *  color references the color of the chip which can be black or white
	 *  numofBlacks references the number of black chips currently on board
	 *  numofWhites references the number of white chips currently on board
	 **/
	 
	final static int BLACK = 0;
	final static int WHITE = 1;
	final static int EMPTY = 2;
	final static int DIMENSION = 8;
	int[][] board;
    int color;
    int numofBlacks;
    int numofWhites;
	
    /**
     *  Creates an empty board with a certain dimension (8 x 8)
     **/ 
	Board () {
        board = new int[DIMENSION][DIMENSION];
        for (int i=0; i < DIMENSION; i++) {
            for (int j=0; j <DIMENSION; j++) {
            	board[i][j] = EMPTY;
            }
        }
	}

	/**
     *  Creates an empty board with the given color
     *  @param color is the color the board is assigned to (black or white)
     **/
    Board (int color) {
       this.color = color;
        board = new int[DIMENSION][DIMENSION];
        for(int i=0; i < DIMENSION; i++){
            for(int j=0; j <DIMENSION; j++){
            	board[i][j] = EMPTY;
            }
        }
    }
    
    /**
     *  Assigns a status to a certain cell and the status can be white, black or empty
     *  @param x is the x-coordinate on the game-board
     *  @param y is the y-coordinate on the game-board
     *  @param value is the status assigned to a certain cell
     **/
    void setColor (int x, int y, int value) {
         board[x][y] = value;
    }
	
    /**
     *  Gets the status of a certain cell and the status can be white, black or empty
     *  @param x is the x-coordinate on the game-board
     *  @param y is the y-coordinate on the game-board
     *  @return the status of a certain which can be black, white or empty
     **/
    int getColor (int x, int y ) {
       return board[x][y];
    }
    
    /**
     *  Updates the board status after a certain move made by the assigned color
     *  @param m is an assigned move
     *  @param color is the color status (black or white) of the chip being moved
     **/
    void updateBoard(Move m, int color) {
    	if(m.moveKind == Move.ADD) {
    		setColor(m.x1, m.y1, color);
			addNumofColor(color);
    	} else if (m.moveKind == Move.STEP) {
    		setColor(m.x2, m.y2, Board.EMPTY);
    		setColor(m.x1, m.y1, color);
		}	  
	}
    
    /**
     *  Updates the board status after canceling a certain move made by the color
     *  @param m is an assigned move
     *  @param color is the color status (black or white) of the chip being moved
     **/
    void retrieveBoard(Move m, int color) {
    	if (m.moveKind == Move.ADD) {
    		setColor(m.x1, m.y1, EMPTY);
			subtractNumofColor(color);
		} else if (m.moveKind == Move.STEP) {
			setColor(m.x2, m.y2, color);
			setColor(m.x1, m.y1, EMPTY);
		}   	  
	}

    /**
     *  Adds the number of chips of a certain color currently on board after a move
     *  @param color is the color status of the chip being added
     **/
    void addNumofColor (int color) {
	  	if(color == WHITE) {
	  		numofWhites++;
	  	} else {
	  		numofBlacks++;
	  	}	   
    }
    
    /**
     *  Subtracts the number of chips of a certain color currently on board after a move
     *  @param color is the color status of the chip being removed
     **/
    void subtractNumofColor (int color) {
	   if (color == WHITE) {
	  		numofWhites--;
	  	} else {
	  		numofBlacks--;
	  	}
    }
    
    /**
     *  Total number of chips currently on board
     *  @return total number of chips currently on board
     **/
	int totalChips () {
		return numofBlacks + numofWhites;	
	}
	
	/**
     *  Total number of chips of a given color currently on board
     *  @param color is the color status of the chip
     *  @return total number of chips of the color currently on board
     **/
	int totalChips (int color) {
		if (color == WHITE) {
			return numofWhites;
		} else if (color == BLACK) {
		    return numofBlacks;
		} else {
		    return -2;
		}
	}
	
	/**
	 * Determines whether the move by the chip of a certain color is valid
	 * If m is a legal move for the given color, return true
	 * If m is not a legal move for the given color, return false
	 * @param m is an assigned move
	 * @param color is the color of the chip being moved
	 * @return whether the move is valid
	 **/


	boolean isValidMove(Move m, int color) {
		if (m.moveKind == Move.STEP) {
            if (getColor (m.x1, m.y1)!= EMPTY) {
            	return false;
            }
            if(!isNotGoal(m,1-color)) {
                return false;
            }
            if(!isValidBound(m)) {        
                return false;
            }
            DList list = new DList();
            Coordinate coord = new Coordinate(m.x1, m.y1);
            setColor(m.x2,m.y2,EMPTY);
            DList firstList = neighborList(coord,color,list);
            if(firstList.length()>=2) {
                setColor(m.x2,m.y2,color);
                return false;
            } else if(firstList.length() == 1 ) {
            	try {
            		Coordinate item = (Coordinate) firstList.front().item();
                    DList secondList = neighborList(item,color,list);
                    setColor(m.x2,m.y2,color);
                    if(secondList.length()>=2) {
                    	setColor(m.x2,m.y2,color);
                    	return false;
                    }
                } 
            	catch (InvalidNodeException e) {
            	}   
            }
            setColor(m.x2,m.y2,color);
            return true;
        } else if(m.moveKind == Move.ADD) { 
            if (totalChips(color)>=10) {   
                return false;
            }
            if(!isValidBound(m)) {        
                return false;
            }
            if (getColor(m.x1, m.y1)!= EMPTY) { 
            	return false;
            }
            if (!isNotGoal(m,1-color)) {
                return false;
            }
            DList list = new DList();
            Coordinate coord = new Coordinate(m.x1, m.y1);
            DList firstList = neighborList(coord,color,list);
            if(firstList.length()>=2) {
                return false;
            } else if (firstList.length() == 1){
                try {
                	Coordinate item = (Coordinate) firstList.front().item();
                    DList secondList = neighborList(item,color,list);
                    if(secondList.length()>=2) {
                    	return false;
                    }        
                } 
                catch (InvalidNodeException e) {
                    e.printStackTrace();
                }
            }
       }
       return true;
    }	
	
	/**
	 * Determines whether the move is within the boundary of the game-board
	 * @param m is an assigned move
	 * @return whether the move is within the boundary of the game-board
	 **/
	private boolean isValidBound(Move m) {
		if(m.x1<0 || m.x1>DIMENSION-1 || m.y1<0 || m.y1>DIMENSION-1 ) {
			return false;
		}
		if(m.x1==0 || m.x1 == DIMENSION-1) {
			if(m.y1==0 || m.y1==DIMENSION-1) {
				return false;
			}
		} 
		return true;
	}
	
	/**
	 * Determines whether the move is within the goal of area of a given color
	 * @param m is an assigned move
	 * @param color is the color of the goal area
	 * @return whether the move is valid
	 **/
	private boolean isNotGoal(Move m, int color) {
		if(color == BLACK) {		
			if(m.y1 == 0 || m.y1 == DIMENSION-1) 	
				return false;
			}
		if(color == WHITE) {			
			 if(m.x1 == 0 || m.x1 == DIMENSION-1) {
				 return false;
			 }
		}
		return true;
	}
	
	/**
	 * Searches around the 8 cells around the given cell/chip to see if there is any chip of the same color
	 * @param coord is the coordinate of the give chip
	 * @param color is the color of the chip being searched
	 * @return a DList containing all the chips of the same color around a given cell
	 **/
	private DList neighborList(Coordinate coord, int color, DList list){
		int x = coord.getX();
		int y = coord.getY();
		for(int i=x-1; i<x+2; i++) {
			for(int j=y-1; j<y+2; j++) {
				if(!(i==x && j==y) && i>=0 && i<=7 && j>=0 && j<=7) {	
					if(getColor(i, j) == color) {
						Coordinate coor = new Coordinate(i,j);
						list.insertBack(coor);
					}
				}
			}
        }
		return list;
	}
        
	/**
	 * Checks if this board and the given board are equal
	 * @param Board b as a comparison
	 * @return whether the current board is the same given board; true if yes, false if no
	 **/
        boolean isEqual(Board b) {
		for(int i=0;i<DIMENSION; i++) {
			for(int j =0; j<DIMENSION; j++) {
				if(this.board[i][j] != b.board[i][j]) {
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * Makes a new copy of the current board
	 * @return a board which is a new copy of the current board
	 **/
	Board copyBoard() {
		Board newBoard = new Board();
		for (int i=0;i<DIMENSION; i++) {
			for (int j =0; j<DIMENSION; j++) {
				this.board[i][j] = newBoard.board[i][j];
			}
		}
		return newBoard;
	}
	 
	
	  /**
	   *  Returns a DList that stores Chip objects that represent all the chips 
	   *  connected to Chip c on this Board. If c has no connection, return an
	   *  empty DList.
	   *  @param c is the Chip to which connections are searched.
	   **/
	   DList connections(Chip c) {
	     int color = c.getColor();
	     int x = c.getX();
	     int y = c.getY();
	     DList connected = new DList();
	     for (int i = x - 1; i >= 0; i--) {
	       if (board[i][y] == color) {
		 Chip left = new Chip(color, i, y);
		 connected.insertBack(left);
		 break;
	       } else if (board[i][y] == 1 - color) {
		 break;
	       }
	     }
	     for (int i = x + 1; i < DIMENSION; i++) {
	       if (board[i][y] == color) {
		 Chip right = new Chip(color, i, y);
		 connected.insertBack(right);
		 break;
	       } else if (board[i][y] == 1 - color) {
		 break;
	       }
	     }
	     for (int i = y - 1; i >= 0; i--) {
	       if (board[x][i] == color) {
	         Chip up = new Chip(color, x, i);
		 connected.insertBack(up);
		 break;
	       } else if (board[x][i] == 1 - color) {
		 break;
	       }
	     }
	     for (int i = y + 1; i < DIMENSION; i++) {
	       if (board[x][i] == color) {
	         Chip down = new Chip(color, x, i);
		 connected.insertBack(down);
		 break;
	       } else if (board[x][i] == 1 - color) {
		 break;
	       }
	     }
	     for (int i = 1; x - i >= 0 && y - i >= 0; i++) {
	       if (board[x-i][y-i] == color) {
		 Chip upperleft = new Chip(color, x-i, y-i);
		 connected.insertBack(upperleft);
		 break;
	       } else if (board[x-i][y-i] == 1 - color) {
		 break;
	       }
	     }
	     for (int i = 1; x + i < DIMENSION && y - i >= 0; i++) {
	       if (board[x+i][y-i] == color) {
		 Chip upperright = new Chip(color, x+i, y-i);
		 connected.insertBack(upperright);
		 break;
	       } else if (board[x+i][y-i] == 1 - color) {
		 break;
	       }
	     }
	     for (int i = 1; x - i >= 0 && y + i < DIMENSION; i++) {
	       if (board[x-i][y+i] == color) {
		 Chip lowerleft = new Chip(color, x-i, y+i);
		 connected.insertBack(lowerleft);
		 break;
	       } else if (board[x-i][y+i] == 1 - color) {
		 break;
	       }
	     }
	     for (int i = 1; x + i < DIMENSION && y + i < DIMENSION; i++) {
	       if (board[x+i][y+i] == color) {
		 Chip lowerright = new Chip(color, x+i, y+i);
		 connected.insertBack(lowerright);
		 break;
	       } else if (board[x+i][y+i] == 1 - color) {
		 break;
	       }
	     }
	     return connected;
	   }

	  /**
	   *  Return a decimal between -1 and 1 that indicates the likelihood of winning by 
	   *  the player on this Board. If this Board yields an immediate win for the 
	   *  player, return 100. If this Board yields an immediate win for the opponent, 
	   *  return -100. Otherwise, return a decimal in between as the score. The higher 
	   *  the score is, the more likely and faster the player will win.
	   *  @param numOfMoves is the number of moves made in game tree search before 
	   *  arriving at the current board.
	   *  @param previousPlayer is either 0(black) or 1(white), which denotes the 
	   *  color that made the last move.
	   **/
	  int score(int numOfMoves, int previousPlayer) throws InvalidNodeException {
	    int score = 0;
	    boolean ourNetwork = hasNetwork(color);
	    boolean oppoNetwork = hasNetwork(1-color);
	    if (ourNetwork && oppoNetwork) {
	      if (previousPlayer == color) {
	        return -100 + (numOfMoves - 1) / 2;
	      } else {
	        return 100 - (numOfMoves - 2) / 2;
	      }
	    } else if (ourNetwork) {
	      return 100 - (numOfMoves - 1) / 2;
	    } else if (oppoNetwork) {
	      return -100 + (numOfMoves - 2) / 2;
	    }
	    if (color == BLACK) {
	      if (board[3][7] == BLACK || board[4][7] == BLACK) {
		score = score + 4;
	      } 
	      if (search("y", 7, BLACK) == 1) {
		score = score + 4;
	      } else if (search("y", 7, BLACK) > 2) {
		score = score - 20;
	      }
	      if (board[3][0] == BLACK || board[4][0] == BLACK) {
		score = score + 4;
	      }
	      if (search("y", 0, BLACK) == 1) {
		score = score + 4;
	      } else if (search("y", 0, BLACK) > 2) {
		score = score - 20;
	      }
	    } else {
	      if (board[7][3] == WHITE || board[7][4] == WHITE) {
		score = score + 4;
	      }
	      if (search("x", 7, WHITE) == 1) {
		score = score + 4;
	      } else if (search("x", 7, WHITE) > 2) {
		score = score - 20;
	      }
	      if (board[0][3] == WHITE || board[0][4] == WHITE) {
		score = score + 4;
	      }
	      if (search("x", 0, WHITE) == 1) {
		score = score + 4;
	      } else if (search("x", 0, WHITE) > 2) {
		score = score - 20;
	      }
	    }
	    int connection = 0;
	    int oppositeconnection = 0;
	    for (int i = 0; i < DIMENSION; i++) {
	      for (int j = 0; j< DIMENSION; j++) {
		if (board[i][j] == color) {
		  connection+=connections(new Chip(color, i, j)).length();
		} else if (board[i][j] == 1 - color) {
		  oppositeconnection+=connections(new Chip(1-color, i, j)).length();
		}
	      }
	    }
	    score+= connection - oppositeconnection; 
	    if (score > 100) {
	      return 100;
	    } else if (score < -100) {
	      return -100;
	    } else {
	      return score;
	    }
	  }
		  
	  /**
	   *  Search through the specified column or row and return the number
	   *  of chips in that column or row with the given color.
	   *  @param direction is either "x" or "y". If it's "x", look at the 
	   *  column direction; otherwise, look at the row direction.
	   *  @param coord is the number specifying the coordinate in the given
	   *  direction.
	   *  @param side is either 0(black) or 1(white). It's the color we 
	   *  search for.
	   **/
	  private int search(String direction, int coord, int side) {
	    int counter = 0;
	    if (direction == "x") {
	      for (int i = 0; i < DIMENSION; i++) {
		if (board[coord][i] == side) {
		  counter++;
		}
	      }
	    } else {
	      for (int i = 0; i < DIMENSION; i++) {
		if (board[i][coord] == side) {
		  counter++;
		}
	      }
	    }
	    return counter;
	  }

	  /**  
	   *  Returns a DList that stores Move objects that represent all the
	   *  valid next moves this Board can make for the given color. If 
	   *  there is no valid move this Board can make, return an empty DList.
	   *  @param color is the color that is to make the next move.
	   **/
	  DList allValidMoves(int color) {
	    DList allMoves = new DList();
	    try {
	      if (totalChips(color) >= 10) {
		DList emptyList = new DList();
		DList chipList = new DList();
		for(int i=0; i<Board.DIMENSION; i++) {
		  for(int j=0; j<Board.DIMENSION; j++) {
		    Coordinate currCoord = new Coordinate(i,j);
		    if(getColor(i,j) == EMPTY) {
		      emptyList.insertBack(currCoord);
		    } else if(getColor(i,j) == color){
	              chipList.insertBack(currCoord);
		    }
		  }
		}
	        DListNode chipNode = (DListNode) chipList.front();
		for(int i=0; i<chipList.length(); i++) {
		  DListNode emptyNode = (DListNode) emptyList.front();
		  for(int j=0; j<emptyList.length(); j++) {
		    Coordinate emptyCoord = (Coordinate) emptyNode.item();
		    Coordinate chipCoord = (Coordinate) chipNode.item();
		    Move stepMove = new Move(emptyCoord.getX(), emptyCoord.getY(), chipCoord.getX(), chipCoord.getY());
	            setColor(stepMove.x2, stepMove.y2, EMPTY);
		    if(isValidMove(stepMove, color)) {
	              allMoves.insertBack(stepMove);
		    }
		    setColor(stepMove.x2, stepMove.y2, color);
		    emptyNode = (DListNode) emptyNode.next();
		  }
		  chipNode = (DListNode) chipNode.next();
		}
	      } else  {
		for(int i=0; i<DIMENSION; i++) {
		  for(int j=0; j<DIMENSION; j++) {
		    if(getColor(i,j) == EMPTY) {
	              Move addMove = new Move(i,j);
		      if(isValidMove(addMove, color)) {
		        allMoves.insertBack(addMove);
		      }
		    }
		  }
		}
	      }
	    } catch (InvalidNodeException e) {
	      System.out.println(e + "in move generations");
	    }
	    return allMoves;
	  }

	  /**
	   *  Returns true if c is in the end goal area. Otherwise, returns false.
	   *  @param c is the Chip whose position is to be determined.
	   **/
	  boolean inGoal(Chip c) {
	    if (c.getColor() == WHITE) {
	      if (c.getX() == DIMENSION -1) {
		return true;
	      } 
	    } 
	    if (c.getColor() == BLACK) {
	      if (c.getY() == DIMENSION -1) {
		return true;
	      }
	    }
	    return false;
	  }

	  /**
	   *  Returns true if c is in the start goal area. Otherwise, returns false.
	   *  @param c is the Chip whose position is to be determined.
	   **/
	  boolean inStartGoal(Chip c) {
	    if (c.getColor() == WHITE) {
	      if (c.getX() == 0) {
		return true;
	      } 
	    } 
	    if (c.getColor() == BLACK) {
	      if (c.getY() == 0) {
		return true;
	      }
	    }
	    return false;
	  }
		  
	  /** 
	   *  Returns the slope of the segment formed by Chip a and Chip b.
	   *  If the slope is infinite, return 10.
	   *  @param a is the Chip that forms one end of the segment.
	   *  @param b is the Chip that forms the other end of the segment.
	   **/
	  double slope(Chip a, Chip b) {
	    if (a.getX() == b.getX()) {
	      return 10;
	    } else {
	      double up = b.getY() - a.getY();
	      double down = b.getX() - a.getX();
	      return up/down;
	    }
	  }
	      
	  /**
	   *  Returns all networks in a DList regardless of length starting from Chip start.
	   *  @param start is the starting Chip of the network.
	   *  @param depth records depth of the method in a recursive call.
	   **/
	  DList allNetwork(Chip start, int depth) throws InvalidNodeException {
	    DList set = new DList();
	    if (inGoal(start)) {
	      DList network = new DList();
	      network.insertFront(start);
	      set.insertFront(network);
	      return set;
	    } else if (depth > 10) {
	      return set;
	    } else if (connections(start).length() == 0) {
	      return set;
	    } else {
	      DListNode current = (DListNode) connections(start).front();
	      int length =  connections(start).length();    
	      for (int i = 0; i < length; i++) {
		if (!inStartGoal((Chip)current.item())) {
		  DList rest = allNetwork((Chip) current.item(), depth+1);
		  if (rest.length() > 0) {
		    DListNode currentNetworkNode = (DListNode) rest.front();
		    for (int j = 0; j < rest.length(); j++) {
		      DList currentNetwork = (DList) currentNetworkNode.item();
		      currentNetwork.insertFront(start);
		      if (isTurnCorner(currentNetwork) && noSameChip(currentNetwork)) {
		        set.insertFront(currentNetwork);
		      }
		      currentNetworkNode = (DListNode) currentNetworkNode.next();
		    }
		  }
                }
		current = (DListNode) current.next();
	      }
	      return set;
	    }
	  }  
	  
	    /**
	     *  Searches if the length of the winning network is larger or equal to 6
	     *  @param dlist is the possible winning network
	     *  @return true if the network has enough length and false if not
	     **/  
	  boolean lengthMatch(DList dlist) throws InvalidNodeException {
		  if (dlist.length() > 0) {
			  DListNode currentNode = (DListNode) dlist.front();
			  for (int i = 0; i < dlist.length(); i++) {
				  DList currentList = (DList) currentNode.item();
				  if (currentList.length() >= 6) {
					  return true;
				  } 
			  currentNode = (DListNode) currentNode.next();
			  } 
		  } 	
		 return false;
	  }
	  
	    /**
	     *  Searches if a give color has a winning network
	     *  @param color is the color being checked
	     *  @return true if the color has a network and false if it doesn't
	     **/
	  boolean hasNetwork(int color) throws InvalidNodeException {
		  if (color == WHITE) {
			  for (int i = 0; i < DIMENSION; i++) {
				  if (board [0][i] == color) {
					  Chip start = new Chip (color,0,i);
					  return lengthMatch(allNetwork(start, 1));
				  }
			  }
		  }
		  if (color == BLACK) {
			  for (int i = 0; i < DIMENSION; i++) {
				  if (board [i][0] == color) {
					  Chip start = new Chip (color,i,0);
					  return lengthMatch(allNetwork(start,1));
				  }
			  }
		  }
		  return false;	   
	  }
		
	    /**
	     *  Searches if a network has passed through a chip without turning a corner
	     *  @param list is a possible network
	     *  @return true if the network has turned corner when passing through a chip
	     *  false if it hasn't
	     **/	  
	  private  boolean isTurnCorner(DList list) throws InvalidNodeException {
		  if (list.length() < 3) {
			  return true;
		  } else {
			  Chip currChip = (Chip) list.front().item();
			  Chip fatherChip = (Chip) list.front().next().item();
			  Chip grandFatherChip = (Chip) list.front().next().next().item();
			  if (slope(currChip, fatherChip) == slope(fatherChip, grandFatherChip)) {	
				  return false;
			  }
			  else { 
				  return true;
			  }
		 }
	  }
		  
	    /**
	     *  Searches if the newly added chip already exists in the input list
	     *  @param list is a DList being checked
	     *  @return whether the newly added chip already exists in the input list; return false if there it already exists
	     *  and true if it doesn't exist
	     **/
	private boolean noSameChip(DList list) {
		if (list.length()-1==0) {
			return true;
		}
		DListNode currNode;
		try {
			currNode = (DListNode) list.front().next();
			for(int i =0; i< list.length()-1; i++) {
				if(((Chip)currNode.item()).equal((Chip)list.front().item())) { 
					return false;
				}
				currNode = (DListNode) currNode.next();
			}
            return true;
		} 
		catch (InvalidNodeException e) {
			e.printStackTrace();
		}
		return false;
	}

  /**
   *  toString() returns a String representation of this Board.
   *  @return a String representation of this Board.
   **/	  
    public String toString() {
    	String print = new String();
    	String margin = "   0    1   2   3   4   5   6   7\n";
    	String line = "  --------------------------------\n";
    	for(int i=0; i<DIMENSION; i++) {
    		String str = i+" |";
    		for(int j=0; j<DIMENSION; j++) {
    			if(board[j][i] == BLACK) {
    				str = str + " B |";
    			} else if(board[j][i] == WHITE) {
    				str = str + " W |";
    			} else if(board[j][i] == EMPTY) {
    				str = str + "   |";
    			}
    		}
    		str = str + "\n";
    		print = print + str + line;
    	}
    	print =margin+ line + print;
    	return print;
    }
}
    







    







    







    






