package sample.todosapp.error;

/**
 *  Common error messages.
 * 
 * @author relai
 */
public class ErrorMessages {

    public static final String CONFLICT_MSG  = "The item has been changed or deleted.";
    public static final String NOT_FOUND_MSG = "The item cannot be found.";  
    
    public static final Message CONFLICT_ERR  = new Message("conflict", CONFLICT_MSG);   
    public static final Message NOT_FOUND_ERR = new Message("not_found", NOT_FOUND_MSG);
    
    
    public static class Message {
        private  String errorCode;
        private  String errorMessage;
        
        public Message () {}

        public Message(String errorCode, String errorMessage) {
            this.errorCode = errorCode;
            this.errorMessage = errorMessage;
        }

        public void   setErrorCode(String code) {errorCode = code;}
        public String getErrorCode() {return errorCode;}

        public void   setErrorMessage(String msg) {errorMessage = msg;}
        public String getErrorMessage() {return errorMessage;}
    }
    
}
