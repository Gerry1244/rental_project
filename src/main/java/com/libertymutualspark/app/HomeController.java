package com.libertymutualspark.app;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;

import com.github.mustachejava.Mustache;
import com.libertymutualspark.app.models.Apartment;
import com.libertymutualspark.app.utilities.AutoCloseableDb;
import com.libertymutualspark.app.utilities.MustacheRenderer;

import spark.Request;
import spark.Response;
import spark.Route;
import spark.template.velocity.VelocityTemplateEngine;
import spark.ModelAndView;

public class HomeController {

	public static final Route index = (Request req, Response res) -> {
		try (AutoCloseableDb db = new AutoCloseableDb()) {
		List<Apartment> apartments = Apartment.findAll();
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("apartments", apartments);
		model.put("currentUser", req.session().attribute("currentUser"));
		model.put("noUser", req.session().attribute("currentUser") == null);
			return MustacheRenderer.getInstance().render("home/index.html", model);
		}
		
		// VelocityContext context = new VelocityContext(model);
		// StringWriter writer = new StringWriter();
		//
		// return VelocityContext.getTemplate().render("home/index2.html", model);
		//
	};

	// public String render(String templatePath, Map<String, Object> model) {
	// Mustache mustache = factory.compile(templatePath);
	// StringWriter writer = new StringWriter();
	// mustache.execute(writer, model);
	// return writer.toString();
}
