package in.hadoopexpress;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("status")
public class Status {

    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("namenode")
    public String getIt() {
        return "{nodetype:\"namenode\", status:\"yellow\"}";
    }
    
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("datanode")
    public String getIt2() {
    	return "{nodetype:\"datanode\", status:\"green\"}";
    }
}
