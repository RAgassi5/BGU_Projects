package DataStructureProject;

/**
 * Interface describing a standard Stack ADT.
 */
public interface Stack { 
  
	/** Add o to the top of this Stack.
	 * Precondition: the stack is not full.
	 * @param o The object to be pushed.
	 */
	public void push(Object o);
	 
	/** Remove and return the top element of this Stack.
	 * Precondition: the stack is not empty.
	 * If trying to pop from an empty stack should return null.
	 * @return the formerly top element of the stack.
	 */
	public Object pop();

	 /** Returns whether the stack is empty.
	  * @return true if and only if the stack is empty
	  */
	public boolean isEmpty();
	 



}