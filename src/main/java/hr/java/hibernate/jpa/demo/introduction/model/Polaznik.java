package hr.java.hibernate.jpa.demo.introduction.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Polaznik")
public class Polaznik {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PolaznikID")
    private int polaznikID;


    @Column(name = "Ime", nullable = false, length = 100)
    private String ime;

    @Column(name = "Prezime", nullable = false, length = 100)
    private String prezime;

    @OneToMany(mappedBy = "polaznik", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Upis> upisi = new HashSet<>();

    public Polaznik() {
    }

    public int getPolaznikID() {
        return polaznikID;
    }

    public void setPolaznikID(int polaznikID) {
        this.polaznikID = polaznikID;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public Set<Upis> getUpisi() {
        return upisi;
    }

    public void setUpisi(Set<Upis> upisi) {
        this.upisi = upisi;
    }
}
