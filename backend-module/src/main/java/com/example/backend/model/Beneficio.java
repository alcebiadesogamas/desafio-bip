package com.example.backend.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "beneficio")
public class Beneficio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome")
    private String name;

    @Column(name = "descricao")
    private String description;

    @Column(name = "valor")
    private BigDecimal value;

    @Column(name = "ativo")
    private Boolean active;

    @Column(name = "version")
    private Long version;

    public Beneficio() {
    }

    public Beneficio(Long id, String nome, String descricao, BigDecimal valor, Boolean ativo, Long version) {
        this.id = id;
        this.name = nome;
        this.description = descricao;
        this.value = valor;
        this.active = ativo;
        this.version = version;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "Beneficio{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", description='" + description + '\'' +
               ", value=" + value +
               ", active=" + active +
               ", version=" + version +
               '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Beneficio beneficio = (Beneficio) o;
        return Objects.equals(id, beneficio.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
