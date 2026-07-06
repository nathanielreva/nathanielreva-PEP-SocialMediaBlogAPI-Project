package DAO;

import Model.Account;
import Util.ConnectionUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AccountDAO {
    
    Connection conx = ConnectionUtil.getConnection();

    public Account create(Account account) throws SQLException{
        
        String query = "INSERT INTO account (username, password) VALUES (?, ?)";
        PreparedStatement ps = conx.prepareStatement(query,  Statement.RETURN_GENERATED_KEYS);


        ps.setString(1, account.getUsername());
        ps.setString(2, account.getPassword());

        int rowsAffected = ps.executeUpdate();

        if(rowsAffected > 0){
            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next()){
                account.setAccount_id(rs.getInt(1));
            }
        }
        
        return account;
    }

    public boolean isUsernameExist(String username) throws SQLException{
        String query = "SELECT * FROM account WHERE username = ?";

        PreparedStatement ps = conx.prepareStatement(query);
        ps.setString(1, username);

        ResultSet rs = ps.executeQuery();
        return rs.next();

    }

    public boolean isUserIdExists(int id) throws SQLException{
        String query = "SELECT * FROM account WHERE account_id = ?";

        PreparedStatement ps = conx.prepareStatement(query);
        ps.setInt(1, id);

        ResultSet rs = ps.executeQuery();
        return rs.next();
    }

    public Account getAccountByUsername(String username) throws SQLException{
        String query = "SELECT * FROM account WHERE username = ? ";

        PreparedStatement ps = conx.prepareStatement(query);
        ps.setString(1, username);

        ResultSet rs = ps.executeQuery();
        if(rs.next()){
            return new Account(
                rs.getInt(1),
                rs.getString(2),
                rs.getString(3)
            );
        }
        return null;
    }
    
}
