<%@ page contentType="text/html;charset=UTF-8" %>

<nav class="navbar navbar-expand-lg navbar-dark bg-dark static-top">
    <div class="container-fluid">
        <a class="navbar-brand" href="/">
            <%@ include file="/resources/static/assets/maison.svg"%>
        </a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarText">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarText">
            <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                <li class="nav-item">
                    <a class="nav-link" href="/apporteur-affaire/">Apporteur d'affaire</a>
                </li>
                <sec:authorize access="hasRole('ADMIN')">
                    <li class="nav-item">
                        <a class="nav-link" href="/parametres/">Paramètres</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="/statistique/">Statistiques</a>
                    </li>
                </sec:authorize>
            </ul>
            <span class="navbar-text">
                <a class="nav-link" href="/logout"><em class="fa fa-xl fa-sign-out"></em></a>
            </span>
        </div>
    </div>
</nav>