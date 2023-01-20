<%@ page contentType="text/html;charset=UTF-8"%>
<div class="modal fade" id="confirmDeleteModal" tabindex="-1">
    <div class="modal-dialog modal-sm">
        <div class="modal-content" id="modalContent">
            <div class="modal-header">
                <h5 class="modal-title" id="confirmDeleteModalTitle">Suppression</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <form id="confirmDeleteModalForm" method="post" action="">
                <div class="modal-body">
                    <input type="hidden" name="entityId" id="entityId">
                    <p>Êtes-vous sûr de vouloir supprimer cet élément ?</p>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Fermer</button>
                    <button type="submit" class="btn btn-danger">Supprimer</button>
                </div>
            </form>
        </div>
    </div>
</div>