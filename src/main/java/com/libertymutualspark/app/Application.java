package com.libertymutualspark.app;

import static spark.Spark.*;

import org.mindrot.jbcrypt.BCrypt;

import com.libertymutualspark.app.controllers.ApartmentApiController;
import com.libertymutualspark.app.controllers.ApartmentController;
import com.libertymutualspark.app.controllers.HomeController;
import com.libertymutualspark.app.controllers.SessionController;
import com.libertymutualspark.app.controllers.UserController;
import com.libertymutualspark.app.filters.SecurityFilters;
import com.libertymutualspark.app.models.Apartment;
import com.libertymutualspark.app.models.User;
import com.libertymutualspark.app.utilities.AutoCloseableDb;

public class Application {

	public static void main(String[] args) {

		String encryptedPassword = BCrypt.hashpw("password", BCrypt.gensalt());

		try (AutoCloseableDb db = new AutoCloseableDb()) {
			User.deleteAll();
			User curtis = new User("curtis.schlak@theironyard.com", encryptedPassword, "Curtis", "Schlak");
			curtis.saveIt();
			Apartment.deleteAll();
			Apartment apartment = new Apartment(6000, 1, 0, 350, "123 Main St.", "San Francisco", "CA", "95125", true);
			curtis.add(apartment);
			apartment.saveIt();
			apartment = new Apartment(1400, 5, 6, 4000, "123 Cowboy Way", "Houston", "TX", "77006", false);
			curtis.add(apartment);
			apartment.saveIt();
		}

		path("/apartments", () -> {
			before("/new", SecurityFilters.isAuthenticated);
			get("/new", ApartmentController.newForm);

			before("/mine", SecurityFilters.isAuthenticated);
			get("/mine", ApartmentController.index);

			get("/:id", ApartmentController.details);
			post("/:id/activations", ApartmentController.activate);
			post("/:id/deactivations", ApartmentController.deactivate);
			post("/:id/like", ApartmentController.like);

			before("", SecurityFilters.isAuthenticated);
			post("", ApartmentController.create);
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
