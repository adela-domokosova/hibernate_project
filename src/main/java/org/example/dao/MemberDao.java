package org.example.dao;

import org.example.entity.Member;
import org.example.entity.Subscription;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

/**
 * DAO třída pro práci s členskou entitou
 * Implementuje základní operace pro přístup k datům členů
 */
public class MemberDao implements Dao<Member>{
    @Override
    public Optional<Member> get(EntityManager em, long id) {
        Member member = em.find(Member.class, id);
        return Optional.ofNullable(member);
    }

    @Override
    public List<Member> getAll() {
        return List.of();
    }

    @Override
    public List<Member> getAll(EntityManager em) {
        return em.createQuery("SELECT m FROM Member m", Member.class).getResultList();
    }

    @Override
    public void save(Member member) {

    }

    @Override
    public void update(Member member, String[] params) {

    }

    @Override
    public void delete(Member member) {

    }
}
