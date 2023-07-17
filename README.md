# study_JPA
## 연관관계 매핑 시 고려사항

1. 다중성
2. 단방향 or 양방향
3. 연관관계의 주인

### 다중성

- 다대일: @ManyToOne
- 일대다: @OneToMany
- 일대일: @OneToOne
- 다대다: @ManyToMany
    
    JPA에서 나오는 애너테이션은 전부 DB와 매핑하기 위해서 존재한다. 
    그래서, DB 관점에서 다중성을 기준으로 고민하면 된다.
    
    한번씩 헷갈릴 때가 있는데, 그럴 땐 반대로 생각해보면 풀릴 때가 있다. 대칭성이 있기 때문에.
    
    **가장 중요한 것, 다대다는 실무에서 사용하지 않는다.** 
    

### 단방향, 양방향

- 테이블
    - 외래 키 하나로 양쪽 조인 가능
    - 사실 테이블에는 방향이라는 개념이 없음
- 객체
    - 객체는 참조용 필드가 있는 쪽으로만 참조 가능
    - 한쪽만 참조하면 단방향
    - 양쪽이 서로 참조하면 양방향
    - 사실 객체 입장에서 보면, 방향은 무조건 하나이다. 
    member에서 team에 대한 참조가 있고, team에서 member에 대한 참조가 있다.
    그냥 단방향이 2개인 것이고, 양방향처럼 보이는 것일 뿐이다.

### 연관관계의 주인

- 테이블은 **외래 키 하나**로 두 테이블이 연관관계를 맺는다.
- 객체 양방향 관게는 A→B, B→A 처럼 **참조가 2군데**이다.
- 객체 양방향 관계는 참조가 2군데 있다. 둘 중 테이블의 외래 키를 관리할 곳을 지정해야 한다.
- **연관관계의 주인 : 외래 키를 관리하는 참조**
- 주인의 반대편 : 외래 키에 영향을 주지 않음, 단순 조회만 가능

### 다대일 [N:1]

### 다대일 단방향

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/ecb3eaa1-6954-4b66-abae-738ca5534f39/Untitled.png)

- Member와 Team이 있으면, DB 입장에서는 Member가 N이고 Team이 1이다. 
따라서, Member에 FK가 가야 한다.
    - Member에 FK가 있으면, Member를 2개 넣어도 Team_ID에 값을 넣기 가능.
    Team에 FK가 있으면, Team을 여러개 insert 해야 하므로 설계가 잘못 나온다.
    - RDB에서는 항상 N 쪽에 FK 가 들어가야 설계가 맞다.
- 가장 많이 사용하는 연관관계
- 다대일의 반대는 일대다

```java
@ManyToOne 
@JoinColumn(name = "TEAM_ID")
private Team team;
```

### 다대일 양방향

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/adc544c6-2119-49aa-9694-b06cb86160f1/Untitled.png)

- 양방향을 하기 위해서는, 역방향 참조를 걸어주면 된다. 
이때, **테이블에 아무런 영향도 주지 않는다.**
- 연관관계의 주인인 TEAM_ID가 외래키를 관리하고 있고, 반대쪽은 읽기만 가능하다.
- 외래 키가 있는 쪽이 연관관계의 주인
- 양쪽을 서로 참조하도록 개발할 때 필요하다.

```java
@OneToMany(mappedBy = "team")
private List<Member> members = new ArrayList<>();
```

### 일대다 [1:N]

### 일대다 단방향 ✅

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/05557c60-5bb1-4ae6-a62d-1719923c6844/Untitled.png)

- 1이 연관관계의 주인이다.
- 우선, 권장하지 않는 모델이다. 표준 스펙에서 지원하는 모델이니 설명은 해주겠다. 
실무에서는 거의 사용하지 않는다.
- Team을 중심으로 외래 키 관리 등을 하겠다는 의미. 
team은 member를 알고 싶지만, member는 team을 알고 싶지 않은 경우에 나올 수 있다.
Team이 List members를 가진다.
- DB입장에서 생각해봤을 때, DB 설계 상 무조건 N 쪽에 FK가 들어가야 한다.
FK 가 반대로 되어 있어서 Team에 FK가 있으면, Team을 계속 insert 해야 한다.
team이 중복돼서 1이 아니라 N이 되어버림.
- 그래서, Team의 List members를 바꾸었을 때, Member 테이블의 TEAM_ID를 업데이트 해야 한다.
**연관관계의 주인이 외래 키를 관리해야 하기 때문에.**
- 즉, Team의 List members를 바꾸면, Member에 있는 TEAM_ID를 변경시켜 주어야 한다.

```java
// Team 코드
@OneToMany
@JoinColumn(name = "TEAM_ID")
private List<Member> members = new ArrayList<>();

// Main 코드
		Member member = new Member();
		member.setUsername("member");
		em.persist(member);

    Team team = new Team();
		team.setName("TeamA"); // 여기까지는 Team 테이블에 그냥 업데이트 하면 된다.
		team.getMembers().add(member);
		// 옆에 있는 MEMBER 테이블로 가서 업데이트를 할 수 밖에 없다. 
		// = 어쩔 수 없이 update 쿼리가 한번 더 나가야 한다. = 성능 상의 단점(미약함)

		em.persist(team);
```

- 물론 성능 이슈도 있지만, 미약하다. 
더 큰 문제는, 비즈니스 로직을 짜다보면 Team 엔티티만 변경했는데 Member가 update되는 결과가 나올 수 있음.
실무에서는 테이블 수십개가 엮여서 돌아가므로 운영이 매우 어려워진다.
- **따라서, Team에서 꼭 Member를 가져와야 할 상황이면 일대다 관게를 가지는 것보다, 
그냥 다대일 관계에서 양방향으로 추가하는 것이 좋다.**
- 객체지향적으로 약간 손해(member에서 team을 갈 일이 없는데 갈 수 있음)가 있더라도, 
DB에 맞춰서 설계하는 것이 더 유지보수 하기 좋다.
- 정리
    - 일대다 단방향은 일대다(1:N)에서 **1이 연관관계의 주인이다**.
    - 테이블 일대다 관계는 **항상 N 쪽에 외래 키가 있다.**
    - 객체와 테이블의 차이 때문에 반대편 테이블의 외래 키를 관리하는 특이한 구조다.
    - @JoinColumn을 꼭 사용해야 한다. 그렇지 않으면 조인 테이블 방식을 사용하기에, 중간에 테이블이 하나 추가된다. (Team_Member)
    중간 테이블은 장점도 있지만, 운영 상의 어려움이나 성능 상의 안 좋은 점이 있다.
- 일대다 단방향 매핑의 단점
    - 엔티티가 관리하는 외래 키가 다른 테이블에 있음 = 어마어마한 단점
    - 연관관게 관리를 위해 추가로 UPDATE SQL 실행
- 일대다 단방향 매핑보다는 **다대일 양방향 매핑**을 사용하자.

### 일대다 양방향

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/b1e4cc8a-b822-4ca4-8ccb-3cac69a29feb/Untitled.png)

- 스펙 상 되는 것이 아니라, 약간 야매로 된다.

```java
@ManyToOne
@JoinColumn(name = "TEAM_ID", insertable = false, updatable = false)
private Team team;
```

- 지금 둘 다 연관관계의 주인이다. 둘 중 어느 필드로 FK를 업데이트할 지 꼬이게 된다.
- insertable = false, updatable = false 을 넣어주면, 읽기 전용이 된다.
- 매핑이 되어있고 값도 다 쓰지만, Insert와 Update를 하지 않는다.
- Team의 members와 Member의 team을 둘 다 연관관계의 주인으로 만들어 놓고, team은 읽기 전용으로 매핑한 것. = 양방향 매핑과 똑같이 됨.
- 정리
    - 이런 매핑은 공식적으로 존재하지 않는다.
    - @JoinColumn(insertable=false, updateable=false)
    - **읽기 전용 필드를 사용해서 양방향**처럼 사용하는 방법
    - 결론은, 그냥 **다대일 양방향**을 사용하자.
    - 매핑과 설계는 단순해야 한다. 실무에서는 수십개의 테이블이 엮여서 돌아가기 때문에.

### 일대일 [1:1]

### 일대일 관계

- 일대일 관계는 그 반대도 일대일
- 주 테이블이나 대상 테이블 중에 외래 키 선택 가능(대칭이기 때문에)
    - 주 테이블에 외래 키
    - 대상 테이블에 외래 키
- 외래 키에 데이터베이스 유니크(UNI) 제약조건 추가가 되어야 1대1이 된다.
    - 제약 조건을 안넣어도 할 수 있는데, 애플리케이션 관리를 잘 해야 한다.

### 일대일: 주 테이블에 외래 키 단방향

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/0073b71e-7d97-460a-824d-4b6180699840/Untitled.png)

- Member를 주 테이블이라고 생각.
회원이 락커 하나를 가지고 있다는 비즈니스 룰이 있다고 가정한다.
- Member테이블 입장에서 LOCKER_ID를 FK로 가지고 UNI 제약 조건을 가진다.
(그 반대도 허용)
- 다대일(@ManyToOne) 단방향 매핑과 유사하다.

### 일대일: 주 테이블에 외래 키 양방향

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/2d281f0b-f38d-4103-a58d-cc048d53c758/Untitled.png)

```java
// Member
@OneToOne
@JoinColumn(name = "LOCKER_ID") // JoinColumn은 넣어주는 것이 좋다. 기본값이 직관적이지 않고 지저분하다.
private Locker locker;

// Locker
@OneToOne(mappedBy = "locker")
private Member member;
```

- 다대일 양방향 매핑처럼 **외래 키가 있는 곳이 연관관계의 주인**
- 반대편은 mappedBy를 적용한다.

### 일대일: 대상 테이블에 외래 키 단방향

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/b4414553-97d8-45a7-8f80-70993067d190/Untitled.png)

- 대상 테이블에 외래 키가 있는 단방향 관계는 JPA가 지원해주지 않는다.
- 양방향 관계는 지원해준다.

### 일대일: 대상 테이블에 외래 키 양방향

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/c8ad9b46-f07b-426b-849b-7d7207ede4f0/Untitled.png)

- Locker에 있는 member를 연관관계의 주인으로 잡아서 매핑하면 된다.
- 말의 어폐가 있는게, 주 테이블에 외래 키가 있는 양방향을 뒤집은 것이다.
- 일대일 관계는, 내 엔티티에 있는 외래 키를 직접 관리해야 한다.
- 사실 일대일 주 테이블에 외래 키 양방향과 매핑 방법은 같다.

### 일대일 정리

- **주 테이블에 외래 키**
    - 여기서 주 테이블은, 자주 엑세스 하는 테이블이라고 생각하면 된다.
    - 주 객체가 대상 객체의 참조를 가지는 것 처럼, 주 테이블에 외래 키를 두고 대상 테이블을 찾음
    - 객체지향 개발자가 선호
    - JPA 매핑 편리
    - 장점: 주 테이블만 조회해도 대상 테이블에 데이터가 있는지 확인 가능
    DB 쿼리 하나로 join 필요 없이 member에 대해 locker 값 확인 가능
    - 단점: 값이 없으면 외래 키에 null 허용
    - 영한님이 선호하는 방식.
- **대상 테이블에 외래 키**
    - 대상 테이블에 외래 키가 존재
    - 전통적인 데이터베이스 개발자가 선호
    - 장점: 주 테이블과 대상 테이블을 일대일에서 일대다 관계로 변경할 때 테이블 구조 유지
    한 member가 여러 locker를 가지게 될 수도 있음.
    - 단점: JPA 입장에서 보면, 양방향으로 만들어야 한다. member에서 locker를 호출할 일이 많기 때문에.
    - 또한 치명적인 단점으로, 프록시 기능의 한계로 **지연 로딩으로 설정해도 항상 즉시 로딩됨**(프록시는 뒤에서 설명)
        - JPA입장에서는 프록시 객체를 만드려면 Member의 locker에 값이 있는지 없는지를 알아야 한다.
        주 테이블에 외래 키가 있는 경우에는 Member를 로딩할 때 foreign key에 값이 있으면 넣어주고 없으면 null을 locker에 넣으면 된다. MEMBER만 보고 쿼리하면 된다.
        - 대상 테이블에 외래 키가 있는 경우에는, Member의 locker에 값이 있는지 없는지를 확인하기 위해서 MEMBER 테이블 뿐만 아니라 LOCKER 테이블도 뒤져서 MEMBER_ID에 내 Member가 있는지를 where문에 넣어서 확인해야 인정이 된다. 
        = 어차피 쿼리가 나가기 때문에 프록시를 만들 이유가 없다. 지연 로딩으로 세팅하는 게 의미가 없다.
        그래서 하이버네이트의 경우에는 지연 로딩으로 설정해도 항상 즉시 로딩 된다.

### 다대다 [N:N]

실무에서 사용하면 안된다.

### 다대다

- 관계형 데이터베이스는 정규화된 테이블 2개로 다대다 관계를 표현할 수 없다.
- 연결 테이블을 추가해서 일대다, 다대일 관계로 풀어내야 한다.

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/3501f366-e44e-48e0-95df-4ec6e2207b5d/Untitled.png)

- 그러나 객체는 컬렉션을 사용해서 객체 2개로 다대다 관계가 가능하다.

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/eabe4c4d-42e2-4168-954b-38aab9e17a90/Untitled.png)

```java
// Member
@ManyToMany
@JoinTable(name = "MEMBER_PRODUCT")
private List<Product> products = new ArrayList<>();
```

- @ManyToMany를 사용한다.
- @JoinTable로 연결 테이블을 지정한다.
- 다대다 매핑은 단방향, 양방향이 가능하다.

### 다대다 매핑의 한계

- 편리해보이지만, 실무에서 사용할 수 없다.
- 연결 테이블이 단순히 연결만 하고 끝나지 않는다.
- 주문시간, 수량같은 데이터가 들어올 수 있는데, 매핑 정보 말고 추가 정보를 넣을 수 없다.
- Member와 Product를 조회하려면 중간 테이블에 예상치 못한 쿼리가 나갈 수 있다.

### 다대다 한계 극복

- 연결 테이블용 엔티티 추가(연결 테이블을 엔티티로 승격)
- @ManyToMany → @OneToMany, @ManyToOne

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/004c5154-9171-4d62-8ee6-c50baebe00b7/Untitled.png)

```java
		// MemberProduct
		@Id @GeneratedValue // id를 의미 없는 값으로 두는 것이 좋다.
    private Long id;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "PRODUCT_ID")
    private Product product;

		// Member
		@OneToMany(mappedBy = "member")
    private List<MemberProduct> memberProducts = new ArrayList<>();

		// Product
		@OneToMany(mappedBy = "product")
    private List<MemberProduct> memberProducts = new ArrayList<>();
```

- 이러면 원하는 추가 필드를 MemberProduct에 넣을 수 있다.
- 테이블 이름도 MemberProduct가 아니라 Orders 등 의미있는 이름으로 지정할 수 있다.
- 결론적으로, 실제 비즈니스에서 ManyToMany로 풀 수 있는 게 거의 없다. 
그러니 연결 테이블용 엔티티를 추가하는 것이 좋다.
- 전통적인 방식으로는 MEMBER_ID와 PRODUCT_ID를 묶어서 PK로 잡고 각각을 FK로 잡는다.
영한님은 실전에서 부딪히면서 느낀 거로는 그냥 의미 없는 값을 사용하는 게 더 낫다. 
GeneratedValue를 id에 붙여서 사용하는 것이 JPA 매핑도 쉽고 나중에 변경도 쉽다.
