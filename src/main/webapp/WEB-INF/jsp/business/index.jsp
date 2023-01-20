<%@ page contentType="text/html;charset=UTF-8" %>

<%@ include file="../common/header.jsp" %>

<div class="container">
    <div class="card">
        <div class="card-header d-flex justify-content-between">
            <h3>Liste des affaires</h3>
            <a type="button" class="btn btn-primary" id="openAddBusinessModalBtn">Ajouter</a>

        </div>
        <div class="card-body mw-100 overflow-auto">
            <table id="business-table" class="table table-bordered table-responsive" style="width: 100%">
                <caption>Liste des affaires</caption>
                <thead>
                <tr>
                    <th class="searchable">Nom</th>
                    <th class="searchable">Déposeur</th>
                    <th class="searchable">Montant</th>
                    <th class="searchable">Comission</th>
                    <th class="searchable">Date</th>
                    <sec:authorize access="hasRole('ADMIN')">
                        <th class="text-center">Gestion</th>
                    </sec:authorize>
                </tr>
                </thead>
            </table>
        </div>
    </div>
</div>

<%@ include file="addOrEditBusinessModal.jsp" %>

<script type="module">
    import {
        changeThHeaderInSearchInput,
        frenchTransltation,
        hideGlobalSearchInput,
        setEventSearchForTheadInput
    } from "/resources/static/js/datatableUtils.js";
    import {
        deleteBusiness,
        editBusinessModal,
        openAddBusinessModal
    } from "/resources/static/js/gestionBusinessModal.js";

    $(document).ready(function () {

        $("#openAddBusinessModalBtn").on("click", function () {
            openAddBusinessModal("creer");
        });

        const datatable = $('#business-table').DataTable({
            "processing": true,
            "serverSide": true,
            "language": frenchTransltation,
            "pagingType": "full_numbers",
            "ajax": {
                "url": "/api/business/fetch_businesses",
                "type": "POST",
                "dataType": "json",
                "contentType": "application/json",
                "data": function (d) {
                    return JSON.stringify(d);
                }
            },
            "order": [[4, 'desc']],
            "columnDefs": [
                {
                    "targets": 0,
                    "render":
                        function (data, type, row) {
                            const id = row[5] // id business
                            return '<a class="hrefLink" href="/affaires/afficher?id=' + id + '">' + data + '</a>';
                        }
                },
                {
                    "targets": 1,
                    "render":
                        function (data, type, row) {
                            const id = row[6] // id businessProvider
                            return '<a class="hrefLink" href="/apporteur-affaire/afficher?id=' + id + '">' + data + '</a>';
                        }
                },
                <sec:authorize access="hasRole('ADMIN')">
                {
                    "targets": 5,
                    "orderable": false,
                    "render":
                        function (data, type, row) {
                            const id = row[5]
                            return '<div class="d-flex justify-content-around ">' +
                                '<a data-id="' + id + '" class="openEditBusinessModalBtn"><em class="fas fa-edit"></em></a> ' +
                                '<a data-id="' + id + '" class="openDeleteBusinessModalBtn"><em class="fas fa-trash"></em></a>' +
                                '</div>';
                        }
                },
                </sec:authorize>
            ],
            "initComplete": function () {
                changeThHeaderInSearchInput.call(this, "business-table")
                setEventSearchForTheadInput(datatable)
                $(".openEditBusinessModalBtn").on("click", function (event) {
                    editBusinessModal(event, "modifier");
                });
                $(".openDeleteBusinessModalBtn").on("click", function (event) {
                    deleteBusiness(event);
                });
            }
        });

        hideGlobalSearchInput(datatable);
    })
</script>

<%@ include file="../common/footer.jsp" %>