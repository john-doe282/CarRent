package com.andrew.rental.web.rest;

import com.andrew.rental.domain.ActiveRent;
import com.andrew.rental.domain.Car;
import com.andrew.rental.domain.User;
import com.andrew.rental.service.ActiveRentService;
import com.andrew.rental.service.UserService;
import com.andrew.rental.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import static com.andrew.rental.security.SecurityUtils.getCurrentUserLogin;

/**
 * REST controller for managing {@link com.andrew.rental.domain.ActiveRent}.
 */
@RestController
@RequestMapping("/api")
public class ActiveRentResource {

    private final Logger log = LoggerFactory.getLogger(ActiveRentResource.class);

    private static final String ENTITY_NAME = "activeRent";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ActiveRentService activeRentService;

    private final UserService userService;

    public ActiveRentResource(ActiveRentService activeRentService,
                              UserService userService) {
        this.activeRentService = activeRentService;
        this.userService = userService;
    }

    /**
     * {@code POST  /active-rents} : Create a new activeRent.
     *
     * @param activeRent the activeRent to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new activeRent, or with status {@code 400 (Bad Request)} if the activeRent has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/active-rents")
    public ResponseEntity<ActiveRent> createActiveRent(@Valid @RequestBody ActiveRent activeRent) throws URISyntaxException {
        log.debug("REST request to save ActiveRent : {}", activeRent);
        if (activeRent.getId() != null) {
            throw new BadRequestAlertException("A new activeRent cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Optional<User> user = userService.getUserWithAuthorities();
        if (!user.isPresent()) {
            throw new BadRequestAlertException("Log in first", ENTITY_NAME, "forbidden");
        }

        activeRent.setClient(user.get());
        ActiveRent result = activeRentService.save(activeRent);
        return ResponseEntity.created(new URI("/api/active-rents/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /active-rents} : Updates an existing activeRent.
     *
     * @param activeRent the activeRent to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated activeRent,
     * or with status {@code 400 (Bad Request)} if the activeRent is not valid,
     * or with status {@code 500 (Internal Server Error)} if the activeRent couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/active-rents")
    public ResponseEntity<ActiveRent> updateActiveRent(@Valid @RequestBody ActiveRent activeRent) throws URISyntaxException {
        log.debug("REST request to update ActiveRent : {}", activeRent);
        if (activeRent.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Optional<User> user = userService.getUserWithAuthorities();
        if (!user.isPresent()) {
            throw new BadRequestAlertException("Log in first", ENTITY_NAME, "forbidden");
        }

        activeRent.setClient(user.get());
        ActiveRent result = activeRentService.save(activeRent);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, activeRent.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /active-rents} : get all the activeRents.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of activeRents in body.
     */
    @GetMapping("/active-rents")
    public ResponseEntity<List<ActiveRent>> getAllActiveRents(Pageable pageable) {
        log.debug("REST request to get a page of ActiveRents");
        Optional<User> user = userService.getUserWithAuthorities();
        if (!user.isPresent()) {
            throw new BadRequestAlertException("Log in first", ENTITY_NAME, "forbidden");
        }
        Page<ActiveRent> page = activeRentService.findAllByUser(pageable, user.get());
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /active-rents/:id} : get the "id" activeRent.
     *
     * @param id the id of the activeRent to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the activeRent, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/active-rents/{id}")
    public ResponseEntity<ActiveRent> getActiveRent(@PathVariable Long id) {
        log.debug("REST request to get ActiveRent : {}", id);
        Optional<ActiveRent> activeRent = activeRentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(activeRent);
    }

    /**
     * {@code DELETE  /active-rents/:id} : delete the "id" activeRent.
     *
     * @param id the id of the activeRent to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/active-rents/{id}")
    public ResponseEntity<Void> deleteActiveRent(@PathVariable Long id) {
        log.debug("REST request to delete ActiveRent : {}", id);
        Optional<User> user = userService.getUserWithAuthorities();
        if (!user.isPresent()) {
            throw new BadRequestAlertException("Log in", ENTITY_NAME, "forbidden");
        }

        ActiveRent rent = activeRentService.findOne(id).get();
        if (!user.get().getLogin().
            equalsIgnoreCase(rent.getClient().getLogin())) {
            throw new BadRequestAlertException("Forbidden", ENTITY_NAME, "forbidden");
        }

        activeRentService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
