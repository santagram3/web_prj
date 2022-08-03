<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!-- header -->
<header>
    <div class="inner-header">
        <h1 class="logo">
            <a href="#">
                <img src="/img/logo.png" alt="로고이미지">
            </a>
        </h1>
        <h2 class="intro-text">Welcome
            <c:if test="${loginUser !=null}">
                ${loginUser.name}님 HELLO~~!
            </c:if>
        </h2>
        <a href="#" class="menu-open">
            <span class="menu-txt">MENU</span>
            <span class="lnr lnr-menu"></span>
        </a>
    </div>

    <nav class="gnb">
        <a href="#" class="close">
            <span class="lnr lnr-cross"></span>
        </a>
        <ul>
            <li><a href="/">Home</a></li>
            <li><a href="#">About</a></li>
            <li><a href="/board/list">Board</a></li>
            <li><a href="#">Contact</a></li>

            <c:if test="${sessionScope.loginUser==null}">
                <!-- // 로그인 안한사람 -->
                <li><a href="/member/sign-up">SignUp</a></li>
                <li><a href="/member/sign-in">SignIn</a></li>
            </c:if>

            <c:if test="${sessionScope.loginUser!=null}">
                <!-- // 로그인 한사람 -->
                <li><a href="/member/myPage">MyPage</a></li>
                <li><a href="/member/sign-out">SignOut</a></li>
            </c:if>
        </ul>
    </nav>

</header>
<!-- //header -->