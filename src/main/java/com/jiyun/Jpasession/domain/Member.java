package com.jiyun.Jpasession.domain;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@NoArgsConstructor //해당 클래스에 기본 생성자를 만들줌 -> @Entity가 그 기능을 하지만 붙이는 이유는 기본생성자를 추가하되, 접근제한을 걸어서 안전성도 높이는 것이다.
@Table(name = "member")
@Entity
public class Member {
    @Id
    @Column(name = "ID") //PK가 된다.
    private String id;

    @Column(name = "NAME")
    private String username;

    @Enumerated(EnumType.STRING) // 회원은 일반 회원과 관리자로 구분해야 하므로 자바의 enum사용
    private RoleType roleType;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedDate;

    @Lob //길이 제한이 없기 때문에
    private String description;



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
