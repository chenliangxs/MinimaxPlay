import java.util.ArrayList;

public class ChessBoard{
	public ChessNode[][] chessBoard;
	private int boardSize;
	
	public ChessBoard(int[][] data, int[][] occupency){
		boardSize = data.length;
		chessBoard = new ChessNode[boardSize][boardSize];
		for(int i=0;i<boardSize;i++){
			for(int j=0;j<boardSize;j++){
				chessBoard[i][j] = new ChessNode(data[i][j]);
				chessBoard[i][j].owner = occupency[i][j];
			}
		}
	}
	public void occupy(int n, int m, int gang){
		chessBoard[n][m].owner = gang;
	}
	/**
	 * take one action
	 * @param n
	 * @param m
	 * @param gang
	 * @return
	 */
	public boolean[] action(int n, int m, int gang){
		occupy(n,m,gang);
		boolean[] changed = new boolean[4];
		int[] neighbor = new int[4];
		if(n>0){
			neighbor[0] = chessBoard[n-1][m].owner;
		}
		if(n<boardSize-1){
			neighbor[1] = chessBoard[n+1][m].owner;
		}
		if(m>0){
			neighbor[2] = chessBoard[n][m-1].owner;
		}
		if(m<boardSize-1){
			neighbor[3] = chessBoard[n][m+1].owner;
		}
		for(int a:neighbor){
			if(a == gang){
				if(neighbor[0]>0 && neighbor[0]!=gang){
					chessBoard[n-1][m].owner = gang;
					changed[0]=true;
				}
				if(neighbor[1]>0 && neighbor[1]!=gang){
					chessBoard[n+1][m].owner = gang;
					changed[1]=true;
				}
				if(neighbor[2]>0 && neighbor[2]!=gang){
					chessBoard[n][m-1].owner = gang;
					changed[2]=true;
				}
				if(neighbor[3]>0 && neighbor[3]!=gang){
					chessBoard[n][m+1].owner = gang;
					changed[3]=true;
				}
			}
		}
		return changed;
	}
	/**
	 * undo action
	 * @return
	 */
	public void undoMove(int x, int y, boolean[] moved, int player){
		chessBoard[x][y].owner = 0;
		if(player==1){
			if(moved[0]==true){
				chessBoard[x-1][y].owner = 2;
			}
			if(moved[1]==true){
				chessBoard[x+1][y].owner = 2;
			}
			if(moved[2]==true){
				chessBoard[x][y-1].owner = 2;
			}
			if(moved[3]==true){
				chessBoard[x][y+1].owner = 2;
			}
		}
		else{
			if(moved[0]==true){
				chessBoard[x-1][y].owner = 1;
			}
			if(moved[1]==true){
				chessBoard[x+1][y].owner = 1;
			}
			if(moved[2]==true){
				chessBoard[x][y-1].owner = 1;
			}
			if(moved[3]==true){
				chessBoard[x][y+1].owner = 1;
			}
		}
	}
	
	/**
	 * evaluate function
	 * @return
	 */
	public int getEval(){
		int gang1 = 0;
		int gang2 = 0;
		for(int i=0;i<chessBoard.length;i++){
			for(int j=0;j<chessBoard.length;j++){
				if(chessBoard[i][j].owner==1){
					gang1+=chessBoard[i][j].value;
				}
				else if(chessBoard[i][j].owner==2){
					gang2+=chessBoard[i][j].value;
				}
			}
		}
		return gang1-gang2;
	}
	/**
	 * possible movements
	 * @return
	 */
	public ArrayList<Position> getMoves(){
		ArrayList<Position> moves = new ArrayList<Position>();
		for(int i=0;i<boardSize;i++){
			for(int j=0;j<boardSize;j++){
				if(chessBoard[i][j].owner==0){
					Position p = new Position(i,j);
					moves.add(p);
				}
			}
		}
		return moves;
	}
	/**minimax
	 * @param player
	 * @param depth
	 * @return
	 */
	public int[] miniMax(int player, int depth){
		ArrayList<Position> nextMove = getMoves();
		int[] bestRes = new int[3];
		bestRes[1] = -1;
		bestRes[2] = -1;
		if(player == 1){
			bestRes[0] = Integer.MIN_VALUE;
		}
		if(player == 2){
			bestRes[0] = Integer.MAX_VALUE;
		}
		if(depth==0 || nextMove.size()==0){
			bestRes[0] = getEval();
			return bestRes;
		}
		else{
			for(Position p:nextMove){
				int x = p.getX();
				int y = p.getY();
				boolean[] undo = action(x,y,player);
				if(player==1){
					int currentValue = miniMax(2,depth-1)[0];
					if(currentValue>bestRes[0]){
						bestRes[0] = currentValue;
						bestRes[1] = x;
						bestRes[2] = y;
					}
					//undo movement
					undoMove(x,y,undo,player);
				}
				if(player==2){
					int currentValue = miniMax(1,depth-1)[0];
					if(currentValue<bestRes[0]){
						bestRes[0] = currentValue;
						bestRes[1] = x;
						bestRes[2] = y;
					}
					//undo movement
					undoMove(x,y,undo,player);
				}
			}	
		}
		//System.out.println("Bestmove"+bestRes[1]+" "+bestRes[2]+" "+bestRes[0]+" "+"depth"+depth);
		return bestRes;
	}
	/**
	 * Alfa-beta pruning
	 */
	public int[] alphaBetaPrune(int player, int depth, int alpha, int beta){
		ArrayList<Position> nextMove = getMoves();
		int[] result = new int[3];
		result[1] = -1;
		result[2] = -1;
		if(player==1){
			result[0] = Integer.MIN_VALUE;
		}
		if(player==2){
			result[0] = Integer.MAX_VALUE;
		}
		if(depth==0 || nextMove.size()==0){
			result[0] = getEval();
			return result;
		}
		else{
			for(Position p:nextMove){
				int x = p.getX();
				int y = p.getY();
				boolean[] action = action(x, y, player);
				if(player==1){
					//System.out.println(x+" "+y + " "+alpha+" "+beta);
					int currentValue = alphaBetaPrune(2, depth-1, alpha, beta)[0];
					if(currentValue>result[0]){
						result[0] = currentValue;
						result[1] = x;
						result[2] = y;
					}
					if(result[0]>=beta){
						undoMove(x,y,action,player);
						//System.out.println(x+" "+y + " "+alpha+" "+beta);
						return result;
					}
					alpha = Math.max(alpha, result[0]);
					undoMove(x,y,action,player);
				}
				if(player==2){
					//System.out.println(x+" "+y + " "+alpha+" "+beta);
					int currentValue = alphaBetaPrune(1, depth-1, alpha, beta)[0];
					if(currentValue<result[0]){
						result[0] = currentValue;
						result[1] = x;
						result[2] = y;
					}
					if(result[0]<=alpha){
						undoMove(x,y,action,player);
						//System.out.println(x+" "+y + " "+alpha+" "+beta);
						return result;
					}
					beta = Math.min(beta, result[0]);
					undoMove(x,y,action,player);
				}
			}
		}	
		return result;
	}
	
}