package org.example.dao;

import org.example.entity.Member;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

public class MemberDao implements Dao<Member>{
    @Override
    public Optional<Member> get(EntityManager em, long id) {
        return Optional.empty();
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
