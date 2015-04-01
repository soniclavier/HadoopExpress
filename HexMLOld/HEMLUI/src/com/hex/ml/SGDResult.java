package com.hex.ml;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class SGDResult
 */
public class SGDResult extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SGDResult() {
        super();
        
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String inputLocation = request.getParameter("dataset");
		String targetClass = request.getParameter("targetclass");
		String varType = request.getParameter("varType");
		SGDMLP sgd = new SGDMLP();
		String matrix="";
		try {
			matrix=sgd.runSGD(inputLocation,targetClass,varType);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		request.setAttribute("matrix", matrix);
		RequestDispatcher reqDispather = request.getRequestDispatcher("/SGDResult.jsp");
		reqDispather.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

}
