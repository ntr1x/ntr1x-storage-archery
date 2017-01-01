package com.ntr1x.storage.archery.resources;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.ntr1x.storage.archery.model.Domain;
import com.ntr1x.storage.archery.services.IDomainService;
import com.ntr1x.storage.archery.services.IDomainService.DomainCreate;
import com.ntr1x.storage.archery.services.IDomainService.DomainPageResponse;
import com.ntr1x.storage.core.transport.PageableQuery;
import com.ntr1x.storage.security.model.ISession;

import io.swagger.annotations.Api;

@Api("Me")
@Component
@Path("/me/domains")
@PermitAll
public class DomainMe {

	@PersistenceContext
    private EntityManager em;

    @Inject
    private Provider<ISession> session;
    
    @Inject
    private IDomainService domains;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @RolesAllowed({ "auth" })
    public DomainPageResponse query(
    	@QueryParam("portal") Long portal,
		@BeanParam PageableQuery pageable
    ) {
    	
    	Page<Domain> p = domains.query(
			session.get().getUser().getId(),
			portal,
			pageable.toPageRequest()
		);
    	
        return new DomainPageResponse(
    		p.getTotalElements(),
    		p.getNumber(),
    		p.getSize(),
    		p.getContent()
		);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @RolesAllowed({ "auth" })
    public Domain create(DomainCreate create) {
    	
        return domains.create(create);
	}
}