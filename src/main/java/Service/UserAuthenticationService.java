package Service;

import java.sql.SQLException;

import DAO.AccountDAO;
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

    public Boolean login(Account account) throws SQLException{
        if(account.getUsername() == null || account.getUsername().isBlank()){
            throw new IllegalArgumentException("Username cannot be null or blank");
        }

        String passwordDB  = accountDAO.getAccountPasswordByUsername(account.getUsername());

        if(passwordDB.equals(account.getPassword())){
            return true;
        }else{
            return false;
        }
        
    }

}

