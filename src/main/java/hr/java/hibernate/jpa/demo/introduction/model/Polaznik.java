package hr.java.hibernate.jpa.demo.introduction.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Polaznik")
public class Polaznik {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int PolaznikID;


    @Column(nullable = false, length = 100)
    private String ime;

    @Column(nullable = false, length = 100)
    private String prezime;

    @OneToMany(mappedBy = "polaznik")
    private Set<Upis> upisi = new HashSet<>();

    public Polaznik() {
    }

    public int getPolaznikID() {
        return PolaznikID;
    }

    public void setPolaznikID(int polaznikID) {
        PolaznikID = polaznikID;
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
