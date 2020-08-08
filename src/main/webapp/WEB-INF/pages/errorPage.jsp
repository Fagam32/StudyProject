<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">

    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css"
          integrity="sha384-9aIt2nRpC12Uk9gS9baDl411NQApFmC26EwAOH8WgZl5MYYxFfc+NcPb1dKGj7Sk" crossorigin="anonymous">
    <title>Error</title>
</head>
<%@include file="parts/topNavigationMenu.jsp" %>
<body>

<div class="text-center">
<c:out value="${not empty message? message : 'Exception happens'}"/>

</div>

</body>
<%@include file="parts/bottomNavigationMenu.jsp" %>
</html>