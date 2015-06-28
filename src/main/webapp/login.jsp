<html>
    <body>
    <center>
        <c:url value="/login" var="loginUrl" />
        <form action="${loginUrl}" method="post">
            <img src="resources/img/home_screen.jpg" alt="TestDisplay"/>
            <p>
                <label for="username">Username</label> <input type="text"
                                                              id="username" name="username" />
            </p>
            <p>
                <label for="password">Password</label> <input type="password"
                                                              id="password" name="password" />
            </p>
            <input type="hidden" name="${_csrf.parameterName}"
                   value="${_csrf.token}" />
            <button type="submit" class="btn">Log in</button>
        </form>
    </center>
</body>
</html>
