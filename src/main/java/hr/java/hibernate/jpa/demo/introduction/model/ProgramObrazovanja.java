package hr.java.hibernate.jpa.demo.introduction.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "ProgramObrazovanja")
public class ProgramObrazovanja {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ProgramObrazovanjaID;

    @Column(nullable = false, length = 100)
    private String Naziv;

    @Column(nullable = false)
    private int CSVET;

    @OneToMany
    private Set<Upis> upisi = new HashSet<>();

    public ProgramObrazovanja() {
    }

    public int getProgramObrazovanjaID() {
        return ProgramObrazovanjaID;
    }

    public void setProgramObrazovanjaID(int programObrazovanjaID) {
        ProgramObrazovanjaID = programObrazovanjaID;
    }

    public String getNaziv() {
        return Naziv;
    }

    public void setNaziv(String naziv) {
        Naziv = naziv;
    }

    public int getCSVET() {
        return CSVET;
    }

    public void setCSVET(int CSVET) {
        this.CSVET = CSVET;
    }

    public Set<Upis> getUpisi() {
        return upisi;
    }

    public void setUpisi(Set<Upis> upisi) {
        this.upisi = upisi;
    }
}
