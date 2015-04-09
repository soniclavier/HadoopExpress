package com.hex.ml.main;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.Configuration;
import org.eclipse.jetty.webapp.WebAppContext;
import org.glassfish.jersey.servlet.ServletContainer;

import com.hex.ml.logistic.SgdService;

public class Main {

	public static void main(String[] args) throws Exception {
		String webappDirLocation = "src/main/webapp/";
		System.setProperty("org.apache.jasper.compiler.disablejsr199", "false");

		Server server = new Server(8484);

		WebAppContext webAppContext = new WebAppContext();
		webAppContext.setContextPath("/");
		webAppContext.setDescriptor(webappDirLocation + "/WEB-INF/web.xml");
		webAppContext.setResourceBase(webappDirLocation);
		webAppContext.setParentLoaderPriority(true);

		Configuration.ClassList classlist = Configuration.ClassList
				.setServerDefault(server);
		classlist.addBefore(
				"org.eclipse.jetty.webapp.JettyWebXmlConfiguration",
				"org.eclipse.jetty.annotations.AnnotationConfiguration");
		webAppContext
				.setAttribute(
						"org.eclipse.jetty.server.webapp.ContainerIncludeJarPattern",
						".*/[^/]*servlet-api-[^/]*\\.jar$|.*/javax.servlet.jsp.jstl-.*\\.jar$|.*/.*taglibs.*\\.jar$");

		ServletContextHandler restContext = new ServletContextHandler(
				ServletContextHandler.SESSIONS);
		restContext.setContextPath("/rest");
		ServletHolder jerseyServlet = new ServletHolder(new ServletContainer());
		//ServletHolder jerseyServlet = restContext.addServlet(org.glassfish.jersey.servlet.ServletContainer.class, "/*");
		jerseyServlet.setInitOrder(0);
		jerseyServlet.setInitParameter(
				"jersey.config.server.provider.classnames",
				SgdService.class.getCanonicalName());
		restContext.addServlet(jerseyServlet, "/rest");
		HandlerCollection handlers = new HandlerCollection();
		Handler[] handlerArr = new Handler[2];
		handlerArr[0] = webAppContext;
		handlerArr[1] = restContext;
		handlers.setHandlers(handlerArr);

		server.setHandler(handlers);
		server.start();
		server.join();
	}
}