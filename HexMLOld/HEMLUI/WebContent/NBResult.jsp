<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@page import="java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>HEX-ML:NaiveBayes</title>

    <!-- Bootstrap Core CSS -->
    <link href="css/bootstrap.min.css" rel="stylesheet">
    <!-- MetisMenu CSS -->
    <link href="css/metisMenu.min.css" rel="stylesheet">
    <!-- Custom CSS -->
    <link href="css/sb-admin-2.css" rel="stylesheet">
    <!-- Custom CSS -->
    <link href="css/simple-sidebar.css" rel="stylesheet">
    
    <link href="css/font-awesome.min.css" rel="stylesheet">
     <!-- Custom CSS -->
    <link href="css/custom.css" rel="stylesheet">
    
    


    
    

    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
        <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->

</head>

<body>

    <div id="wrapper">

        <!-- Sidebar -->
        <div class="navbar-default sidebar" role="navigation" id="sidebar-wrapper">
            <ul class="nav in" id="side-menu">
                <li class="sidebar-brand">
                    <a href="#">
                        HEX-ML
                    </a>
                </li>
                   <li>
                    <a href="#">Overview</a>
                </li>

                 <li>
                            <a href="#"> HEX-Modeler<span class="fa fa-angle-left"></span></a>
                            <ul class="nav nav-second-level collapse">
                                <li>
                                    <a href="NaiveBayes.html">Naive Bayes</a>
                                </li>
                                <li>
                                    <a href="SGD.html">Logistic Regression</a>
                                </li>
                                <li>
                                    <a href="RandomForest.html">Random Forest</a>
                                </li>
                                <li>
                                    <a href="HiddenMarkovModels.html">Hidden Markov Models</a>
                                </li>
                                <li>
                                    <a href="MultilayerPerceptron.html"> Multilayer Perceptron</a>
                                </li>
                                <li>
                                    <a href="Linearsupportvectormachines.html">Linear support vector machines (SVM)</a>
                                </li>
                                 <li>
                                    <a href="KMeans.html">K-Means</a>
                                </li>
                                 <li>
                                    <a href="FuzzykMeans.html">Fuzzy k-Means</a>
                                </li>
                                 <li>
                                    <a href="StreamingkMeans.html">Streaming k-Means</a>
                                </li>
                                 <li>
                                    <a href="SpectralClustering.html">Spectral Clustering</a>
                                </li>
                                
                            </ul>
                            <!-- /.nav-second-level -->
                        </li>      
                <li>
                    <a href="#">Contact</a>
                </li>
            </ul>
        </div>
        <!-- /#sidebar-wrapper -->

        <!-- Page Content -->
        <div id="page-content-wrapper">
            <div class="container-fluid">
                <div class="row">
                    <div class="col-lg-12">
                        <h1>HEX-ML (Naive Bayes Result)</h1>
                        <p>Naive Bayes classifiers are a family of simple probabilistic classifiers based on applying Bayes theorem. Mahout currently has two Naive Bayes implementations. 
                           The first is standard multinomial Naive Bayes and second is  an implementation of Transformed Weight-normalized Complement Naive Bayes as CBayes.</p>
                        
                                          
                    </div>
                  <div class="col-lg-5">
                   <p></p>
                   <p></p>  
                   <p></p>
                   <p></p> 
                      <%
                      Object matrix = request.getAttribute("matrix");
                     
                       %>                 	
                  
	                  <p><%=matrix.toString() %></p>  
              
	              </div>
	            
	            <p></p>

				 
	         </div>
                </div>
            </div>
         
        </div>
        <!-- /#page-content-wrapper -->

  
    <!-- /#wrapper -->

    <!-- jQuery -->
    <!-- <script src="js/jquery.js"></script> -->

     <!-- jQuery -->
    <script src="js/jquery.min.js"></script>
    <!-- Bootstrap Core JavaScript -->
    <script src="js/bootstrap.min.js"></script>
         <!-- Metis Menu Plugin JavaScript -->
    <script src="js/metisMenu.min.js"></script>

	
    <!-- Custom Theme JavaScript -->
    <script src="js/sb-admin-2.js"></script>
    
    <!-- Menu Toggle Script -->
    <script>
    $("#menu-toggle").click(function(e) {
        e.preventDefault();
        $("#wrapper").toggleClass("toggled");
    });
    </script>
    

    

    

</body>

</html>