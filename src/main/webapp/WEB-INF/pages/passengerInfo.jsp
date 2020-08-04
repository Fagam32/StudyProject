<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">

    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css"
          integrity="sha384-9aIt2nRpC12Uk9gS9baDl411NQApFmC26EwAOH8WgZl5MYYxFfc+NcPb1dKGj7Sk" crossorigin="anonymous">
    <title>Passenger Info</title>
</head>
<body class="text-center">
<%@include file="parts/topNavigationMenu.jsp" %>
<div class="container">
    <div class="row justify-content-center align-items-center" style="height:100vh ">
        <div class="col-3 card card-body">
            <form method="post">
                <%if (request.getParameter("message") != null) {%>
                <p style="color: red">
                    <% out.print(request.getParameter("message"));
                    }%>
                </p>
                <div class="form-group">
                    <label> Name <input class="form-control" type="text" name="name"
                                        value="${empty name? '' : name}"/></label>
                    <label> Surname <input class="form-control" type="text" name="surname"
                                           value="${empty surname? '' : surname}"/></label>
                    <label> Birthday <input class="form-control" type="date" name="birthDate"
                                            value="${empty birthDate? '' : birthDate}"/></label>
                </div>
                <button type="submit" class="btn btn-primary">Refresh</button>
            </form>
        </div>
    </div>
</div>

<%@include file="parts/bottomNavigationMenu.jsp" %>
</body>
</html>