package DataStructureProject;

public class CloseBracket extends CalcToken {//the class represents the mathematical operator ( 
	
	private char closeBracket; 
	
	public CloseBracket(){
		super();
		this.closeBracket=')';
	}
	
	public String toString(){
		return ")";
	}
}

