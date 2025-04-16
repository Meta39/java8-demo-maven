package com.fu.springbootjpademo.service;

import com.fu.springbootjpademo.entity.Person;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PersonService {
    void save(Person person);

    Person findById(Long id);

    Page<Person> findAllByNameLike(String name, int page, int size);

    void deleteById(Long id);

    void deleteAllById(List<Long> ids);

}
