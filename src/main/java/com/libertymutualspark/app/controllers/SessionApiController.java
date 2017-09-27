package com.libertymutualspark.app.controllers;

import java.util.Map;

import org.mindrot.jbcrypt.BCrypt;

import com.libertymutualspark.app.models.User;
import com.libertymutualspark.app.utilities.AutoCloseableDb;
import com.libertymutualspark.app.utilities.JsonHelper;

import spark.Request;
import spark.Response;
import spark.Route;

public class SessionApiController {

	public class SessionApiController {

		public static final Route create = (Request req, Response res) -> {
			try (AutoCloseableDb db = new AutoCloseableDb()) {
				String password = (String) Map.get("password");
				String email = (String) Map.get("email");
				User user = User.findFirst("email = ?", email);
				if (user != null && BCrypt.checkpw(password, user.getPassword())) {
					req.session().attribute("currentUser", user);
					req.session().attribute("____", "application/json");
					return user.toJson(true);
				}
				notFound("Did not find user");

			}

		};
	}
}
