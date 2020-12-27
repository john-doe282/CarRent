package com.andrew.rental.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;

/**
 * A ActiveRent.
 */
@Entity
@Table(name = "active_rent")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ActiveRent implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "duration", nullable = false)
    private Duration duration;

    @Column(name = "created_at")
    private Instant createdAt;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "activeRents", allowSetters = true)
    private User client;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "activeRents", allowSetters = true)
    private Car car;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Duration getDuration() {
        return duration;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public ActiveRent duration(Duration duration) {
        this.duration = duration;
        return this;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public User getClient() {
        return client;
    }

    public ActiveRent client(User user) {
        this.client = user;
        return this;
    }

    public void setClient(User user) {
        this.client = user;
    }

    public Car getCar() {
        return car;
    }

    public ActiveRent car(Car car) {
        this.car = car;
        return this;
    }

    public void setCar(Car car) {
        this.car = car;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ActiveRent)) {
            return false;
        }
        return id != null && id.equals(((ActiveRent) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ActiveRent{" +
            "id=" + getId() +
            ", duration='" + getDuration() + "'" +
            "}";
    }
}
