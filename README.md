# 🚀 2단계 - 칸반 보드 관리(수정)

## 2단계 기능 및 작업 명세

### 기능 요구 사항
* 태스크 카드를 클릭하여 수정/삭제할 수 있다.
* 태스크가 수정/삭제되었을 때 스낵바를 노출한다.
* Review 상태를 추가하고, 상태별 태스크의 특징을 적용한다.
    * To Do: 태스크 삭제 가능. 담당자 미지정 가능
    * In Progress: 태스크 삭제 가능. 담당자 지정 필수
    * Review: 태스크 삭제 불가능. 담당자 지정 필수
    * Done: 태스크 삭제 불가능. 담당자 지정 필수
* 태스크 상태 전이 규칙을 적용한다. 규칙에 정의되지 않은 상태 전이는 불가능하다.

```
To Do
└─→ In Progress (작업 시작)

In Progress
├─→ To Do (다시 계획)
└─→ Review (리뷰 요청)

Review
├─→ In Progress (수정 필요)
└─→ Done (승인 완료)

Done
└─→ To Do (재작업)
```

### 테스트 시나리오

#### **상태변경**
*To Do*
- 상태 변경은 To Do에서는 In Progress로만 가능하다
- 상태 변경은 To Do에서는 Review, Done으로 불가능하다
- To Do에서 In Progress로 상태를 변경하는데 담당자가 미지정 되어 있을경우 상태 전이는 불가능하다
- To Do에서 In Progress로 담당자가 미지정 되어 있어 상태 전이가 안 된 경우 스낵바 "해당 상태로 옮길 수 없습니다." 가 뜬다

*In Progress*
- 상태 변경은 In Progress에서는 To Do, Review로 가능하다
- 상태 변경은 In Progress에서는 Done으로 불가능하다

*Review*
- 상태 변경은 Review에서는 In Progress, Done으로 가능하다
- 상태 변경은 Review에서는 To Do로 불가능하다

*Done*
- 상태 변경은 Done에서는 To Do로만 가능하다
- 상태 변경은 Done에서는 Review, In Progress로 불가능하다