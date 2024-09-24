# kstd-assignment
| 기술스택    | 버전  |
| ----------- | ----- |
| Spring Boot | 3.3.3 |
| JDK         | 21    |
| gradle      | 8.8   |
| DB          | H2    |

## API 목록

- BackOffice

  - 강연 목록 (전체 강연 목록)
  - 강연 등록(강연자, 강연장, 신청 인원, 강연 시간, 강연 내용 입력)
  - 강연 신청자 목록(강연 별 신청한 사번 목록)

- Front

  - 강연목록(신청 가능한 시점부터 강연 시작 시간 1일 후까지 노출)
  - 강연 신청(사번 입력, 같은 강연 중복 신청 제한)
  - 신청 내역 조회 (사번입력)
  - 신청한 강연 취소
  - 실시간 인기 강연

  

## 추가사항

- 동시성 이슈를 고려하여 구현
- 기능에 대한 단위 테스트를 작성
- 데이터 일관성을 고려



## 제출 내용

- 개발 소스
- README.md 
  - 사용하신 개발 언어
  - 사용한 프레임 워크
  - 사용한 RDBMS
  - 데이터 설계
  - 그밖에 고민했던 부분 또는 설명하고 싶은 부분
