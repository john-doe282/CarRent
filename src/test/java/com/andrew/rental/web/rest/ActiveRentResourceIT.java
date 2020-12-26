package com.andrew.rental.web.rest;

import com.andrew.rental.RentalApp;
import com.andrew.rental.domain.ActiveRent;
import com.andrew.rental.domain.User;
import com.andrew.rental.domain.Car;
import com.andrew.rental.repository.ActiveRentRepository;
import com.andrew.rental.service.ActiveRentService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ActiveRentResource} REST controller.
 */
@SpringBootTest(classes = RentalApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class ActiveRentResourceIT {

    private static final Duration DEFAULT_DURATION = Duration.ofHours(6);
    private static final Duration UPDATED_DURATION = Duration.ofHours(12);

    @Autowired
    private ActiveRentRepository activeRentRepository;

    @Autowired
    private ActiveRentService activeRentService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restActiveRentMockMvc;

    private ActiveRent activeRent;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ActiveRent createEntity(EntityManager em) {
        ActiveRent activeRent = new ActiveRent()
            .duration(DEFAULT_DURATION);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        activeRent.setClient(user);
        // Add required entity
        Car car;
        if (TestUtil.findAll(em, Car.class).isEmpty()) {
            car = CarResourceIT.createEntity(em);
            em.persist(car);
            em.flush();
        } else {
            car = TestUtil.findAll(em, Car.class).get(0);
        }
        activeRent.setCar(car);
        return activeRent;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ActiveRent createUpdatedEntity(EntityManager em) {
        ActiveRent activeRent = new ActiveRent()
            .duration(UPDATED_DURATION);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        activeRent.setClient(user);
        // Add required entity
        Car car;
        if (TestUtil.findAll(em, Car.class).isEmpty()) {
            car = CarResourceIT.createUpdatedEntity(em);
            em.persist(car);
            em.flush();
        } else {
            car = TestUtil.findAll(em, Car.class).get(0);
        }
        activeRent.setCar(car);
        return activeRent;
    }

    @BeforeEach
    public void initTest() {
        activeRent = createEntity(em);
    }

    @Test
    @Transactional
    public void createActiveRent() throws Exception {
        int databaseSizeBeforeCreate = activeRentRepository.findAll().size();
        // Create the ActiveRent
        restActiveRentMockMvc.perform(post("/api/active-rents").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(activeRent)))
            .andExpect(status().isCreated());

        // Validate the ActiveRent in the database
        List<ActiveRent> activeRentList = activeRentRepository.findAll();
        assertThat(activeRentList).hasSize(databaseSizeBeforeCreate + 1);
        ActiveRent testActiveRent = activeRentList.get(activeRentList.size() - 1);
        assertThat(testActiveRent.getDuration()).isEqualTo(DEFAULT_DURATION);
    }

    @Test
    @Transactional
    public void createActiveRentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = activeRentRepository.findAll().size();

        // Create the ActiveRent with an existing ID
        activeRent.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restActiveRentMockMvc.perform(post("/api/active-rents").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(activeRent)))
            .andExpect(status().isBadRequest());

        // Validate the ActiveRent in the database
        List<ActiveRent> activeRentList = activeRentRepository.findAll();
        assertThat(activeRentList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkDurationIsRequired() throws Exception {
        int databaseSizeBeforeTest = activeRentRepository.findAll().size();
        // set the field null
        activeRent.setDuration(null);

        // Create the ActiveRent, which fails.


        restActiveRentMockMvc.perform(post("/api/active-rents").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(activeRent)))
            .andExpect(status().isBadRequest());

        List<ActiveRent> activeRentList = activeRentRepository.findAll();
        assertThat(activeRentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllActiveRents() throws Exception {
        // Initialize the database
        activeRentRepository.saveAndFlush(activeRent);

        // Get all the activeRentList
        restActiveRentMockMvc.perform(get("/api/active-rents?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(activeRent.getId().intValue())))
            .andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION.toString())));
    }
    
    @Test
    @Transactional
    public void getActiveRent() throws Exception {
        // Initialize the database
        activeRentRepository.saveAndFlush(activeRent);

        // Get the activeRent
        restActiveRentMockMvc.perform(get("/api/active-rents/{id}", activeRent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(activeRent.getId().intValue()))
            .andExpect(jsonPath("$.duration").value(DEFAULT_DURATION.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingActiveRent() throws Exception {
        // Get the activeRent
        restActiveRentMockMvc.perform(get("/api/active-rents/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateActiveRent() throws Exception {
        // Initialize the database
        activeRentService.save(activeRent);

        int databaseSizeBeforeUpdate = activeRentRepository.findAll().size();

        // Update the activeRent
        ActiveRent updatedActiveRent = activeRentRepository.findById(activeRent.getId()).get();
        // Disconnect from session so that the updates on updatedActiveRent are not directly saved in db
        em.detach(updatedActiveRent);
        updatedActiveRent
            .duration(UPDATED_DURATION);

        restActiveRentMockMvc.perform(put("/api/active-rents").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedActiveRent)))
            .andExpect(status().isOk());

        // Validate the ActiveRent in the database
        List<ActiveRent> activeRentList = activeRentRepository.findAll();
        assertThat(activeRentList).hasSize(databaseSizeBeforeUpdate);
        ActiveRent testActiveRent = activeRentList.get(activeRentList.size() - 1);
        assertThat(testActiveRent.getDuration()).isEqualTo(UPDATED_DURATION);
    }

    @Test
    @Transactional
    public void updateNonExistingActiveRent() throws Exception {
        int databaseSizeBeforeUpdate = activeRentRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restActiveRentMockMvc.perform(put("/api/active-rents").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(activeRent)))
            .andExpect(status().isBadRequest());

        // Validate the ActiveRent in the database
        List<ActiveRent> activeRentList = activeRentRepository.findAll();
        assertThat(activeRentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteActiveRent() throws Exception {
        // Initialize the database
        activeRentService.save(activeRent);

        int databaseSizeBeforeDelete = activeRentRepository.findAll().size();

        // Delete the activeRent
        restActiveRentMockMvc.perform(delete("/api/active-rents/{id}", activeRent.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ActiveRent> activeRentList = activeRentRepository.findAll();
        assertThat(activeRentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
