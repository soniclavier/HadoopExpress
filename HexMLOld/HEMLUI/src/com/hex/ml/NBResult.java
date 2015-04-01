package com.hex.ml;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class NBResult
 */
public class NBResult extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public NBResult() {
        super();
       
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String hdfsLocation = request.getParameter("dataset");
		String percentageSplit = request.getParameter("percentage");
		NBMLP nbml = new NBMLP();
		String matrix="";
		try {
			matrix=nbml.createModel(percentageSplit, hdfsLocation);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		request.setAttribute("matrix", matrix);
		RequestDispatcher reqDispather = request.getRequestDispatcher("/NBResult.jsp");
		reqDispather.forward(request, response);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

}
