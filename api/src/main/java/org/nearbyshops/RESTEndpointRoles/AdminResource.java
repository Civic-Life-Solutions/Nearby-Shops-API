package org.nearbyshops.RESTEndpointRoles;

import org.nearbyshops.DAOsPreparedRoles.AdminDAOPrepared;
import org.nearbyshops.Globals.GlobalConstants;
import org.nearbyshops.Globals.Globals;
import org.nearbyshops.ModelRoles.Admin;
import org.nearbyshops.ModelRoles.Staff;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.net.URI;


@Path("/v1/Admin")
public class AdminResource {


	private AdminDAOPrepared daoPrepared = Globals.adminDAOPrepared;

	public AdminResource() {
		super();
		// TODO Auto-generated constructor stub
	}


	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed(GlobalConstants.ROLE_ADMIN)
	public Response updateAdmin(Admin admin)
	{
		if(Globals.accountApproved instanceof Admin)
		{

			Admin adminApproved = (Admin) Globals.accountApproved;
			admin.setAdminID(adminApproved.getAdminID());
			int rowCount = daoPrepared.updateAdmin(admin);

			if(rowCount >= 1)
			{

				return Response.status(Status.OK)
						.entity(null)
						.build();
			}
			if(rowCount == 0)
			{

				return Response.status(Status.NOT_MODIFIED)
						.entity(null)
						.build();
			}
		}
		else
		{
			throw new ForbiddenException("Not Permitted !");
		}

		return null;
	}




	@GET
	@Path("Login")
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed({GlobalConstants.ROLE_ADMIN})
	public Response getDistributorLogin()
	{
		//@Context HttpHeaders header
		if(Globals.accountApproved instanceof Admin)
		{
			Admin adminApproved = (Admin) Globals.accountApproved;

			Admin admin = Globals.adminDAOPrepared.getAdmin(adminApproved.getAdminID());

			if(admin != null)
			{

				return Response.status(Status.OK)
						.entity(admin)
						.build();

			} else
			{

				return Response.status(Status.UNAUTHORIZED)
						.build();

			}

		}
		else
		{
			throw new ForbiddenException("Not Permitted !");
		}

	}

}
