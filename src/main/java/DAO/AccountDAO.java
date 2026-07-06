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

    public String getStoredPassword(String username) throws SQLException{
        String query = "SELECT password FROM account WHERE username = ? ";

        PreparedStatement ps = conx.prepareStatement(query);
        ps.setString(1, username);

        ResultSet rs = ps.executeQuery();
        if(rs.next()){
            return rs.getString(1);
        }
        return "";
    }
    
}
