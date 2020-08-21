# 2020_madcamp_third - 우연히 봄
Android project for week 3 of Madcamp@KAIST - 개발기간: 1주일
By 서건식, 황소진, 서규성

블루투스 기반 근거리 매칭 서비스 '우연히 봄'

출근길, 당신과 매일 같이 출근하는 사람들이지만 평생 모르고 살아갈 수도 있습니다.

평생 모르고 지나칠 수 있는 인연을 우연히 봄이 이어드립니다.


# Abstraction

## 서비스 설명

우연히 봄은 구글과 애플이 코로나 확진자 List를 추출하기 위해 블루투스 MAC ADDRESS를 받아와 근처에 있던

사람들에게 확진자 발생시 알림을 보내는 기술에서 아이디어를 얻어

매일 저녁 오후 7시, 출근길이나 지나가면서 스쳐간 인연들을 블루투스 MAC ADDRESS와 서버에 등록된 ID와 비교하여

일정시간 접촉시 Matching List에 추가해 소개해주는 서비스입니다.



## 친밀도 점수

접촉한 사람들 중에서도 어느 시간대에 접촉했는지, 얼마나 많이 접촉했는지, 사는 곳이 어디인지 등등

많은 요소를 결합해 알고리즘을 적용하여 친밀도 점수를 Backend에서 계산하여 보여줍니다.

이는 우연히 봄의 과금요소이며, 상단 메뉴에 소개된 인연들 중 가장 높은 친밀도 점수를 보여줍니다.

어떤 사람이 가장 높은 확률의 친밀도 점수를 가지고 있는지는 모르며, 이를 확인하기 위해선 결제가 필요합니다. (결제 시스템 미구현)



## 기타 기능

프로필 수정과 프로필 확인, 별점 보내기, 좋아요 보내기, 매칭 시 연락기능 등을 구현하였습니다.

Backend 코드는 해당 github에 있습니다. 

https://github.com/geonsikSeo/2020_madcamp_third-server


# Application Intro

![Loading](https://github.com/geonsikSeo/2020_madcamp_third/blob/master/imageformd/1.gif)

어플리케이션을 구동하면, 권한 허용 ViewPager와 어플 설명을 위한 IntroActivity를 실행한 후

Login Activity로 넘어갑니다.


