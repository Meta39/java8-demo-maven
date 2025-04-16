package com.fu.springbootjpademo.controller;

import com.fu.springbootjpademo.entity.Person;
import com.fu.springbootjpademo.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("person")
public class PersonController {
    @Autowired
    private PersonService personService;

    /**
     * 新增/更新
     */
    @PostMapping("save")
    public void save(@RequestBody Person person) {
        this.personService.save(person);
    }

    /**
     * 根据ID查询
     * @param id ID
     */
    @PostMapping("findById/{id}")
    public Person findById(@PathVariable Long id) {
        return this.personService.findById(id);
    }

    /**
     * 分页查询
     * @param name 名称
     * @param page 起始页
     * @param size 每页数量
     */
    @PostMapping("page")
    public Page<Person> page(@RequestParam(required = false) String name,
                             @RequestParam(required = false, defaultValue = "0") int page,
                             @RequestParam(required = false, defaultValue = "10") int size) {
        return this.personService.findAllByNameLike(name, page, size);
    }

    /**
     * 根据id删除记录
     * @param id ID
     */
    @PostMapping("deleteById/{id}")
    public void deleteById(@PathVariable Long id) {
        this.personService.deleteById(id);
    }

    /**
     * 根据id list批量删除
     * @param ids ids
     */
    @PostMapping("deleteAllById")
    public void deleteAllById(@RequestBody List<Long> ids) {
        this.personService.deleteAllById(ids);
    }

}
