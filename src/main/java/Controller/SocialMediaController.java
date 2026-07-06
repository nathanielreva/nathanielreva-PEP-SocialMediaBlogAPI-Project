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

        MessageService ms = new MessageService();
        
        context.json(ms.addMessage(message));

    }

    private void getMessages(Context context) throws SQLException{
        MessageService ms = new MessageService();

        List<Message> messages = ms.getAllMessage();

        context.json(messages);
    }
}