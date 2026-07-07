package DAO;

import Util.ConnectionUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Model.Message;

public class MessageDAO {
    Connection conx = ConnectionUtil.getConnection();


    public Message create(Message message) throws SQLException{

        String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";

        PreparedStatement ps = conx.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.setInt(1, message.getPosted_by());
        ps.setString(2, message.getMessage_text());
        ps.setLong(3, message.getTime_posted_epoch());

        int rowsAffected = ps.executeUpdate();

        if(rowsAffected > 0){
            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next()){
                message.setMessage_id(rs.getInt(1));
            }
        }
        return message;
    }

    public List<Message> getAllMessage() throws SQLException {

        List<Message> messages = new ArrayList<>();

        String sql = "SELECT * FROM message";

        PreparedStatement ps = conx.prepareStatement(sql);

        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            Message message = new Message();
            message.setMessage_id(rs.getInt("message_id"));
            message.setPosted_by(rs.getInt("posted_by"));
            message.setMessage_text(rs.getString("message_text"));
            message.setTime_posted_epoch(rs.getLong("time_posted_epoch"));

            messages.add(message);
        }

        return messages;
    }

    public Message getMessageById(int id) throws SQLException{

        String sql = "SELECT * FROM message WHERE message_id = ?";
        PreparedStatement ps = conx.prepareStatement(sql);

        ps.setInt(1, id);

        ResultSet rs = ps.executeQuery();
        
        if(rs.next()){
            return new Message(
                rs.getInt("message_id"),
                rs.getInt("posted_by"),
                rs.getString("message_text"),
                rs.getLong("time_posted_epoch")
            );
        }
        return null;
    }

    public Message deleteMessageById(int id) throws SQLException{
        Message message = this.getMessageById(id);

        if(message == null){
            return null;
        }

        String sql = "DELETE FROM message WHERE message_id = ?";
        PreparedStatement ps = conx.prepareStatement(sql);
        ps.setInt(1, id);
        ps.execute();

        return message;
    }

    public Message patchMessageById(int id, String message_text) throws SQLException {
        Message message = this.getMessageById(id);

        if(message == null){
            return null;
        }

        String sql = "UPDATE message SET message_text = ? WHERE message_id = ? ";
        PreparedStatement ps = conx.prepareStatement(sql);

        ps.setString(1, message_text);
        ps.setInt(2, id);

        int rowsAffected = ps.executeUpdate();
        if(rowsAffected == 1){
            message.setMessage_text(message_text);
        }

        return message;
    }

    public ArrayList<Message> getAllMessagesByUserAccountId(int id) throws SQLException{
        ArrayList<Message> messages = new ArrayList<>();

        String sql = "SELECT * FROM message WHERE posted_by = ?";
        PreparedStatement ps = conx.prepareStatement(sql);

        ps.setInt(1, id);

        ResultSet rs = ps.executeQuery();

        while(rs.next()){
            messages.add(
                new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch")
                )
            );
        }
        return messages;
    }

}
