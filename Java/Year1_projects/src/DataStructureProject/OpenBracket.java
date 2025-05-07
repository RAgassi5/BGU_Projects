package DataStructureProject;

public class OpenBracket extends CalcToken {//the class represents the mathematical operator ) 
	
	private char openBracket; 
	
	public OpenBracket(){
		super();
		this.openBracket='(';
	}
	
	public String toString(){
		return "(";
	}
}
