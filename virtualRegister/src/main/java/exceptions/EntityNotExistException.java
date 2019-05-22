package exceptions;

public class EntityNotExistException extends Exception{

	private static final long serialVersionUID = 1L;
	private static final String MESSAGE_FORMAT = "Entity '%d' does not exist";
	private static final String MESSAGE_FORMAT_STRING = "Entity '%s' does not exist";

	    public EntityNotExistException(Long id) {
	    	super(String.format(MESSAGE_FORMAT, id));
	    }

		public EntityNotExistException(String paramName) {
	    	super(String.format(MESSAGE_FORMAT_STRING, paramName));
		}
}