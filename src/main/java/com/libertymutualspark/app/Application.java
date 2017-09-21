package com.libertymutualspark.app;

import static spark.Spark.*;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.javalite.activejdbc.Base;
import org.mindrot.jbcrypt.BCrypt;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.libertymutualspark.app.filters.Securityfilters;
import com.libertymutualspark.app.models.Apartment;
import com.libertymutualspark.app.models.User;
import com.libertymutualspark.app.utilities.ApartmentApiController;
import com.libertymutualspark.app.utilities.AutoCloseableDb;
import com.libertymutualspark.app.utilities.MustacheRenderer;
import com.libertymutualspark.app.utilities.UserController;

import spark.Request;
import spark.Response;

public class Application {

	public static void main(String[] args) {
		
		String encryptedPassword = BCrypt.hashpw("password", BCrypt.gensalt());

		try (AutoCloseableDb db = new AutoCloseableDb()) {
			User.deleteAll();
			
			 User curtis = new User("curtis.schlak@theironyard.com", encryptedPassword, "Curtis", "Schlak");
			 curtis.saveIt();
			
			
			Apartment.deleteAll();
			Apartment apartment = new Apartment(6200, 2, 0d, 350, "123 Main St", "San Francisco", "CA", "95125");
			apartment.save();
			curtis.add(apartment);
			
			apartment = new Apartment(1200, 4, 6d, 4000, "789 Hillcrest Drive", "Manchester", "NH", "03104");
			apartment.save();
			curtis.add(apartment);
			
			apartment = new Apartment(1459, 3, 6d, 4000, "456 Cowboy Way", "Houston", "TX", "77006");
			apartment.save();
			curtis.add(apartment);
			
			
			path("/apartments",() -> {
				
				before("/new", Securityfilters.isAuthenticated);
				get("/new", ApartmentController.newform);
				
				before("/mine", Securityfilters.isAuthenticated);
				get ("/mine", ApartmentController.index);
				
				get("/:id", ApartmentController.details);
				
				before("", Securityfilters.isAuthenticated);
				post("",     ApartmentController.create);
			});
			
			
			get("/", HomeController.index);
			get("/login", SessionController.newForm);
			post("/login", SessionController.create);
			get("/users/new", UserController.newForm);
			post("/users", UserController.create);
//			get("/logout", SessionController.destroy);
			
			path("/api", () -> {
				get("/apartments/:id", ApartmentApiController.details);
				post("/apartments", ApartmentApiController.create);
				
				
			});
				
		}

	}
}
