
public class ChessNode{
	public int value;
	public int owner;
	
	public ChessNode(int value){
		this.value = value;
		owner = 0;
	}
	public int getValue(){
		return value;
	}
	public int getOwner(){
		return owner;
	}
	
}