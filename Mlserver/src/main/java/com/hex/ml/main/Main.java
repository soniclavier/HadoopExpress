package com.hex.ml.main;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

public class Main {

	public static void main(String[] args) throws Exception {
		String webappDirLocation = "src/main/webapp/";

		Server server = new Server(8484);
		WebAppContext root = new WebAppContext();

		root.setContextPath("/");
		root.setDescriptor(webappDirLocation + "/WEB-INF/web.xml");
		root.setResourceBase(webappDirLocation);

		root.setParentLoaderPriority(true);

		server.setHandler(root);

		server.start();
		server.join();
	}
}