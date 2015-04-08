package com.hex.ml.main;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.Configuration;
import org.eclipse.jetty.webapp.WebAppContext;

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

		Configuration.ClassList classlist = Configuration.ClassList.setServerDefault(server);
		classlist.addBefore(
                "org.eclipse.jetty.webapp.JettyWebXmlConfiguration",
                "org.eclipse.jetty.annotations.AnnotationConfiguration");
		webAppContext.setAttribute("org.eclipse.jetty.server.webapp.ContainerIncludeJarPattern",
		          ".*/[^/]*servlet-api-[^/]*\\.jar$|.*/javax.servlet.jsp.jstl-.*\\.jar$|.*/.*taglibs.*\\.jar$");
		
		HandlerCollection handlers = new HandlerCollection();
		Handler[] handlerArr = new Handler[2];
		handlerArr[0] = webAppContext;
		//handlerArr[1] = 
		handlers.setHandlers(handlerArr);
		
		server.setHandler(webAppContext);
		server.start();
		server.join();
	}
}