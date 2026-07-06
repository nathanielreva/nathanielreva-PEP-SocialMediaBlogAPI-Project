package Service;

import java.sql.SQLException;

import DAO.AccountDAO;
import Exception.InvalidPasswordException;
import Exception.InvalidUsernameException;
import Exception.UsernameExistsException;
import Model.Account;

public class UserAuthenticationService {
    AccountDAO accountDAO = new AccountDAO();

    public Account register(Account account) throws SQLException{
        if(account.getUsername() == null || account.getUsername().isBlank()){
            throw new IllegalArgumentException("Username cannot be null or blank");
        }
        if(account.getPassword() == null || account.getPassword().isBlank() || account.getPassword().length() < 4){
            throw new IllegalArgumentException("Password cannot be less then 4 characters.");
        }
        
        if(accountDAO.isUsernameExist(account.username)){
            throw new UsernameExistsException("username in database");
        }
        
        return accountDAO.create(account);
        
    };

    public Account login(Account account) throws SQLException{
        if(account.getUsername() == null || account.getUsername().isBlank()){
            throw new IllegalArgumentException("Username cannot be null or blank");
        }

        Account passwordDB  = accountDAO.getAccountByUsername(account.getUsername());

        if (passwordDB == null) {
            throw new InvalidUsernameException("User not found");
        }

        if(passwordDB.getPassword().equals(account.getPassword())){
            return passwordDB;
        }else{
            throw new InvalidPasswordException("Invalid password");
        }
        
      
    }

}

