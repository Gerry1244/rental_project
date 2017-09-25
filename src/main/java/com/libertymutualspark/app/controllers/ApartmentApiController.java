package com.libertymutualspark.app.controllers;

import com.libertymutualspark.app.models.Apartment;
import com.libertymutualspark.app.utilities.AutoCloseableDb;
import com.libertymutualspark.app.utilities.JsonHelper;

import static spark.Spark.notFound;

import java.util.Map;

import spark.Request;
import spark.Response;
import spark.Route;

public class ApartmentApiController {

	public static final Route details = (Request req, Response res) -> {
		try (AutoCloseableDb db = new AutoCloseableDb()) {
			String idAsString = req.params("id");
			int id = Integer.parseInt(idAsString);
			Apartment apartment = Apartment.findById(id);
			if (apartment != null) {
				res.header("Content-Type", "application/json");
				return apartment.toJson(true);
			}
			notFound("Did not find that apartment.");
			return "";
		}
	};
	public static Route create = (Request req, Response res) -> {
		String json = req.body();
		Map map = JsonHelper.toMap(json);
		Apartment apartment = new Apartment();
		apartment.fromMap(map);
		
		try (AutoCloseableDb db = new AutoCloseableDb()) {
			apartment.saveIt();
			res.status(201);
			return apartment.toJson(true);
		}
		
	};

}
