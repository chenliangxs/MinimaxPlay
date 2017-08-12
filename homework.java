import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.PrintWriter;

public class homework{
	public static void main(String[] args) throws FileNotFoundException{
		File inputfile = new File("input.txt");
		Scanner in = new Scanner(inputfile);
		PrintWriter out = new PrintWriter("output.txt");
		int size = Integer.parseInt(in.nextLine());
		String mode = in.nextLine();
		System.out.println(mode);
		String player = in.nextLine();
		String player_2 = "";
		if(player.equals("X")){
			player_2 = "O";
		}
		if(player.equals("O")){
			player_2 = "X";
		}
		int depth = Integer.parseInt(in.nextLine());
		String[] inputValue = new String[size];
		String[] inputOwner = new String[size];
		for(int i = 0;i<size;i++){
			inputValue[i] = in.nextLine();
		}
		for(int j = 0;j<size;j++){
			inputOwner[j] = in.nextLine();
		}	
		int[][] data = new int[size][size];
		int[][] occupency = new int[size][size];
		for(int i=0;i<size;i++){
			in = new Scanner(inputValue[i]);
			for(int j=0;j<size;j++){
				data[i][j] = in.nextInt();
			}
		}
		for(int i=0;i<size;i++){
			for(int j=0;j<size;j++){
				String p = Character.toString(inputOwner[i].charAt(j));
				if(p.equals(player)){
					occupency[i][j] = 1;
				}
				else if(p.equals(".")){
					occupency[i][j] = 0;
				}
				else{
					occupency[i][j] = 2;
				}
			}
		}
		ChessBoard chess = new ChessBoard(data, occupency);
		int[] res = new int[3];
		if(mode.equals("MINIMAX")){
			res = chess.miniMax(1,depth);
		}
		if(mode.equals("ALPHABETA")){
			res = chess.alphaBetaPrune(1, depth, Integer.MIN_VALUE, Integer.MAX_VALUE);
		}
		boolean[] raid = chess.action(res[1],res[2],1);
		boolean isRaid = false;
		for(boolean b:raid){
			if(b==true){
				isRaid = true;
			}
		}
		char col = (char)((char)res[2] + 65);
		if(isRaid){
			out.println(col+""+(res[1]+1)+" "+"Raid");
		}
		else{
			out.println(col+""+(res[1]+1)+" "+"Stake");
		}
		for(int i=0;i<size;i++){
			for(int j=0;j<size;j++){
				if(chess.chessBoard[i][j].owner==1){
					out.print(player);
				}
				else if(chess.chessBoard[i][j].owner==0){
					out.print(".");
				}
				else{
					out.print(player_2);
				}
			}
			out.println();
		}
		
		in.close();
		out.close();
	}
}