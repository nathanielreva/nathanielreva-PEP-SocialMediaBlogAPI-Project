package Controller;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import Exception.InvalidPasswordException;
import Exception.InvalidUsernameException;
import Exception.UsernameExistsException;
import Model.Account;
import Model.Message;
import Service.MessageService;
import Service.UserAuthenticationService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    private final UserAuthenticationService userAuthenticationService = new UserAuthenticationService();
    private final MessageService ms = new MessageService();

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();

        app.exception(IllegalArgumentException.class, (e, ctx) -> {
            ctx.status(400);
        });

        app.exception(UsernameExistsException.class, (e, ctx) -> {
            ctx.status(400);
        });
        app.exception(InvalidUsernameException.class, (e, ctx) -> {
            ctx.status(401);
        });
        app.exception(InvalidPasswordException.class, (e, ctx) -> {
            ctx.status(401);
        });

        
        app.post("register", this::register );
        app.post("login", this::login);
        app.post("messages", this::messages);
        app.get("messages", this::getMessages);
        app.get("/messages/{message_id}", this::getMessageById);
        app.delete("/messages/{message_id}", this::deleteMessageById);
        app.patch("/messages/{message_id}", this::patchMessageById);
        app.get("/accounts/{account_id}/messages", this::getAllMessagesByUserAccountId);
        return app;

    
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */


    private void register(Context context) throws SQLException{
        Account account = context.bodyAsClass(Account.class); 
        Account register = userAuthenticationService.register(account);
        context.json(register);
    }

    private void login(Context context) throws SQLException{
        Account account = context.bodyAsClass(Account.class);
        Account logged_in = userAuthenticationService.login(account);
        context.json(logged_in);
    }

    private void messages(Context context) throws SQLException{
        Message message = context.bodyAsClass(Message.class);        
        context.json(ms.addMessage(message));
    }


    private void getMessages(Context context) throws SQLException{
        List<Message> messages = ms.getAllMessage();
        context.json(messages);
        if(messages != null){
            context.json(messages);
        }else{
            context.status(200);
        }
    }

    private void getMessageById(Context context)throws SQLException{
        int message_id = Integer.parseInt(context.pathParam("message_id"));
        Message msg = ms.getMessageById(message_id);
        if(msg != null){
            context.json(msg);
        }else{
            context.status(200);
        }
    }

    private void deleteMessageById(Context context)throws SQLException{
        int message_id = Integer.parseInt(context.pathParam("message_id"));
    
        int count = ms.deleteMessageById(message_id);
        if(count > 0){
            context.status(200).json(count);
        }else{
            context.status(200);
        }
    }

    private void patchMessageById(Context context)throws SQLException{

        int message_id = Integer.parseInt(context.pathParam("message_id"));
        Message message = context.bodyAsClass(Message.class);
 
        int count = ms.patchMessageById(message_id, message.getMessage_text());
        
        if(count > 0){
            context.status(200).json(count);
        }else{
            context.status(400);
        }
    }

    public void getAllMessagesByUserAccountId(Context context) throws SQLException{
        Integer account_id = Integer.parseInt(context.pathParam("account_id"));

        context.json(ms.getAllMessagesByUserAccountId(account_id));
        

    }
}