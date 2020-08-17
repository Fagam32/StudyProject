<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
      integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"
        integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo"
        crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"
        integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1"
        crossorigin="anonymous"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"
        integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM"
        crossorigin="anonymous"></script>
<%
    boolean logged = false;
    if (request.getRemoteUser() != null)
        logged = true;
%>
<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav mr-auto">
            <li class="nav-item active">
                <a class="nav-link" href="/index">Home<span class="sr-only"></span></a>
            </li>

            <li class="nav-item active">
                <a class="nav-link" href="/stations">Stations<span class="sr-only"></span></a>
            </li>

            <%if (logged) {%>
            <sec:authorize access="hasAuthority('ADMIN')">
            <li class="nav-item active">
                <a class="nav-link" href="/trains">Trains<span class="sr-only"></span></a>
            </li>
            </sec:authorize>

            <sec:authorize access="hasAuthority('ADMIN')">
                <li class="nav-item active">
                    <a class="nav-link" href="/edges">Edges<span class="sr-only"></span></a>
                </li>

                <li class="nav-item active">
                    <a class="nav-link" href="/users">Users<span class="sr-only"></span></a>
                </li>
            </sec:authorize>
            <%}%>
            <li class="nav-item active">
                <a class="nav-link" href="/search">Search<span class="sr-only"></span></a>
            </li>
        </ul>
        <%if (logged) {%>
        <div class="nav-item dropdown">
            <a class="nav-link dropdown-toggle my-2 my-lg-0" href="#" id="profileDropdown" role="button"
               data-toggle="dropdown"
               aria-haspopup="true" aria-expanded="false">
                Profile
            </a>
            <div class="dropdown-menu" aria-labelledby="profileDropdown">
                <a class="dropdown-item" href="/myTickets">My tickets</a>
                <a class="dropdown-item" href="/passengerInfo">Settings</a>
                <a class="dropdown-item" href="/logout">Logout</a>
            </div>
        </div>
        <%} else {%>
        <a class="my-2 my-lg-0" href="/login">Login</a>
        <%}%>
    </div>
</nav>