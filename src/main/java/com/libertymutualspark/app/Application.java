package com.libertymutualspark.app;

import static spark.Spark.*;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.javalite.activejdbc.Base;
import org.mindrot.jbcrypt.BCrypt;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.libertymutualspark.app.controllers.ApartmentApiController;
import com.libertymutualspark.app.controllers.ApartmentController;
import com.libertymutualspark.app.controllers.HomeController;
import com.libertymutualspark.app.controllers.SessionController;
import com.libertymutualspark.app.controllers.UserController;
import com.libertymutualspark.app.filters.Securityfilters;
import com.libertymutualspark.app.models.Apartment;
import com.libertymutualspark.app.models.User;
import com.libertymutualspark.app.utilities.AutoCloseableDb;
import com.libertymutualspark.app.utilities.MustacheRenderer;

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
			Apartment apartment = new Apartment(6200, 2, 0d, 350, "123 Main St", "San Francisco", "CA", "95125", true);
			apartment.save();
			curtis.add(apartment);
			
			apartment = new Apartment(1200, 4, 6d, 4000, "789 Hillcrest Drive", "Manchester", "NH", "03104", true);
			apartment.save();
			curtis.add(apartment);
			
			apartment = new Apartment(1459, 3, 6d, 4000, "456 Cowboy Way", "Houston", "TX", "77006", false);
			apartment.save();
			curtis.add(apartment);
			
			
			path("/apartments",() -> {
				
				before("/new", Securityfilters.isAuthenticated);
				get("/new", ApartmentController.newform);
				
				before("/mine", Securityfilters.isAuthenticated);
				get ("/mine", ApartmentController.index);
				
				get("/:id", ApartmentController.details);
				post("/:id/activations", ApartmentController.activate);
				post("/:id/deactivations", ApartmentController.deactivate);
				
				before("", Securityfilters.isAuthenticated);
				post("",     ApartmentController.create);
			});
			
			
			get("/", HomeController.index);
			get("/login", SessionController.newForm);
			get("/users/new", UserController.newForm);
			post("/logout", SessionController.destroy);
			post("/users", UserController.create);
			post("/login", SessionController.create);
			
			
			path("/api", () -> {
				get("/apartments/:id", ApartmentApiController.details);
				post("/apartments", ApartmentApiController.create);
				
				
			});
				
		}

	}
}
