package com.hex.ml;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class KResult
 */
public class KResult extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public KResult() {
        super();
        
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		KMeansVectors kmv = new KMeansVectors();
		String fileType = request.getParameter("File_Type");
		String inputLocation = request.getParameter("dataset");
		String convergenceDelta = request.getParameter("condelta");
		String maxIterations = request.getParameter("numIter");
		String distanceMeasure = request.getParameter("DM");
		if(inputLocation==null || inputLocation.equals("")){
			request.setAttribute("matrix", "Provide the input location");
			RequestDispatcher reqDispather = request.getRequestDispatcher("/KResult.jsp");
			reqDispather.forward(request, response);
			
		}
		if(convergenceDelta==null || convergenceDelta.equals("")){
			convergenceDelta ="0.5";
		}
		if(distanceMeasure.equals("ED")){
			distanceMeasure ="org.apache.mahout.common.distance.EuclideanDistanceMeasure";
			
		}
		else if(distanceMeasure.equals("MD")){
			
		}
		else if(distanceMeasure.equals("CD")){
			
		}
		else if(distanceMeasure.equals("TD")){
			
		}
		else if(distanceMeasure.equals("WED")){
			
		}
		else if(distanceMeasure.equals("WMD")){
			
		}
		else if(distanceMeasure.equals("SED")){
					
		}
		else if(distanceMeasure.equals("CHD")){
			
		}
		else if(distanceMeasure.equals("MAD")){
			
		}
		else if(distanceMeasure.equals("MID")){
			
		}
		
		if(fileType!=null&&!fileType.equals("")){
			if(fileType.equals("CS")){
				
				try {
					kmv.setFileSperator(",");
					Kmeans km = new Kmeans();
					String kout=km.runKmeans(kmv.getSeqFile(inputLocation), convergenceDelta, maxIterations, distanceMeasure);
					request.setAttribute("matrix", "cluster output location is : "+kout);
					RequestDispatcher reqDispather = request.getRequestDispatcher("/KResult.jsp");
					reqDispather.forward(request, response);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
			else if(fileType.equals("TS")){
				kmv.setFileSperator("\\t");
				try {
					Kmeans km = new Kmeans();
					km.runKmeans(kmv.getSeqFile(inputLocation), convergenceDelta, maxIterations, distanceMeasure);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			else if(fileType.equals("PI")){
				kmv.setFileSperator("|");
				try {
					Kmeans km = new Kmeans();
					km.runKmeans(kmv.getSeqFile(inputLocation), convergenceDelta, maxIterations, distanceMeasure);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			else if(fileType.equals("TX")){
								
			}
			
			
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

}
