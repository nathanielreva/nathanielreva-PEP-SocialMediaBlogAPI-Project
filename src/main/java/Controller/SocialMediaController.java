package Controller;

import java.sql.SQLException;
import java.util.Map;

import Exception.UsernameExistsException;
import Model.Account;
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
            ctx.status(400).json(Map.of("exception", e.getMessage()));
        });

        app.exception(UsernameExistsException.class, (e, ctx) -> {
            ctx.status(400).json(Map.of("exeption", e.getMessage()));
        });
        
        app.post("register", this::register );
        app.post("login", this::login);
        return app;

    
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */


    private void register(Context context) throws SQLException{
        Account account = context.bodyAsClass(Account.class); 

        String username = account.getUsername();
        String password = account.getPassword();

        Account register = userAuthenticationService.register(username, password);

        context.json(register);
        
    }

    private void login(Context context) throws SQLException{
        Account account = context.bodyAsClass(Account.class);

        String username = account.getUsername();
        String password = account.getPassword();

        Boolean logged_in = userAuthenticationService.login(username, password);
        
        context.status(logged_in ? 200 : 401).json(Map.of(
            "logged_in" , logged_in
        ));
    }
}