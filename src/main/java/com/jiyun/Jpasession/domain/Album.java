package com.jiyun.Jpasession.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;


import org.springframework.context.annotation.Primary;

@Entity
@DiscriminatorValue("ALBUM") //엔티티 구분용
@PrimaryKeyJoinColumn(name = "BOOK_ID") // 이것이 없으면 부모 테이블의 ID 컬럼명을 그대로 사용하여 ITEM_ID인데 이를 바꿀 수 있음
public class Album extends Item {


	private String artist;
}
