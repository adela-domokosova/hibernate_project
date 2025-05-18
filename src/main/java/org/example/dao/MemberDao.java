package org.example.dao;

import org.example.entity.Member;

import java.util.List;
import java.util.Optional;

public class MemberDao implements Dao<Member>{
    @Override
    public Optional<Member> get(long id) {
        return Optional.empty();
    }

    @Override
    public List<Member> getAll() {
        return List.of();
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
