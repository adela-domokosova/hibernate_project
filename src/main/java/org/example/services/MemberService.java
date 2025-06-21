package org.example.services;

import org.example.controller.HomeController;
import org.example.dao.MemberDao;
import org.example.entity.Member;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class MemberService {
    private static final Logger LOG = LoggerFactory.getLogger(MemberService.class);


    private MemberDao memberDao;
    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }
    public void saveMember(EntityManager em, String firstName, String lastName, String email, LocalDateTime registrationDate) {
        try {
            em.getTransaction().begin();
            Member member = new Member(firstName, lastName, email, registrationDate);
            member.setLastName(lastName);
            member.setEmail(email);
            em.persist(member);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        }
    }

    public void updateMember(EntityManager em, Long id, String firstName, String lastName, String email) {
        try {
            em.getTransaction().begin();
            Optional<Member> optionalMember = memberDao.get(em, id);
            if (optionalMember.isPresent()) {
                Member member = optionalMember.get();
                member.setFirstName(firstName);
                member.setLastName(lastName);
                member.setEmail(email);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        }
    }

    public void deleteMember(EntityManager em, Long memberId) {
        if (!em.getTransaction().isActive()) {
            em.getTransaction().begin();
        }
        try {
            Member memberToDelete = em.find(Member.class, memberId);

            if (memberToDelete != null) {
                em.remove(memberToDelete);
                em.getTransaction().commit();
                LOG.info("Member with ID {} deleted successfully.", memberId);
            } else {
                LOG.warn("Attempted to delete non-existent member with ID: {}", memberId);
                em.getTransaction().rollback();
            }
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            LOG.error("Error deleting member with ID {}: {}", memberId, e.getMessage());
            throw e;
        }
    }

    public List<Member> getAllMembers(EntityManager em) {
        List<Member> members = null;
        try {
            em.getTransaction().begin();
            members = memberDao.getAll(em);
            System.out.println("from dao " + members);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        }
        return members;
    }

    public Optional<Member> getMemberById(EntityManager em, Long id) {
        Optional<Member> member = null;
        try {
            em.getTransaction().begin();
            member = memberDao.get(em, id);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        }
        return member;
    }
}
