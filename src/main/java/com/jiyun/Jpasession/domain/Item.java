package com.jiyun.Jpasession.domain;


import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;



@Entity
@Inheritance(strategy = InheritanceType.JOINED) // 상속 매핑은 부모틀래스에 해당 어노테이션을 사용해야 한다. (매핑 전략은 조인)
@DiscriminatorColumn //부모 클래스에 구분 컬럼을 지정한다. (자식 테이블 구분)
public class Item {

	@Id
	@GeneratedValue
	@Column(name = "ITEM_ID")
	private Long id;

	private String name;
	private int price;
}
