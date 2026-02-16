package hr.java.hibernate.jpa.demo.introduction;

import hr.java.hibernate.jpa.demo.introduction.model.Polaznik;
import hr.java.hibernate.jpa.demo.introduction.model.ProgramObrazovanja;
import hr.java.hibernate.jpa.demo.introduction.model.Upis;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Main {

    public static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) throws SQLException {

        System.out.println("=== Evidencija polaznika ==");
        System.out.println("Odaberite ponuđene opcije");

        int odabir;
        do {
            System.out.println("=== === === === ===");
            System.out.println("1. Unos novog polaznika");
            System.out.println("2. Unos novog programa obrazovanja");
            System.out.println("3. Upis polaznika na program obrazovanja");
            System.out.println("4. Prebacivanje polaznika na drugi program obrazovanja");
            System.out.println("5. Ispis polaznika po programu obrazovanja");
            System.out.println("6. Izlaz iz programa");
            System.out.println("=== === === === ===");
            System.out.print("Odabir: ");

            odabir = sc.nextInt();
            sc.nextLine();

            switch (odabir) {

                case 1 -> unesiNovogPolaznika();
                case 2 -> unesiNoviProgramObrazovanja();
                case 3 -> upisiPolaznikaNaProgram();
                case 4 -> prebaciPolaznika();
                case 5 -> ispisiPolaznikePoProgramu();
                case 6 -> System.out.println("Doviđenja!");
                default -> System.out.println("Nepravilan odabir!");

            }

        } while (odabir != 6);

        HibernateUtil.getSessionFactory().close();

    }


    private static void unesiNovogPolaznika() {
        System.out.print("Ime: ");
        String ime = sc.nextLine();

        System.out.print("Prezime: ");
        String prezime = sc.nextLine();

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        Polaznik p = new Polaznik();
        p.setIme(ime);
        p.setPrezime(prezime);

        session.persist(p);
        tx.commit();
        session.close();

        System.out.println("Polaznik uspješno unesen.");
    }

    private static void unesiNoviProgramObrazovanja() {
        System.out.print("Naziv programa: ");
        String naziv = sc.nextLine();

        System.out.print("CSVET: ");
        int csvet = ucitajInt();

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        ProgramObrazovanja po = new ProgramObrazovanja();
        po.setNaziv(naziv);
        po.setCSVET(csvet);

        session.persist(po);
        tx.commit();
        session.close();

        System.out.println("Program obrazovanja uspješno unesen.");
    }

    private static void upisiPolaznikaNaProgram() {
        System.out.print("ID polaznika: ");
        int polaznikId = ucitajInt();

        System.out.print("ID programa obrazovanja: ");
        int programId = ucitajInt();

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        Polaznik p = session.get(Polaznik.class, polaznikId);
        ProgramObrazovanja po = session.get(ProgramObrazovanja.class, programId);

        if (p == null || po == null) {
            System.out.println("Neispravan ID polaznika ili programa.");
            tx.rollback();
        } else {
            Upis upis = new Upis();
            upis.setPolaznik(p);
            upis.setProgramObrazovanja(po);

            session.persist(upis);
            tx.commit();
            System.out.println("Polaznik uspješno upisan.");
        }

        session.close();
    }

    private static void prebaciPolaznika() {
        System.out.print("ID upisa: ");
        int upisId = ucitajInt();

        System.out.print("ID novog programa obrazovanja: ");
        int noviProgramId = ucitajInt();

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        try {
            Upis upis = session.get(Upis.class, upisId);
            ProgramObrazovanja noviProgram =
                    session.get(ProgramObrazovanja.class, noviProgramId);

            if (upis == null || noviProgram == null) {
                System.out.println("Neispravan ID upisa ili programa.");
                tx.rollback();
            } else {
                upis.setProgramObrazovanja(noviProgram);
                tx.commit();
                System.out.println("Polaznik uspješno prebačen.");
            }

        } catch (Exception e) {
            tx.rollback();
            System.out.println("Greška pri prebacivanju polaznika.");
        } finally {
            session.close();
        }
    }

    private static void ispisiPolaznikePoProgramu() {
        System.out.print("ID programa obrazovanja: ");
        int programId = ucitajInt();

        Session session = HibernateUtil.getSessionFactory().openSession();

        String hql = "FROM Upis u WHERE u.programObrazovanja.programObrazovanjaID = :id";

        Set<Upis> upisi = new HashSet<>(session.createQuery(hql, Upis.class)
                .setParameter("id", programId)
                .getResultList());

        if (upisi.isEmpty()) {
            System.out.println("Nema polaznika za odabrani program.");
            return;
        }

        System.out.println("\n--- Popis polaznika ---");
        for (Upis u : upisi) {
            Polaznik p = u.getPolaznik();
            ProgramObrazovanja po = u.getProgramObrazovanja();
            System.out.printf("%s %s | %s | CSVET: %d%n",
                    p.getIme(), p.getPrezime(), po.getNaziv(), po.getCSVET());
        }

        session.close();
    }


    private static int ucitajInt() {
        while (true) {
            try {
                int broj = Integer.parseInt(sc.nextLine());
                return broj;
            } catch (NumberFormatException e) {
                System.out.print("Neispravan unos, unesite cijeli broj od 1 do 6: ");
            }
        }
    }
}