package br.com.hioktec.infrastructure.resources.reactives;

import br.com.hioktec.application.dto.CustomerDTO;
import br.com.hioktec.application.dto.ProfilePhotoDTO;
import br.com.hioktec.application.services.reactives.ReactiveApplicationService;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.ResponseStatus;
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.multipart.FileUpload;

@Path("/reactive/customers")
public class ReactiveCustomerResource {

  private final ReactiveApplicationService service;

  public ReactiveCustomerResource(ReactiveApplicationService service) {
    this.service = service;
  }

  @GET
  public Multi<CustomerDTO> searchCustomers() {
    return service.searchCustomers();
  }

  @GET
  @Path("/{id}")
  public Uni<Response> getCustomer(@PathParam("id") String id) {
    return service.getCustomer(id)
      .onItem().transform(customer ->
        customer != null ? Response.ok(customer) : Response.status(Response.Status.NOT_FOUND))
      .onItem().transform(Response.ResponseBuilder::build);
  }

  @POST
  @Path("/{id}")
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  @ResponseStatus(RestResponse.StatusCode.ACCEPTED)
  public void persistProfilePhoto(@PathParam("id") String id, @RestForm("photo") FileUpload fileUpload) {
    service.persistProfilePhoto(id, ProfilePhotoDTO.create(fileUpload));
  }
}
