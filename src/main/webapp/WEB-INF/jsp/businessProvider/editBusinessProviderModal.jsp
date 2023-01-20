<%@ page contentType="text/html;charset=UTF-8"%>

<div class="modal fade" id="editBusinessProviderModal" tabindex="-1">
    <div class="modal-dialog modal-sm">
        <div class="modal-content" id="modalContent">
            <div class="modal-header">
                <h5 class="modal-title" id="editBusinessProviderModalHeader">Modification</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <form method="post" action="/apporteur-affaire/modifier">
                <div class="modal-body">
                    <input type="hidden" name="businessProviderId" id="businessProviderId">
                    <div class="row">
                        <div class="col-4">
                            <label for="businessProviderFirstName">Prénom</label>
                        </div>
                        <div class="col-8">
                            <input id="businessProviderFirstName" name="businessProviderFirstName" class="form-control" type="text" required>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-4">
                            <label for="businessProviderLastName">Nom</label>
                        </div>
                        <div class="col-8">
                            <input id="businessProviderLastName" name="businessProviderLastName" class="form-control" type="text" required>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-4">
                            <label for="businessProviderSponsor">Parrain</label>
                        </div>
                        <div class="col-8">
                            <select id="businessProviderSponsor" name="businessProviderSponsor" class="form-select"></select>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Fermer</button>
                    <button type="submit" class="btn btn-success">Ajouter</button>
                </div>
            </form>
        </div>
    </div>
</div>