/**
 * Open a modal for adding a new business
 * @param action the action to perform when the form because we use the same modal for adding and editing business.
 *               For adding, the action should be "creer"
 */
export function openAddBusinessModal(action){
    $("#addOrEditBusinessModalTitle").text("Ajout d'une affaire")
    $("#addOrEditBusinessForm").attr('action', action)
    $('#addOrEditBusinessModal').modal('show')
}

/**
 * Exported function to open a modal for editing a business
 * @param event the event object
 * @param action the action to perform when the form because we use the same modal for adding and editing business.
 *               For editing, the action should be "modifier"
 */
export function editBusinessModal(event, action){
    //beceause all the button are a <a> tag with an <i> tag inside, we have set the data-id attribute on the <a> tag
    const id = event.target.parentElement.dataset.id
    openEditBusinessModal(id, action)
}

/**
 * Perform the ajax call to get the business data and open the modal for editing the business
 * @param id the id of the business to edit
 * @param action the action to perform when the form because we use the same modal for adding and editing business.
 *               For editing, the action should be "modifier"
 */
function openEditBusinessModal(id, action){
    $.ajax({
        url: '/api/business/' + id,
        type: 'GET',
        success: function (business) {
            $('#businessId').val(business.id)
            $('#businessName').val(business.name)
            $('#businessAmount').val(business.amount)
            $("#addOrEditBusinessModalTitle").text("Modification d'une affaire")
            $("#addOrEditBusinessForm").attr('action', action)
            $('#addOrEditBusinessModal').modal('show')
        }
    })
}

/**
 * Exported function to open a modal for deleting a business
 * @param event the event object
 */
export function deleteBusiness(event){
    //beceause all the button are a <a> tag with an <i> tag inside, we have set the data-id attribute on the <a> tag
    const id = event.target.parentElement.dataset.id
    openDeleteBusinessModal(id)
}

/**
 * Open the modal for deleting a business
 * @param id the id of the business to delete
 */
function openDeleteBusinessModal(id){
    $('#entityId').val(id)
    $("#confirmDeleteModalTitle").text("Supprimer une affaire")
    $("#confirmDeleteModalForm").attr('action', "/affaires/supprimer")
    $('#confirmDeleteModal').modal('show')
}