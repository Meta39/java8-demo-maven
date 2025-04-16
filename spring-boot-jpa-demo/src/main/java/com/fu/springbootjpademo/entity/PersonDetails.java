package com.fu.springbootjpademo.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.Table;
import org.hibernate.proxy.HibernateProxy;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

/**
 * 创建日期：2024-06-28
 */
@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(appliesTo = "person_details", comment = "人员详情表")//appliesTo指表的名称（必填），MySQL表名小写，用Person会抛异常。
public class PersonDetails {
    @Id//主键标识
    @GeneratedValue(strategy = GenerationType.IDENTITY)//数据库主键策略，默认是AUTO。
    @Comment("主键ID")
    private Long id;

    @Column(name = "address")
    @Comment("地址")
    private String address;

    @Column(name = "phone")
    @Comment("电话")
    private String phone;

    @Column(name = "birthday")
    @Comment("生日")
    private Date birthday;

    @OneToOne
    @JoinColumn(name = "person_id", nullable = false)//设置和Person关联的外键和外键列名称、外键非空
    private Person person;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        PersonDetails that = (PersonDetails) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
