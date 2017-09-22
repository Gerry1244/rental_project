package com.libertymutualspark.app.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.libertymutualspark.app.models.Apartment;
import com.libertymutualspark.app.models.User;
import com.libertymutualspark.app.utilities.AutoCloseableDb;
import com.libertymutualspark.app.utilities.MustacheRenderer;

import spark.Request;
import spark.Response;
import spark.Route;

public class ApartmentController {

	public static final Route details = (Request req, Response res) -> {
		String idAsString = req.params("id");
		int id = Integer.parseInt(idAsString);

		try (AutoCloseableDb db = new AutoCloseableDb()) {
			Apartment apartment = Apartment.findById(id); 
			User currentUser = req.session().attribute("currentUser");
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("apartment", apartment);
			if (currentUser != null) {
				model.put("owner", (currentUser.getId().toString()).equals(apartment.get("user_id").toString()));
			}
			return MustacheRenderer.getInstance().render("apartment/details.html", model);
		
		}
	};

	public static final Route newform = (Request req, Response res) -> {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("currentUser", req.session().attribute("currentUser"));
		model.put("noUser", req.session().attribute("currentUser") == null);
		return MustacheRenderer.getInstance().render("apartment/newform.html", null);
	};

	public static final Route create = (Request req, Response res) -> {
		try (AutoCloseableDb db = new AutoCloseableDb()) {
			Apartment apartment = new Apartment(Integer.parseInt(req.queryParams("rent")),
					Integer.parseInt(req.queryParams("number_of_bedrooms")),
					Double.parseDouble(req.queryParams("number_of_bathrooms")),
					Integer.parseInt(req.queryParams("square_footage")), 
					req.queryParams("address"),
					req.queryParams("city"), 
					req.queryParams("state"), 
					req.queryParams("zip_code"), true
					

			);
			apartment.saveIt();
			User user = req.session().attribute("currentUser");
			System.out.println(user.toString());
			user.add(apartment);
			res.redirect("/");
			return "";
		}
	};

	public static final Route index = (Request req, Response res) -> {
		User currentUser = req.session().attribute("currentUser");
		long id = (long) currentUser.getId();

		try (AutoCloseableDb db = new AutoCloseableDb()) {
			List<Apartment> apartments = Apartment.where("user_id = ? and is_active = ?", id, true);
			List<Apartment> inactiveApartments = Apartment.where("user_id = ? and is_active = ?", id, false);
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("isActive", apartments);
			model.put("notActive", inactiveApartments);
			return MustacheRenderer.getInstance().render("apartment/index.html", model);
		}

	};

	public static Route activate = (Request req, Response res) -> {
		try (AutoCloseableDb db = new AutoCloseableDb()) {
			int id = Integer.parseInt(req.params("id"));
			Apartment apartment = Apartment.findById(id);
			apartment.set("is_active", true);
			apartment.saveIt();
			res.redirect("/apartments/" + id);
			return "";
		}
	};

	public static Route deactivate = (Request req, Response res) -> {
		try (AutoCloseableDb db = new AutoCloseableDb()) {
			int id = Integer.parseInt(req.params("id"));
			Apartment apartment = Apartment.findById(id);
			apartment.set("is_active", false);
			apartment.saveIt();
			res.redirect("/apartments/" + id);
			return "";
		}
	};
	
	
	

}