package hr.java.hibernate.jpa.demo.introduction;

import hr.java.hibernate.jpa.demo.introduction.model.Polaznik;
import hr.java.hibernate.jpa.demo.introduction.model.ProgramObrazovanja;
import hr.java.hibernate.jpa.demo.introduction.model.Upis;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

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

        Polaznik polaznik = new Polaznik();
        polaznik.setIme(ime);
        polaznik.setPrezime(prezime);

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(polaznik);
            tx.commit();
            System.out.println("Polaznik uspješno unesen!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void unesiNoviProgramObrazovanja() {
        System.out.print("Naziv programa: ");
        String naziv = sc.nextLine();
        System.out.print("CSVET bodovi: ");
        int csvet = ucitajInt();

        ProgramObrazovanja program = new ProgramObrazovanja();
        program.setNaziv(naziv);
        program.setCSVET(csvet);

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(program);
            tx.commit();
            System.out.println("Program obrazovanja uspješno unesen!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void upisiPolaznikaNaProgram() {
        System.out.print("ID Polaznika: ");
        int polaznikID = ucitajInt();
        System.out.print("ID Programa obrazovanja: ");
        int programID = ucitajInt();

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Polaznik p = session.get(Polaznik.class, polaznikID);
            ProgramObrazovanja po = session.get(ProgramObrazovanja.class, programID);

            if (p == null || po == null) {
                System.out.println("Polaznik ili program ne postoji!");
                return;
            }

            Upis upis = new Upis();
            upis.setPolaznik(p);
            upis.setProgramObrazovanja(po);

            Transaction tx = session.beginTransaction();
            session.persist(upis);
            tx.commit();
            System.out.println("Polaznik uspješno upisan na program!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void prebaciPolaznika() {
        System.out.print("ID Polaznika: ");
        int polaznikID = ucitajInt();
        System.out.print("ID trenutnog programa: ");
        int stariProgramID = ucitajInt();
        System.out.print("ID novog programa: ");
        int noviProgramID = ucitajInt();

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();

            String hql = "FROM Upis u WHERE u.polaznik.polaznikID = :pid AND u.programObrazovanja.programObrazovanjaID = :progid";
            Upis upis = session.createQuery(hql, Upis.class)
                    .setParameter("pid", polaznikID)
                    .setParameter("progid", stariProgramID)
                    .uniqueResult();

            ProgramObrazovanja noviProgram = session.get(ProgramObrazovanja.class, noviProgramID);

            if (upis == null || noviProgram == null) {
                System.out.println("Polaznik ili program ne postoji / upis ne postoji!");
                tx.rollback();
                return;
            }

            upis.setProgramObrazovanja(noviProgram);
            session.merge(upis);
            tx.commit();
            System.out.println("Polaznik uspješno prebačen na novi program!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void ispisiPolaznikePoProgramu() {
        System.out.print("ID programa obrazovanja: ");
        int programID = ucitajInt();

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            String hql = "FROM Upis u WHERE u.programObrazovanja.programObrazovanjaID = :id";

            List<Upis> upisi = session.createQuery(hql, Upis.class)
                    .setParameter("id", programID)
                    .getResultList();

            if (upisi.isEmpty()) {
                System.out.println("Nema polaznika za odabrani program.");
                return;
            }

            System.out.println("\n--- Popis polaznika ---");
            for (Upis u : upisi) {
                Polaznik p = u.getPolaznik();
                ProgramObrazovanja po = u.getProgramObrazovanja();
                System.out.printf("%s %s | %s | CSVET: %d%n",
                        p.getIme(),
                        p.getPrezime(),
                        po.getNaziv(),
                        po.getCSVET());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
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