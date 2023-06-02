# Tripbook Main Api

## Git Rules

### Commit message

- 커밋 메세지의 시작은 어떤 작업인지 명시하는 접두사(`Feat`, `Fix`, `Docs`, `Chore` 등)를 붙입니다.
- 커밋 메세지 제목에는 간결하게 코드가 포함된 작업을 대표하는 내용을 작성합니다.
- 제목에서 표현할 수 있는 부분이 적은 경우 commit body에 해당 내용을 작성합니다.

### Branch

- 브랜치 이름은 다음과 같은 규칙으로 만듭니다.
```markdown
`${접두사}-${구현하려는 기능 내용의 요약}`

예시 : `feat-login`, `fix-nickname-validation`
```

- 브랜치에서 변경된 코드가 많을 경우 리뷰에 많은 시간이 소요되므로 최대한 작업을 WBS 단위로 나누어 작업합니다.
- 해당 브랜치에서 하고자 하는 일과 연관이 없는 작업은 다른 브랜치를 통해서 작업합니다.

### Pull Requests and Reviews

- PR은 지정된 템플릿을 사용해서 작성합니다.
- 만약 **연관된 이슈나 PR이 있을 경우 해당 이슈 및 PR 번호를 명시**해줍니다.(`#${숫자}` 형태)
- PR에 리뷰 받고 싶은 기한을 지정합니다. 리뷰어들은 해당 기간 내에 리뷰를 완료해야 합니다.
- Asignees에는 항상 자기 자신을 기본으로 선택합니다.
- 2명의 리뷰어가 `Approve`를 할 경우에만 브랜치를 머지 합니다.
  - 단, hotfix가 필요한 경우에는 1명의 리뷰어에게 ASAP으로 리뷰를 받아 머지 합니다.
- 리뷰의 각 상태는 다음과 같을 때 사용합니다.
  - `Approve` : 리뷰에 이상이 없어 브랜치를 머지해도 된다고 판단될 때
  - `Request Changed` : 리뷰 상 문제점이 있어 수정이 필요하다고 판단될 때
  - `Comment` : 반드시 수정을 해야하는 것은 아니지만 개선할 수 있거나 의문 사항이 있을 때
- 머지 시에는 **Merge commit이 아닌 Squash Merge**을 사용합니다.
  - [왜 그래야 하나요?](https://discord.com/channels/1068872548747575376/1068874007480373369/1112695631488765974)
- 머지 후에 브랜치는 제거 합니다.(작성일 기준 20230602, 머지 시 자동으로 브랜치를 지우는 기능을 활성화 해두었습니다.)

### How to rebase?

- `rebase`란, 브랜치의 시작점을 특정 브랜치의 시작점으로 옮기는 것을 의미합니다.
- 주기적으로 `git remote update` 또는 `git fetch` 명령어를 통해 `master` 브랜치나 `develop` 브랜치의 최신 상황을 확인해야 합니다.
  - 왜 그래야 하나요? : 협업을 하다보면 특정 브랜치의 개발 기간이 길어지는 경우가 있습니다. 이 때, 다른 개발자들과 변경사항이 겹쳐 conflict가 발생할 수도 있고, 주요 개선된 로직이 반영되지 않은 채 작업을 할 수도 있습니다. 그렇기 때문에 주기적으로 주요 브랜치(master, develop)의 변경사항을 확인하고 해당 변경 사항을 내 브랜치에 적용할 필요가 있습니다.
- 터미널 상에서 `rebase` 하는 방법
  - 기타 GUI 툴(Github desktop, Gitkraken 등)이 있다면 해당 툴을 사용하셔도 좋습니다.(매우 편함)
  - 내가 작업 중인 `feat-login`이라는 브랜치에 `main` 브랜치를 `rebase` 하는 명령어 입니다.
    ```shell
    # HEAD는 나의 브랜치 - feat-login
    git rebase main # main 대신 내가 rebase 하고 싶은 브랜치 이름을 작성해도 됨.
    
    # conflict가 발생한 경우
    git status # 어떤 파일에서 충돌이 발생했는지 확인합니다.
    # 충돌이 발생한 파일에서 conflict를 해결한 후
    git rebase --continue # conflict가 발생하지 않는 다면 rebase가 계속 진행됩니다.
    # rebase 완료 후 remote에 업로드된 브랜치를 최신화 해줘야 합니다.
    git push --force origin feat-login 
    ```
  - 주의할 점
    1. 나의 local 브랜치가 remote 브랜치와 동기화 되어 있는지 확인해야 합니다.
    2. conflict가 발생한 경우 해당 파일에 어떤 것이 나의 브랜치의 작업 내용이고 어떤 것이 `rebase` 하려는 브랜치의 작업 내용인지 확인할 수 있습니다. 내가 작업하지 않은 conflict는 `rebase` 하려는 브랜치 코드를, 내가 변경한 코드는 나의 브랜치 코드를 따르면 **왠만하면** conflict를 쉽게 해결할 수 있습니다.
- 정리
  - git HEAD를 내가 `rebase` 하고 싶은 브랜치_보통 나의 브랜치_로 설정
  - `git rebase ${rebase 하려는 브랜치_보통 master, develop 브랜치_}` 실행
  - conflict 발생 시 `git status`로 확인
  - 충돌난 파일 수정 후 `git rebase --continue`를 통해서 리베이스 진행
  - `rebase` 완료 후 remote 브랜치 업데이트 `git push --force origin ${rebase 완료한 브랜치}`