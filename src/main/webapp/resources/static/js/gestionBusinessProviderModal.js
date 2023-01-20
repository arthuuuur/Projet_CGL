/**
 * Exported function to open a modal for editing a business provider
 * @param event
 * @returns {Promise<void>}
 */
export async function editBusinessProvider(event){
    //beceause all the button are a <a> tag with an <i> tag inside, we have set the data-id attribute on the <a> tag
    const id = event.target.parentElement.dataset.id
    const availableSponsors = await fetchAllAvailableSponsors(id)
    openEditBusinessProviderModal(id, availableSponsors)
}

/**
 * Fetch all the available sponsors for a business provider to only display the ones that can be selected as a sponsor
 * @param id
 * @returns {Promise<{getAllResponseHeaders: function(): *|null, abort: function(*): this, setRequestHeader: function(*, *): this, readyState: number, getResponseHeader: function(*): null|*, overrideMimeType: function(*): this, statusCode: function(*): this}|jQuery>}
 */
async function fetchAllAvailableSponsors(id){
    return $.ajax({
        url: "/api/businessProvider/fetch_all_available_sponsors/" + id,
        type: "GET",
    });
}

/**
 * Perform the ajax call to get the business provider data and open the modal for editing the business provider
 * @param id the id of the business provider to edit
 * @param availableSponsors the list of available sponsors
 */
function openEditBusinessProviderModal(id, availableSponsors){
    $.ajax({
        url: '/api/businessProvider/' + id,
        type: 'GET',
        success: function (businessProvider) {
            $('#businessProviderId').val(businessProvider.id)
            $('#businessProviderFirstName').val(businessProvider.firstName)
            $('#businessProviderLastName').val(businessProvider.lastName)
            const select = $('#businessProviderSponsor')
            select.empty()
            select.append('<option value=""> Parrain</option>')
            availableSponsors.forEach(function (sponsor) {
                if(businessProvider.sponsor !== null && sponsor.id === businessProvider.sponsor.id){
                    select.append('<option value="'+sponsor.id+'" selected>'+sponsor.firstName+' '+sponsor.lastName+'</option>')
                }else{
                    select.append('<option value="'+sponsor.id+'">'+sponsor.firstName+' '+sponsor.lastName+'</option>')
                }
            })
            $('#editBusinessProviderModal').modal('show')
        }
    })
}

/**
 * Exported function to open a modal for deleting a business provider
 * @param event the event object
 */
export function deleteBusinessProvider(event){
    const id = event.target.parentElement.dataset.id
    openDeleteBusinessProviderModal(id)
}

/**
 * Open the modal for deleting a business provider
 * @param id the id of the business provider to delete
 */
function openDeleteBusinessProviderModal(id){
    $('#entityId').val(id)
    $("#confirmDeleteModalTitle").text("Supprimer un apporteur d'affaire")
    $("#confirmDeleteModalForm").attr('action', "/apporteur-affaire/supprimer")
    $('#confirmDeleteModal').modal('show')
}