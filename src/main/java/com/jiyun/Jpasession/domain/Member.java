package com.jiyun.Jpasession.domain;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@NoArgsConstructor //해당 클래스에 기본 생성자를 만들줌 -> @Entity가 그 기능을 하지만 붙이는 이유는 기본생성자를 추가하되, 접근제한을 걸어서 안전성도 높이는 것이다.
@Table(name = "member")
@Entity
public class Member {
    @Id
    @Column(name = "ID") //PK가 된다.
    private String id;

    @Column(name = "NAME")
    private String username;

    //매핑 정보가 없는 필드
    private Integer age;



    public void setId(String id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
