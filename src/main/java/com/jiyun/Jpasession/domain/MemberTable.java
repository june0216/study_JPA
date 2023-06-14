package com.jiyun.Jpasession.domain;

import javax.persistence.*;

@Entity
@TableGenerator(
		name = "MEMBER_SEQ_GENERATOR",  // 제너레이터 명
		table = "MY_SEQUENCES",  // 테이블 명
		pkColumnValue = "MEMBER_SEQ", allocationSize = 1) // pk 컬럼 명
public class MemberTable {
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, // 매핑 전략 설정
			generator = "MEMBER_SEQ_GENERATOR")  // 제너레이터 설정
	private Long id;
}