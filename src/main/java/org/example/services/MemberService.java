package org.example.services;

import org.example.entity.Member;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;
//nová DAO třída pro každou entitu
//zde se bude předávat EM z controlleru v každé metodě
//budou tu transakce


//member service by mělo komunikovat s DAO specifické tabulky
//state management - loadin atd pro ui
public class MemberService {

    private EntityManagerFactory emf;
    //zde volat dao metody a taky transakce
    // Konstruktor pro vytvoření EntityManagerFactory
    public MemberService() {
        this.emf = Persistence.createEntityManagerFactory("punit");
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    // Uložení člena
    public void saveMember(String firstName, String lastName, String email) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Member member = new Member(firstName, lastName, email);
            member.setFirstName(firstName);
            member.setLastName(lastName);
            member.setEmail(email);
            em.persist(member);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    // Aktualizace člena
    public void updateMember(Long id, String firstName, String lastName, String email) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Member member = em.find(Member.class, id);
            if (member != null) {
                member.setFirstName(firstName);
                member.setLastName(lastName);
                member.setEmail(email);
                em.merge(member);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    // Mazání člena
    public void deleteMember(Long id) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Member member = em.find(Member.class, id);
            if (member != null) {
                em.remove(member);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    // Načtení všech členů
    public List<Member> getAllMembers() {
        EntityManager em = getEntityManager();
        List<Member> members = null;
        try {
            em.getTransaction().begin();
            members = em.createQuery("SELECT m FROM Member m", Member.class).getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
        return members;
    }
}
