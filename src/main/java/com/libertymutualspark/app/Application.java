package com.libertymutualspark.app;

import static spark.Spark.*;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.javalite.activejdbc.Base;
import org.mindrot.jbcrypt.BCrypt;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.libertymutualspark.app.models.Apartment;
import com.libertymutualspark.app.models.User;
import com.libertymutualspark.app.utilities.ApartmentApiController;
import com.libertymutualspark.app.utilities.AutoCloseableDb;
import com.libertymutualspark.app.utilities.MustacheRenderer;
import com.libertymutualspark.app.utilities.UserController;

public class Application {

	public static void main(String[] args) {
		
		String encryptedPassword = BCrypt.hashpw("password", BCrypt.gensalt());

		try (AutoCloseableDb db = new AutoCloseableDb()) {
			User.deleteAll();
			new User("curtis.schlak@theironyard.com", encryptedPassword, "Curtis", "Schlak").saveIt();
			
			Apartment.deleteAll();
			new Apartment(6200, 2, 0d, 350, "123 Main St", "San Francisco", "CA", "95125").saveIt();
			new Apartment(1200, 4, 6d, 4000, "789 Hillcrest Drive", "Manchester", "NH", "03104").saveIt();
			new Apartment(1459, 3, 6d, 4000, "456 Cowboy Way", "Houston", "TX", "77006").saveIt();

			get("/", HomeController.index);
			get("/apartments/:id", ApartmentController.details);
			get("/login", SessionController.newForm);
			post("/login", SessionController.create);
			get("/users/new", UserController.newForm);
			post("/users", UserController.create);
			get("/logout", SessionController.destroy);
			
			path("/api", () -> {
				get("/apartments/:id", ApartmentApiController.details);
				post("/apartments", ApartmentApiController.create);
				
				
			});
				
			
		}

	}
}
