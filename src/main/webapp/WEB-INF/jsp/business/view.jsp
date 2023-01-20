<%@ page contentType="text/html;charset=UTF-8" %>

<%@ include file="../common/header.jsp" %>


<div class="container">
    <div class="card">
        <div class="card-header">
            Affaire - <strong>${business.name}</strong>
        </div>
        <div class="card-body mw-100 overflow-auto">
            <table class="table">
                <caption>Informations de l'affaire</caption>
                <tbody>
                <tr>
                    <th>Nom</th>
                    <td>${business.name}</td>
                </tr>
                <tr>
                    <th>Déposeur</th>
                    <td>
                        <a class="hrefLink" href="/apporteur-affaire/afficher?id=${business.businessProvider.id}">
                            ${business.businessProvider.getFullName()}
                        </a>
                    </td>
                </tr>
                <tr>
                    <th>Montant de l'affaire</th>
                    <td>${business.amount} €</td>
                </tr>
                <tr>
                    <th>Date de dépot</th>
                    <td><fmt:formatDate value="${business.date}" pattern="dd/MM/yyyy à HH:mm:ss"/></td>
                </tr>

                </tbody>
            </table>
        </div>
    </div>

    <div class="card w-25">
        <div class="card-header">
            Commissions de l'affaire
        </div>
        <div class="card-body">
            <table class="w-100">
                <caption>Commissions de l'affaire</caption>
                <tr>
                    <th>Receveur</th>
                    <th>Montant</th>
                </tr>
                <c:forEach var="com" items="${commissions}">
                    <tr>
                        <td>
                            <a class="hrefLink" href="/apporteur-affaire/afficher?id=${com.commissionOwner.id}">
                                    ${com.commissionOwner.getFullName()}
                            </a>
                        </td>
                        <td>${com.amount} €</td>
                    </tr>
                </c:forEach>
            </table>
        </div>
    </div>

    <%@ include file="addOrEditBusinessModal.jsp" %>

    <sec:authorize access="hasRole('ADMIN')">
        <a id="openEditBusinessModalBtn" data-id="${business.id}">
            <em class="fas fa-edit"></em>
        </a>
        <a id="openDeleteBusinessModalBtn" data-id="${business.id}">
            <em class="fas fa-trash"></em>
        </a>
    </sec:authorize>

    <a class="btn btn-primary" role="button" onclick="history.back()">Revenir en arrière</a>

</div>

<%@ include file="../common/footer.jsp" %>

<script type="module">
    import {deleteBusiness, editBusinessModal} from "/resources/static/js/gestionBusinessModal.js";

    $(document).ready(function () {
        $('#openEditBusinessModalBtn').on('click', function (event) {
            editBusinessModal(event, 'modifier');
        });
        $('#openDeleteBusinessModalBtn').on('click', function (event) {
            deleteBusiness(event)
        });
    });
</script>