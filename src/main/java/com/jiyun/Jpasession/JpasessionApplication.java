package com.jiyun.Jpasession;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.lang.reflect.Member;
import java.util.List;


@SpringBootApplication
public class JpasessionApplication {

	public static void main(String[] args) {
/*		EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");//엔티티 메니저 팩토리 생성
		EntityManager em = emf.createEntityManager(); //엔티티 매니저 생성
		EntityTransaction tx = em.getTransaction(); //트랜잭션 획득

		try{
			tx.begin();//트랜잭션 시작
			logic(em);//비즈니스 로직 시작
			tx.commit(); //트랜잭션 커밋

		}catch (Exception e){
			tx.rollback(); //트랜잭션 롤백
		} finally {
			em.close(); //엔티티 매니저 종료
		}
		emf.close(); //엔티티 매니저 팩토리 종료

		private static void logic(EntityManager em){
			String id = "id1";
			Member member = new Member();
			member.setId(id);
			member.seUsername("지한");
			member.setAge(2);


			//등록
			em.persis(member);

			//수정
			em.setAge(29);

			//한 건조회
			Member findMember = em.find(Member.class, id);

			//목록 조회
			List<Member> members = em.createQuery("select m from Member m", Member.class).getResultList();


		}*/
		SpringApplication.run(JpasessionApplication.class, args);
	}

}
