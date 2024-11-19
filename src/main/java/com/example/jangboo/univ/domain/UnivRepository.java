package com.example.jangboo.univ.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UnivRepository extends JpaRepository<Univ, Long> {

    List<Univ> findByParentId(Long parentId);
}
