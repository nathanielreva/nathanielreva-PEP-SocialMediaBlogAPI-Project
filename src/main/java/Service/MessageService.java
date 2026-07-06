package Service;
import java.sql.SQLException;
import java.util.List;

import DAO.AccountDAO;
import DAO.MessageDAO;
import Model.Message;

public class MessageService {
    AccountDAO accountDAO = new AccountDAO();
    MessageDAO messageDAO = new MessageDAO();

    public Message addMessage(Message message) throws SQLException{

        if(message.getMessage_text() == null || message.getMessage_text().isBlank()){
            throw new IllegalArgumentException("Message text cannot be blank");
        }

        if(message.getMessage_text().length() > 255){
            throw new IllegalArgumentException("Message text cannot be greater than 255");
        }

        Boolean idExists = accountDAO.isUserIdExists(message.getPosted_by());

        if(!idExists){
            throw new IllegalArgumentException("No user ID found with the post message");
        }

        //add message 
        return messageDAO.create(message);

    }

    public List<Message> getAllMessage() throws SQLException {
        return messageDAO.getAllMessage();
    }
}
