# 아프리카TV 2022 신입 안드로이드 개발자 사전과제

### 이 애플리케이션은...
 - **GitHub API**를 사용하여 GitHub Repository를 조회하고 리스트를 보여주는 어플리케이션입니다.
<br>

### 개발환경
- 언어 : Kotlin
- minSDKVersion : 23
- compileSDKVersion : 31
- 사용 라이브러리 : Retrofit2, Glide
<br>

### 사용법
1. 상단의 검색창에 키워드를 입력합니다.
2. 검색버튼을 누릅니다.
3. 잠시 후, 검색결과에서 10개의 항목을 검색창 아래에 리스트 형식으로 보여줍니다.
4. 스크롤을 바닥까지 내리면 다음 10개의 항목을 보여줍니다.
<br>

### 스크린샷
![앱 초기상태](https://github.com/liqurt/AfreecaTV-2022-Android/blob/master/screenshot/init.png?raw=true)
![Love라는 키워드로 검색을 시작합니다](https://github.com/liqurt/AfreecaTV-2022-Android/blob/master/screenshot/Love.png?raw=true)
![리스트의 끝으로 가면 로딩 후 더 많은 결과를 보여줍니다](https://github.com/liqurt/AfreecaTV-2022-Android/blob/master/screenshot/Loading.png?raw=true)
<br>

### 코드설명
1. 앱을 실행하면 config/ApplicationClass 에서 retrofit의 인스턴스를 만듭니다.
2. src/activity/MainActivity 의 초기 상태는 검색창만 보이고, RecyclerView엔 내용이 없습니다.
3. 검색창에 키워드를 넣고 검색을 시작합니다.
4. 검색버튼이나 키워드를 입력해서 검색하는 경우, 리사이클러뷰에 매칭시킬 리스트를 비웁니다.
5. 초기에 만들었던 retrofit의 인스턴스와 api를 이용하여 "https://api.github.com/search/repositories/" 에 Get 요청을 보냅니다.
6. 이때 q엔 키워드를, per_page는 10을, page엔 1을 대입합니다.
7. 응답은 src/dto/repository의 형태로 받습니다.
8. 받은 응답에서 필요한 데이터를 추출합니다.
9. 추출한 데이터는 리사이클러뷰를 구성하는 하나 하나의 아이템에 대입 될 것입니다.
10. 이때 스크롤을 아래까지 내린다면 동일한 키워드와 page에 1을 더해서 다시 Get요청을 보냅니다.
11. 항상 원하는 데이터를 응답받지는 않습니다. 1분내로 10개의 요청을 하면 GitHub의 관련 정책때문에 오류메시지를 받습니다. (응답코드 : 403)
12. 이 경우, 사용자에게 '잠시 후 다시 시도해주세요' 라는 메시지를 보여줍니다.
