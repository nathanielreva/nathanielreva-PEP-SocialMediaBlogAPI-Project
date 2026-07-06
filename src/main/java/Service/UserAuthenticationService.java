package Service;

import java.sql.SQLException;

import DAO.AccountDAO;
import Exception.UsernameExistsException;
import Model.Account;

public class UserAuthenticationService {
    AccountDAO accountDAO = new AccountDAO();

    public Account register(String username, String password) throws SQLException{
        if(username == null || username.isBlank()){
            throw new IllegalArgumentException("Username cannot be null or blank");
        }
        if(password == null || password.isBlank() || password.length() < 4){
            throw new IllegalArgumentException("Password cannot be less then 4 characters.");
        }
        
        if(accountDAO.isUsernameExist(username)){
            throw new UsernameExistsException("username in database");
        }
        
        return accountDAO.create(new Account(username, password));
        
    };

    public Boolean login(String username, String password) throws SQLException{
        if(username == null || username.isBlank()){
            throw new IllegalArgumentException("Username cannot be null or blank");
        }
        if(password == null || password.isBlank() || password.length() < 4){
            throw new IllegalArgumentException("Password cannot be less then 4 characters.");
        }

        String password_db  = accountDAO.getStoredPassword(username);

        if(password_db.equals(password)){
            return true;
        }else{
            return false;
        }
        
    }

}

