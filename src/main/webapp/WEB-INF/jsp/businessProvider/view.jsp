<%@ page contentType="text/html;charset=UTF-8" %>

<%@ include file="../common/header.jsp" %>

<div class="d-flex flex-column align-items-center">
        <div class="card w-75">
            <div class="card-header d-flex justify-content-between">
                Informations de l'apporteur d'affaire
                <div>
                    <sec:authorize access="hasRole('ADMIN')">
                        <a id="openEditBusinessProviderModalBtn" data-id="${businessProvider.id}">
                            <em class="fas fa-edit"></em>
                        </a>
                        <a id="openDeleteBusinessProviderModalBtn" data-id="${businessProvider.id}">
                            <em class="fas fa-trash"></em>
                        </a>
                    </sec:authorize>
                </div>
            </div>
            <div class="card-body mw-100 overflow-auto">
                <table class="table">
                    <caption>Informations de l'apporteur d'affaire</caption>
                    <tbody>
                    <tr>
                        <th>Prénom</th>
                        <td>${businessProvider.firstName}</td>
                    </tr>
                    <tr>
                        <th>Nom</th>
                        <td>${businessProvider.lastName}</td>
                    </tr>
                    <c:if test="${businessProvider.sponsor != null}">
                        <tr>
                            <th>Parrain</th>
                            <td>
                                <a class="hrefLink" href="/apporteur-affaire/afficher?id=${businessProvider.sponsor.id}">
                                        ${businessProvider.sponsor.getFullName()}
                                </a>
                            </td>
                        </tr>
                    </c:if>
                    </tbody>
                </table>
            </div>
        </div>
        <div class="my-1 p-1 card">
            <div class="col">
                <div class="form-check form-switch">
                    <input class="form-check-input" type="checkbox" role="switch" id="displayDirectBusinessesBtn"> <label
                        class="form-check-label" for="displayUndirectBusinessesBtn">Voir les affaires directes</label>
                </div>
            </div>
            <div class="col">
                <div class="form-check form-switch">
                    <input class="form-check-input" type="checkbox" role="switch" id="displayUndirectBusinessesBtn"> <label
                        class="form-check-label" for="displayDirectBusinessesBtn">Voir les affaires indirectes</label>
                </div>
            </div>
        </div>
        <div id="direct-businesses-container" class="card w-75 my-1">
            <div class="card-header"> Affaires directes</div>
            <div class="card-body">
                <table id="businessProvider-direct-businesses-table" class="table table-bordered table-responsive compact"
                       style="width: 100%;">
                    <caption>Affaires directes</caption>
                    <thead>
                        <tr>
                            <th class="searchable">Nom</th>
                            <th class="searchable">Montant</th>
                            <th class="searchable">Commission percue</th>
                            <th class="searchable">Date</th>
                            <%--                    <sec:authorize access="hasRole('ADMIN')">--%> <%--                        <th class="text-center">Gestion</th>--%> <%--                    </sec:authorize>--%>
                        </tr>
                    </thead>
                </table>
            </div>
        </div>
        <div id="undirect-businesses-container" class="card w-75 my-1">
        <div class="card-header"> Affaires indirectes</div>
        <div class="card-body">
            <table id="businessProvider-undirect-businesses-table" class="table table-bordered table-responsive"
                   style="width: 100%">
                <caption>Affaires indirectes</caption>
                <thead>
                <tr>
                    <th class="searchable">Nom</th>
                    <th class="searchable">Déposeur</th>
                    <th class="searchable">Montant</th>
                    <th class="searchable">Commission déposeur</th>
                    <th class="searchable">Comission ${businessProvider.firstName} ${businessProvider.lastName}</th>
                    <th class="searchable">Date</th>
                    <%--                    <sec:authorize access="hasRole('ADMIN')">--%> <%--                        <th class="text-center">Gestion</th>--%> <%--                    </sec:authorize>--%>
                </tr>
                </thead>
            </table>
        </div>
    </div>
    <%@include file="editBusinessProviderModal.jsp" %>
    <sec:authorize access="!hasRole('ADMIN')">
        <c:if test="${loggedUserId == businessProvider.id}">
            <a id="openEditBusinessProviderModalBtn" data-id="${businessProvider.id}">
                <em class="fas fa-edit"></em>
            </a>
        </c:if>
    </sec:authorize>

    <a class="btn btn-primary" role="button" onclick="history.back()">Revenir en arrière</a>
</div>

<script type="module">
    import {
        changeThHeaderInSearchInput,
        frenchTransltation,
        hideGlobalSearchInput,
        setEventSearchForTheadInput
    } from "/resources/static/js/datatableUtils.js";
    import {deleteBusinessProvider, editBusinessProvider} from "/resources/static/js/gestionBusinessProviderModal.js";

    $(document).ready(function () {
        addEventEditAndDeleteBusinessProviderModalBtn();
        hideDatatableAndSetToggleEvent();

        const undirect_datatable = $('#businessProvider-undirect-businesses-table').DataTable({
            "processing": true,
            "serverSide": true,
            "language": frenchTransltation,
            "pagingType": "full_numbers",
            "ajax": {
                "url": "/api/business/fetch_undirect_user_businesses?id=${businessProvider.id}",
                "type": "POST",
                "dataType": "json",
                "contentType": "application/json",
                "data": function (d) {
                    return JSON.stringify(d);
                }
            },
            "order": [[5, 'desc']],
            "columnDefs": [
                {
                    "targets": 0,
                    "render":
                        function (data, type, row) {
                            const id = row[6] // id business
                            return '<a class="hrefLink" href="/affaires/afficher?id=' + id + '">' + data + '</a>';
                        }
                },
                {
                    "targets": 1,
                    "render":
                        function (data, type, row) {
                            const id = row[7] // id businessProvider
                            return '<a class="hrefLink" href="/apporteur-affaire/afficher?id=' + id + '">' + data + '</a>';
                        }
                },
                <%--                <sec:authorize access="hasRole('ADMIN')">--%>
                <%--                {  "targets": 6,--%>
                <%--                    "orderable": false,--%>
                <%--                    "render":--%>
                <%--                        function(data, type, row){--%>
                <%--                            const id = row[6] // id business--%>
                <%--                            return '<div class="d-flex justify-content-around ">' +--%>
                <%--                                '<a href="/affaires/modifier?id='+id+'" class="btn btn-primary btn-sm"><em class="fas fa-edit"></em></a> ' +--%>
                <%--                                '<a href="/affaires/supprimer?id='+id+'" class="btn btn-danger btn-sm"><em class="fas fa-trash"></em></a>' +--%>
                <%--                                '</div>';--%>
                <%--                        }--%>
                <%--                },--%>
                <%--                </sec:authorize>--%>
            ],
            "initComplete": function () {
                changeThHeaderInSearchInput.call(this, "businessProvider-undirect-businesses-table")
                setEventSearchForTheadInput(undirect_datatable)

            }
        });

        const direct_datatable = $('#businessProvider-direct-businesses-table').DataTable({
            "processing": true,
            "serverSide": true,
            "language": frenchTransltation,
            "pagingType": "full_numbers",
            "ajax": {
                "url": "/api/business/fetch_direct_user_businesses?id=${businessProvider.id}",
                "type": "POST",
                "dataType": "json",
                "contentType": "application/json",
                "data": function (d) {
                    return JSON.stringify(d);
                }
            },
            "order": [[3, 'desc']],
            "columnDefs": [
                {
                    "targets": 0,
                    "render":
                        function (data, type, row) {
                            const id = row[4]
                            return '<a class="hrefLink" href="/affaires/afficher?id=' + id + '">' + data + '</a>';
                        }
                },
                <%--                <sec:authorize access="hasRole('ADMIN')">--%>
                <%--                {  "targets": 4,--%>
                <%--                    "orderable": false,--%>
                <%--                    "render":--%>
                <%--                        function(data, type, row){--%>
                <%--                            const id = row[4]--%>
                <%--                            return '<div class="d-flex justify-content-around ">' +--%>
                <%--                                '<a href="/affaires/modifier?id='+id+'" class="btn btn-primary btn-sm"><em class="fas fa-edit"></em></a> ' +--%>
                <%--                                '<a href="/affaires/supprimer?id='+id+'" class="btn btn-danger btn-sm"><em class="fas fa-trash"></em></a>' +--%>
                <%--                                '</div>';--%>
                <%--                        }--%>
                <%--                },--%>
                <%--                </sec:authorize>--%>
            ],
            "initComplete": function () {
                changeThHeaderInSearchInput.call(this, "businessProvider-direct-businesses-table")
                setEventSearchForTheadInput(direct_datatable)
            }
        });

        hideGlobalSearchInput();
    })

    function addEventEditAndDeleteBusinessProviderModalBtn() {
        $('#openEditBusinessProviderModalBtn').on('click', function (event) {
            editBusinessProvider(event)
        });
        $('#openDeleteBusinessProviderModalBtn').on('click', function (event) {
            deleteBusinessProvider(event)
        });
    }

    function hideDatatableAndSetToggleEvent() {
        const directBusinessesContainer = $('#direct-businesses-container');
        const undirectBusinessesContainer = $('#undirect-businesses-container');

        undirectBusinessesContainer.hide();
        directBusinessesContainer.hide();

        $('#displayUndirectBusinessesBtn').change(function () {
            if ($(this).is(':checked')) {
                undirectBusinessesContainer.show();
            } else {
                undirectBusinessesContainer.hide();
            }
        });

        $('#displayDirectBusinessesBtn').change(function () {
            if ($(this).is(':checked')) {
                directBusinessesContainer.show();
            } else {
                directBusinessesContainer.hide();
            }
        });
    }
</script>

<%@ include file="../common/footer.jsp" %>