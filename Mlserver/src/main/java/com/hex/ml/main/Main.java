package com.hex.ml.main;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.Configuration;
import org.eclipse.jetty.webapp.WebAppContext;

public class Main {

	public static void main(String[] args) throws Exception {
		String webappDirLocation = "src/main/webapp/";
		System.setProperty("org.apache.jasper.compiler.disablejsr199", "false");
		
		Server server = new Server(8484);
		WebAppContext root = new WebAppContext();

		root.setContextPath("/");
		root.setDescriptor(webappDirLocation + "/WEB-INF/web.xml");
		root.setResourceBase(webappDirLocation);

		root.setParentLoaderPriority(true);

		Configuration.ClassList classlist = Configuration.ClassList.setServerDefault(server);
		classlist.addBefore(
                "org.eclipse.jetty.webapp.JettyWebXmlConfiguration",
                "org.eclipse.jetty.annotations.AnnotationConfiguration");
		root.setAttribute("org.eclipse.jetty.server.webapp.ContainerIncludeJarPattern",
		          ".*/[^/]*servlet-api-[^/]*\\.jar$|.*/javax.servlet.jsp.jstl-.*\\.jar$|.*/.*taglibs.*\\.jar$");
		server.setHandler(root);
		server.start();
		server.join();
	}
	
	
}