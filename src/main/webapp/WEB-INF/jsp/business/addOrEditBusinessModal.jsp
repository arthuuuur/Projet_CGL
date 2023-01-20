<%@ page contentType="text/html;charset=UTF-8"%>

<div class="modal fade" id="addOrEditBusinessModal" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-content" id="modalContent">
            <div class="modal-header">
                <h5 class="modal-title" id="addOrEditBusinessModalTitle">Ajout d'une affaire</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <form id="addOrEditBusinessForm" method="post" action="">
                <input id="businessId" name="businessId" type="hidden" value=""/>
                <div class="modal-body">
                    <div class="row">
                        <div class="col-4">
                            <label for="businessName" class="form-label">Nom</label>
                        </div>
                        <div class="col-8">
                            <input type="text" class="form-control" id="businessName" name="businessName" required>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-4">
                            <label for="businessAmount" class="form-label">Montant</label>
                        </div>
                        <div class="col-8">
                            <input type="number" class="form-control" id="businessAmount" name="businessAmount" required>
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
