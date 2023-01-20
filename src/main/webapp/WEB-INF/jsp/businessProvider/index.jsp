<%@ page contentType="text/html;charset=UTF-8" %>

<%@ include file="../common/header.jsp" %>

<div class="container">
    <div class="card">
        <div class="card-header">
            <h3>Liste des apporteurs d'affaires</h3>
        </div>
        <div class="card-body mw-100 overflow-auto">
            <table id="businessProvider-table" class="table table-bordered table-responsive" style="width: 100%">
                <caption>Liste des apporteurs d'affaires</caption>
                <thead>
                <tr>
                    <th class="searchable">Nom</th>
                    <th class="searchable">Affilié</th>
                    <th class="searchable">Parrain</th>
                    <th class="searchable">Total commissions mois courant</th>
                    <th class="searchable">Total commissions mois n-1</th>
                    <th class="searchable">Total commissions mois n-2</th>
                    <sec:authorize access="hasRole('ADMIN')">
                        <th class="text-center">Gestion</th>
                    </sec:authorize>
                </tr>
                </thead>
            </table>
        </div>
    </div>
</div>

<%@include file="editBusinessProviderModal.jsp" %>

<script type="module">
    import {
        changeThHeaderInSearchInput,
        frenchTransltation,
        hideGlobalSearchInput,
        setEventSearchForTheadInput
    } from "/resources/static/js/datatableUtils.js";
    import {deleteBusinessProvider, editBusinessProvider} from "/resources/static/js/gestionBusinessProviderModal.js";

    $(document).ready(function () {

        const datatable = $('#businessProvider-table').DataTable({
            "processing": true,
            "serverSide": true,
            "language": frenchTransltation,
            "pagingType": "full_numbers",
            "ajax": {
                "url": "/api/businessProvider/fetch_businessProviders",
                "type": "POST",
                "dataType": "json",
                "contentType": "application/json",
                "data": function (d) {
                    return JSON.stringify(d);
                }
            },
            "order": [[0, 'asc']],
            "columnDefs": [
                {
                    "targets": 0,
                    "render":
                        function (data, type, row) {
                            const id = row[6]
                            return '<a class="hrefLink" href="/apporteur-affaire/afficher?id=' + id + '">' + data + '</a>';
                        }
                },
                {
                    "targets": 2,
                    "render":
                        function (data, type, row) {
                            const id = row[7] //id sponsor
                            return '<a class="hrefLink" href="/apporteur-affaire/afficher?id=' + id + '">' + data + '</a>';
                        }
                },
                <sec:authorize access="hasRole('ADMIN')">
                {
                    "targets": 6,
                    "orderable": false,
                    "render":
                        function (data, type, row) {
                            const id = row[6] // id businessProvider
                            return '<div class="d-flex justify-content-around">' +
                                '<a class="openEditBusinessProviderModalBtn" data-id="' + id + '"><em class="fas fa-edit"></em></a> ' +
                                '<a class="openDeleteBusinessProviderModalBtn" data-id="' + id + '"><em class="fas fa-trash"></em></a>' +
                                '</div>';
                        }
                },
                </sec:authorize>
            ],
            "initComplete": function () {
                changeThHeaderInSearchInput.call(this, "businessProvider-table")
                setEventSearchForTheadInput(datatable)
                $('.openEditBusinessProviderModalBtn').on('click', function (event) {
                    editBusinessProvider(event)
                });
                $('.openDeleteBusinessProviderModalBtn').on('click', function (event) {
                    deleteBusinessProvider(event)
                });
            }
        });

        hideGlobalSearchInput();
    })


</script>

<%@ include file="../common/footer.jsp" %>