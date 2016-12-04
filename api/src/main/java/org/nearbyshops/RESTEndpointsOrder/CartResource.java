package org.nearbyshops.RESTEndpointsOrder;

import org.nearbyshops.Globals.Globals;
import org.nearbyshops.Model.Cart;

import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.net.URI;
import java.util.List;


@Path("/Cart")
public class CartResource {


	public CartResource() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createCart(Cart cart)
	{

		int idOfInsertedRow = Globals.cartService.saveCart(cart);

		cart.setCartID(idOfInsertedRow);

		if(idOfInsertedRow >=1)
		{
			
			
			Response response = Response.status(Status.CREATED)
					.location(URI.create("/api/Cart/" + idOfInsertedRow))
					.entity(cart)
					.build();
			
			return response;
			
		}else if(idOfInsertedRow <=0)
		{
			Response response = Response.status(Status.NOT_MODIFIED)
					.entity(null)
					.build();
			
			//Response.status(Status.CREATED).location(arg0)
			
			return response;
		}
		
		return null;
		
	}

	
	@PUT
	@Path("/{CartID}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateCart(@PathParam("CartID")int cartID, Cart cart)
	{

		cart.setCartID(cartID);

		int rowCount = Globals.cartService.updateCart(cart);


		if(rowCount >= 1)
		{
			Response response = Response.status(Status.OK)
					.entity(null)
					.build();
			
			return response;
		}
		if(rowCount == 0)
		{
			Response response = Response.status(Status.NOT_MODIFIED)
					.entity(null)
					.build();
			
			return response;
		}


		return null;
	}


	@DELETE
	@Path("/{CartID}")
	public Response deleteCart(@PathParam("CartID")int cartID)
	{

		int rowCount = Globals.cartService.deleteCart(cartID);
		
		
		if(rowCount>=1)
		{
			Response response = Response.status(Status.OK)
					.entity(null)
					.build();
			
			return response;
		}
		
		if(rowCount == 0)
		{
			Response response = Response.status(Status.NOT_MODIFIED)
					.entity(null)
					.build();
			
			return response;
		}
		
		
		return null;
	}
	
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCarts(@QueryParam("EndUserID")int endUserID,
							 @QueryParam("ShopID")int shopID)

	{

		List<Cart> cartList = Globals.cartService.readCarts(endUserID,shopID);

		GenericEntity<List<Cart>> listEntity = new GenericEntity<List<Cart>>(cartList){
		};
	
		
		if(cartList.size()<=0)
		{
			Response response = Response.status(Status.NO_CONTENT)
					.entity(listEntity)
					.build();
			
			return response;
			
		}else
		{
			Response response = Response.status(Status.OK)
					.entity(listEntity)
					.build();
			
			return response;
		}
		
	}
	
	
	@GET
	@Path("/{CartID}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCart(@PathParam("CartID")int cartID)
	{

		Cart cart = Globals.cartService.readCart(cartID);
		
		if(cart != null)
		{
			Response response = Response.status(Status.OK)
			.entity(cart)
			.build();
			
			return response;
			
		} else 
		{
			
			Response response = Response.status(Status.NO_CONTENT)
					.entity(cart)
					.build();
			
			return response;
			
		}
		
	}	
}
