package com.fu.springbootjpademo.repositories;

import com.fu.springbootjpademo.entity.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 写法参考<a href="https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.query-creation"/>
 */
@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    List<Person> findByName(String name);

    List<Person> findByNameLike(String name);

    Page<Person> findAllByNameLike(String name, Pageable pageable);

}
