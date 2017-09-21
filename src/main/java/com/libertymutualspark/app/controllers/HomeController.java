package com.libertymutualspark.app.controllers;

import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import com.libertymutualspark.app.models.Apartment;
import com.libertymutualspark.app.utilities.AutoCloseableDb;
import com.libertymutualspark.app.utilities.MustacheRenderer;

import spark.Request;
import spark.Response;
import spark.Route;

public class HomeController {

	public static final Route index = (Request req, Response res) -> {
		try (AutoCloseableDb db = new AutoCloseableDb()) {
			List<Apartment> apartments = Apartment.findAll();
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("apartments", apartments);
			model.put("currentUser", req.session().attribute("currentUser"));
			model.put("noUser", req.session().attribute("currentUser") == null);
		return MustacheRenderer.getInstance().render("home/index.html", model);
//		return renderWithVelocity(model);

		}

	};
	
//	private static String renderWithVelocity(Map<String, Object> model) {
//		Properties properties = new Properties();
//		properties.setProperty("resource.loader", "class");
//		properties.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
//		VelocityEngine velocityEngine = new VelocityEngine(properties);
//		String templateEncoding = StandardCharsets.UTF_8.name();
//        Template template = velocityEngine.getTemplate("/templates/home/index2.html", templateEncoding);
//        VelocityContext context = new VelocityContext(model);
//        StringWriter writer = new StringWriter();
//        template.merge(context, writer);
//        return writer.toString();
//	}

}
