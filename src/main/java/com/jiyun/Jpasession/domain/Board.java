package com.jiyun.Jpasession.domain;

import lombok.Getter;

import javax.persistence.*;

@Entity
@SequenceGenerator(name = "BOARD_SEQ_GENERATOR", sequenceName = "BOARD_SEQ", initialValue = 1, allocationSize = 1)
//hibernate: create sequence BOARD_SEQ_GENERATOR start with 1 increment by 1
@Getter
public class Board {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BOARD_SEQ_GENERATOR") //원하는 시퀀스 제너레이터를 매핑 걸rl
	private Long id;

	private static void logic(EntityManager em){
		Board board  = new Board();
		em.persist(board);
		System.out.println("board.id = "  + board.getId());
	}
}
