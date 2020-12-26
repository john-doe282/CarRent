package com.andrew.rental.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;

import com.andrew.rental.domain.enumeration.CarStatus;

/**
 * A Car.
 */
@Entity
@Table(name = "car")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Car implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CarStatus status;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "price_per_hour", precision = 21, scale = 2, nullable = false)
    private BigDecimal pricePerHour;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "cars", allowSetters = true)
    private Model model;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "cars", allowSetters = true)
    private Type type;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "cars", allowSetters = true)
    private Location location;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "cars", allowSetters = true)
    private User owner;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public Car description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CarStatus getStatus() {
        return status;
    }

    public Car status(CarStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(CarStatus status) {
        this.status = status;
    }

    public BigDecimal getPricePerHour() {
        return pricePerHour;
    }

    public Car pricePerHour(BigDecimal pricePerHour) {
        this.pricePerHour = pricePerHour;
        return this;
    }

    public void setPricePerHour(BigDecimal pricePerHour) {
        this.pricePerHour = pricePerHour;
    }

    public Model getModel() {
        return model;
    }

    public Car model(Model model) {
        this.model = model;
        return this;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public Type getType() {
        return type;
    }

    public Car type(Type type) {
        this.type = type;
        return this;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Location getLocation() {
        return location;
    }

    public Car location(Location location) {
        this.location = location;
        return this;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public User getOwner() {
        return owner;
    }

    public Car owner(User user) {
        this.owner = user;
        return this;
    }

    public void setOwner(User user) {
        this.owner = user;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Car)) {
            return false;
        }
        return id != null && id.equals(((Car) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Car{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", status='" + getStatus() + "'" +
            ", pricePerHour=" + getPricePerHour() +
            "}";
    }
}
