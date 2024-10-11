# Team1_BE

---

# 그라운드 룰

- 추가 요망

---

# 코드 컨벤션

- angular code conventions을 기반으로 커밋 메세지 작성
  - feat : 새로운 기능 추가
  - fix : 버그 수정
  - docs : 문서 변경
  - style : 코드 스타일 변경 (포매팅 수정, 세미콜론 추가 등)
  - refactor : 코드 리팩토링
  - test : 테스트 코드 추가, 수정
  - chore : 빌드 프로세스, 도구 설정 변경 등 기타 작업

---

# 구현 기능 목록

- 인증(김동혁)
    - 회원가입 & 로그인
      - google oauth2로 구현
      - 몇 가지 예외처리된 경로를 제외하면 구글 로그인 요구
        - swagger3
        - h2-console
        - /
      - flow
        - 토큰 검증
          - 토큰이 없다면
            - 로그인
              - 가입 정보가 없다면 회원가입
            - 성공시 토큰 생성 및 반환
              - GET Param으로 반환됨
            - 실패시 /error로 이동
          - 토큰이 있다면
            - 토큰 검증
            - 토큰 내부의 정보를 파싱
    - 유저 정보 조회
      - 토큰내 정보로 유저 파싱
    - 유저 정보 수정
      - 토큰내 정보로 유저 파싱
      - 유저이름(username), 사진(picture) 수정 가능
      - 입력된 값만 검증해서 변경
        - 공백 검사
          - 공백시 기존 값 유지
        - 이미지는 url 검사
          - url패턴을 어길 시 400
    - 유저 정보 삭제
      - soft delete
    - 코드로 참여
      - 코드로 인증 후 토큰 반환
      - 코드는 멤버id를 aes256으로 암호화 해서 코드 생성
      - 코드를 복호화해서 검증
      - 인증 후 멤버 이메일로 토큰 생성
    - 시큐리티 토큰 생성 예외처리
      - 기존 방식으로(@RestControllerAdvice)는 스프링 시큐리티 예외처리가 안됨
      - 다른 방법 찾는중(w5)
- 프로젝트(김도헌)
  - 프로젝트 리스트 조회
    - 토큰 내 유저의 email 정보를 통해 본인이 관리하는 프로젝트들을 페이지네이션을 통해 보여줌 
  - 프로젝트 기간 리스트 조회
    - 토큰 내 유저의 email 정보를 통해 본인이 관리하는 프로젝트들의 일정 정보를 페이지네이션을 통해 보여줌
  - 프로젝트 조회
    - 프로젝트 Id를 통한 단일 조회
  - 프로젝트 멤버 조회
    - 프로젝트 Id를 통해 해당 프로젝트의 멤버들의 정보를 조회
    - 현재 객체를 통해 정보를 받아 오는 것으로 되어있지만, 추후 fetch join을 활용한 방법으로 수정 예정
  - 프로젝트 생성
    - 프로젝트를 생성함
    - 플로우 : 
      - email을 통해 유저가 존재하는 지 검증 ->
      - DTO에 담긴 optionEntity들의 id 정보들을 통해 OptionEntity조회 ->
      - OptionEntity을 ProjectOption으로 매핑 ->
      - 해당 정보를 가진 ProjectEntity를 생성 후 Repo에 save ->
      - 각 ProjectOption의 ProjectEntity field를 생성한 ProjectEntity로 설정
  - 프로젝트 설정 수정
    - 해당하는 Id의 프로젝트를 수정
    - 플로우 : 
      - 프로젝트가 존재하는지 검증 ->
      - 기존의 ProjectOption 리스트 초기화->
      - DTO에 담긴 Option id들을 통해 OptionEntity 조회 ->
      - OptionEntity를 ProjectOption으로 매핑 ->
      - ProjectOption 리스트에 추가 ->
      - 나머지 정보 업데이트 후 저장
  - 프로젝트 삭제
    - 해당하는 Id의 프로젝트 삭제
    - 현재는 그냥 삭제를 하지만, 추후 Soft Delete 방식으로 재구현 예정
- 게스트(권순호)
  - 게스트 생성
  - 게스트 수정
  - 게스트 삭제
  - 프로젝트 내 게스트 추가
  - 프로젝트 코드 메일로 전달
- 태스크(조서영)
  - 태스크 리스트 조회(프로젝트 아이디를 이용)
  - 태스트 단건 조회
  - 태스크 생성
    - isDeleted와 progress 생성시 기본값인 0으로 설정
    - 프로젝트id, 태스크를 수행할 멤버(팀원)id, 제목, 본문,시작일, 종료일을 입력받아서 ->
      dto에서 시작일, 종료일 검증 ->
      토큰내 이메일과 프로젝트id로 프로젝트를 불러오기 ->
      멤버(팀원)존재 검증 ->
      task객체 생성 (미완)
      저장
  - 태스크 수정
  - 태스트 삭제
- 이벤트
  - 독려 이메일 전달
  - 각 게스트별 진행도 조회
  - 태스크별 진행도 조회
- ...
---
# 피드백
- [3주차 리뷰](https://github.com/kakao-tech-campus-2nd-step3/Team1_BE/issues/13)
- [3주차 피드백](https://github.com/kakao-tech-campus-2nd-step3/Team1_BE/pull/11)
- [4주차 리뷰](https://github.com/kakao-tech-campus-2nd-step3/Team1_BE/issues/17)
- [4주차 멘토링](https://quickest-asterisk-75d.notion.site/Back-end_-323b0e20ae2b405189ffe5b7c4242e00)
- [5주차 리뷰](https://github.com/kakao-tech-campus-2nd-step3/Team1_BE/issues/31)
---
# Issue
- [week4 프로젝트 빌드 실패](https://github.com/kakao-tech-campus-2nd-step3/Team1_BE/issues/27)
- [week6 프로젝트 빌드 실패](https://github.com/kakao-tech-campus-2nd-step3/Team1_BE/issues/39)
- [week6 구글 로그인 실패](https://github.com/kakao-tech-campus-2nd-step3/Team1_BE/issues/41)
- [week6 멤버 생성 실패](https://github.com/kakao-tech-campus-2nd-step3/Team1_BE/issues/42)
- [week6 pathvaliable로 값 파싱 실패](https://github.com/kakao-tech-campus-2nd-step3/Team1_BE/issues/43)
- [week6 task 날짜 validation 오류](https://github.com/kakao-tech-campus-2nd-step3/Team1_BE/issues/44)
- [week6 aws server 구글 로그인 실패](https://github.com/kakao-tech-campus-2nd-step3/Team1_BE/issues/45)
- [week6 멤버 생성 권한 수정](https://github.com/kakao-tech-campus-2nd-step3/Team1_BE/issues/46)
- [week6 task 조회 쿼리 문법 오류](https://github.com/kakao-tech-campus-2nd-step3/Team1_BE/issues/47)
---
# 질문사항
- week3
  - 팀원마다 코드스타일이 다른경우 통일을 하나요?
  - 프로젝트 시작 시 세팅은 어느정도로 하나요?
  - 현재 팀원마다 각각 domain을 1개씩 담당하여 작업을 하고 있습니다. 이러한 경우 다른 팀원이 만들어야 하는 객체를 참조해야 되는 상황일 때,(저는 그냥 구현을 해버려서 conflict가 발생했습니다.) 구현을하지 않고 mock 혹은 fake 객체를 만들어 두고 작업을 하는 편인가요?
  - 보통 이 정도 규모의 프로젝트를 분업하게 되면 어떠한 기준으로 작업을 분배하나요?
  - conflict resolve시의 기준이 있나요?
- week4(멘토링)
  - 유저 삭제시 인증 구현 방식
  - 다양한 소셜 로그인의 유저를 특정하는 방법
  - 메일 발송 방법
  - 코드 스타일
  - 동시성 처리
- week5
  - softDelete의 구현 방법
  - 연결괸 객체의 정보를 가져오는 방법
- week6