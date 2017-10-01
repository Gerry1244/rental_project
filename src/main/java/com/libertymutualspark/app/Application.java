package com.libertymutualspark.app;

import static spark.Spark.*;

import org.mindrot.jbcrypt.BCrypt;

import com.libertymutualspark.app.controllers.ApartmentApiController;
import com.libertymutualspark.app.controllers.ApartmentController;
import com.libertymutualspark.app.controllers.HomeController;
import com.libertymutualspark.app.controllers.SessionApiController;
import com.libertymutualspark.app.controllers.SessionController;
import com.libertymutualspark.app.controllers.UserController;
import com.libertymutualspark.app.controllers.UsersApiController;
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

			User gerry = new User("gerry.bradley@libertymutual.com", encryptedPassword, "Gerry", "Bradley");
			gerry.saveIt();
			apartment = new Apartment(6000, 1, 0, 350, "456 East Main St.", "San Francisco", "CA", "95125", true);
			gerry.add(apartment);
			apartment.saveIt();
			apartment = new Apartment(1400, 5, 6, 4000, "789 West Cowboy Way", "Houston", "TX", "77006", false);
			gerry.add(apartment);
			apartment.saveIt();

		}

		enableCORS("http://localhost:4200", "*", "*");

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
			get("/apartments/mine", ApartmentApiController.myListings);
			get("/apartments/:id", ApartmentApiController.details);
			post("/apartments", ApartmentApiController.create);
			get("/apartments", ApartmentApiController.index);
			post("/apartments/:id/activations", ApartmentApiController.activate);
			post("/apartments/:id/deactivations", ApartmentApiController.deactivate);
			post("/apartments/:id/like", ApartmentApiController.like);
			post("/users/", UsersApiController.create);
			
			
			post("/sessions", SessionApiController.create);
			delete("/sessions/mine", SessionApiController.destroy);

		});
	}

	private static void enableCORS(final String origin, final String methods, final String headers) {

		options("/*", (request, response) -> {

			String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
			if (accessControlRequestHeaders != null) {
				response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
			}

			String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
			if (accessControlRequestMethod != null) {
				response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
			}

			return "OK";
		});

		before((request, response) -> {
			response.header("Access-Control-Allow-Origin", origin);
			response.header("Access-Control-Request-Method", methods);
			response.header("Access-Control-Allow-Headers", headers);
			response.header("Access-Control-Allow-Credentials", "true");
		});
	}

}
