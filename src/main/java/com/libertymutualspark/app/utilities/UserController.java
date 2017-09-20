package com.libertymutualspark.app.utilities;

import java.util.stream.Collectors;

import org.mindrot.jbcrypt.BCrypt;

import com.libertymutualspark.app.models.User;

import spark.Request;
import spark.Response;
import spark.Route;

public class UserController {

	public static final Route newForm = (Request req, Response res) -> {
		return MustacheRenderer.getInstance().render("users/signup.html", null);
	};

	public static final Route create = (Request req, Response res) -> {
		String encryptedPassword = BCrypt.hashpw(req.queryParams("password"), BCrypt.gensalt());
		User user = new User(req.queryParams("email"), encryptedPassword, req.queryParams("firstName"),
				req.queryParams("lastName")

		);
		try (AutoCloseableDb db = new AutoCloseableDb()) {
			user.saveIt();
			req.session().attribute("currentUser", user);
			res.redirect("/");
			return "";
		}


	};

}