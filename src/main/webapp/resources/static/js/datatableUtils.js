/**
 * Frenche translation of the DataTables plugin.
 */
export const frenchTransltation = {
    "lengthMenu": "Afficher _MENU_ ligne par page",
    "zeroRecords": "0 resultats trouves",
    "info": "Affichage de la page _PAGE_ sur _PAGES_",
    "infoEmpty": "0 enregistrement",
    "infoFiltered": "(filtree de _MAX_ enregistrement en total)",
    "search": "Chercher:",
    "paginate": {
        "first": "Premiere",
        "last": "Derniere",
        "next": "Suivante",
        "previous": "Precedente"
    },
    "loadingRecords": "Chargement...",
    "processing": "",
}

/**
 * Hide the global search input for any datatable
 */
export function hideGlobalSearchInput(){
    $(".dataTables_filter").hide();
}

/**
 * Change all datatable header with "searchable" class to an input
 * @param datatableId id of the datatable
 */
export function changeThHeaderInSearchInput(datatableId){
    $('#' + datatableId + ' thead th.searchable').each(function(){
        const title = $(this).text();
        $(this).html('<input type="text" placeholder="'+title+'" style="width: 100%;" class="text-center form-control"/>'); // style="width: 100%;" is important
    });
}

/**
 * Add event to each input of the datatable header to perfom column search with a
 * debounce feature to avoid multiple calls before user stop typing in search input
 * @param datatable the datatable object
 * @param delay the delay in ms
 * @param timer the timer object
 */
export function setEventSearchForTheadInput(datatable, delay = 500, timer = null){
    datatable.columns().every(function(){
        const column = this;
        $('input', this.header())
            .on('click', function(event) { // to prevent ordering column when clicking on input for search
                event.stopPropagation();
            })
            .on('keyup change', function() {
                clearTimeout(timer)
                timer = setTimeout(() => { column.search(this.value).draw(); }, delay)
            })
    });
}