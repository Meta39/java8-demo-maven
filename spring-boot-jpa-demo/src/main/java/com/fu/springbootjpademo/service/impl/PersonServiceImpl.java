package com.fu.springbootjpademo.service.impl;

import com.fu.springbootjpademo.entity.Person;
import com.fu.springbootjpademo.repositories.PersonRepository;
import com.fu.springbootjpademo.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonServiceImpl implements PersonService {
    @Autowired
    private PersonRepository personRepository;

    @Override
    public void save(Person person) {
        this.personRepository.save(person);
    }

    @Override
    public Person findById(Long id) {
        //orElse()。有数据则返回相应的对象数据，否则返回null。个人感觉有点多此一举了。没太大的必要用Optional
        return this.personRepository.findById(id).orElse(null);
    }

    @Override
    public Page<Person> findAllByNameLike(String name, int page, int size) {
        this.personRepository.findAll(PageRequest.of(0,10,Sort.by(Sort.Direction.DESC,"id")));
        //拼接%并不会SQL注入
        return this.personRepository.findAllByNameLike("%" + name + "%", PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id")));
    }

    @Override
    public void deleteById(Long id) {
        this.personRepository.deleteById(id);
    }

    @Override
    public void deleteAllById(List<Long> ids) {
        this.personRepository.deleteAllById(ids);
    }
}
