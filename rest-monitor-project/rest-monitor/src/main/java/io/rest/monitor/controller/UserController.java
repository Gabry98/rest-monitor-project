package io.rest.monitor.controller;

import java.io.IOException;

import io.quarkus.logging.Log;
import io.rest.monitor.request.UserRequestPayload;
import io.rest.monitor.service.UserService;
import jakarta.inject.Inject;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import static io.rest.monitor.util.ConstantUtils.USER_NOT_FOUND;
import static io.rest.monitor.util.ConstantUtils.RESPONSE_NOT_FOUND;
import static io.rest.monitor.util.ConstantUtils.RESPONSE_INTERNAL_SERVER_ERROR;

@Path("/users")
public class UserController {
	
	@Inject
	private UserService userService;
	
	private static final String USER_ID_PATH_PARAM = "userId";

    @GET
    @Path("/get-user/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(@PathParam(USER_ID_PATH_PARAM) final Integer userId) {
    	try {
    		Response res = Response.ok(userService.getUser(userId)).build();
    		return res;
    	} catch (IllegalArgumentException e) {
    		Log.error(e.getMessage());
    		if (e.getMessage().equals(USER_NOT_FOUND)) {
    			return Response.status(RESPONSE_NOT_FOUND).entity(e.getMessage()).build();
    		}
    		return Response.status(RESPONSE_INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
    	}
    }
    
    @POST
    @Path("/filter-user")
    @Produces(MediaType.APPLICATION_JSON)
    public Response filterUser(final UserRequestPayload payload) {
    	Response res = Response.ok(userService.filterUser(payload)).build();
    	return res;
    }
    
    @POST
    @Path("/create-user")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUser(final UserRequestPayload payload) {
    	try {
    		Response res = Response.ok(userService.createUser(payload)).build();
        	return res;
    	} catch (IllegalArgumentException e) {
    		Log.error(e.getMessage());
    		return Response.status(RESPONSE_INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
    	}
    }
    
    @POST
    @Path("/create-user-with-file")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUserWithFile(final String fileDir) throws IOException {
    	try {
    		Response res = Response.ok(userService.createUserWithFile(fileDir)).build();
        	return res;
    	} catch (IllegalArgumentException e) {
    		return Response.status(RESPONSE_INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
    	}
    }
    
    @PUT
    @Path("/modify-user/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response modifyUser(@PathParam(USER_ID_PATH_PARAM) final Integer userId, final UserRequestPayload payload) {
    	try {
    		Response res = Response.ok(userService.modifyUser(userId, payload)).build();
    		return res;
    	} catch (IllegalArgumentException e) {
    		Log.error(e.getMessage());
    		if (e.getMessage().equals(USER_NOT_FOUND)) {
    			return Response.status(RESPONSE_NOT_FOUND).entity(e.getMessage()).build();
    		}
    		return Response.status(RESPONSE_INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
    	}
    }
    
    @DELETE
    @Path("/delete-user/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteUser(@PathParam(USER_ID_PATH_PARAM) final Integer userId) {
    	Response res = Response.ok(userService.deleteUser(userId)).build();
		return res;
    }
}
