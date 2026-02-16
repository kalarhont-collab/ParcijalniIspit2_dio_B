package hr.java.hibernate.jpa.demo.introduction.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Upis")
public class Upis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UpisID")
    private int upisID;

    @ManyToOne
    @JoinColumn(name = "IDPolaznik", nullable = false)
    private Polaznik polaznik;

    @ManyToOne
    @JoinColumn(name ="IDProgramObrazovanja", nullable = false)
    private ProgramObrazovanja programObrazovanja;

    public Upis() {
    }

    public int getUpisID() {
        return upisID;
    }

    public void setUpisID(int upisID) {
        this.upisID = upisID;
    }

    public Polaznik getPolaznik() {
        return polaznik;
    }

    public void setPolaznik(Polaznik polaznik) {
        this.polaznik = polaznik;
    }

    public ProgramObrazovanja getProgramObrazovanja() {
        return programObrazovanja;
    }

    public void setProgramObrazovanja(ProgramObrazovanja programObrazovanja) {
        this.programObrazovanja = programObrazovanja;
    }
}
