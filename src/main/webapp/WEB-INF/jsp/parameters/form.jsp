<%@ page contentType="text/html;charset=UTF-8" %>

<%@ include file="../common/header.jsp" %>

<div class="container">
    <div class="card mx-auto mw-75">
        <div class="card-header"><strong>Paramètres</strong></div>
        <div class="card-body">
            <form:form method="post" modelAttribute="parameters"> <form:hidden path="id"/>

                <div class="row mb-1">
                    <div class="col-8">
                        <form:label
                                path="minimumBusinessToBeAffiliated">Nombre minimum d'affaire à apporter pour etre affilié</form:label>
                    </div>
                    <div class="col-4">
                        <form:input path="minimumBusinessToBeAffiliated" type="number" class="form-control w-50"
                                    required="required"/> <form:errors path="minimumBusinessToBeAffiliated"
                                                                       cssClass="text-warning"/>
                    </div>
                </div>

                <div class="row mb-1">
                    <div class="col-8">
                            <%--                    avant qu'un apporteur d'affaire ne soit plus affilié s'il n'apporte pas d'affaire--%>
                        <form:label path="affiliatedDay">Nombre de jours <em
                                class="fa-solid fa-circle-info"></em></form:label>
                    </div>
                    <div class="col-4">
                        <form:input path="affiliatedDay" type="number" class="form-control w-50"
                                    required="required"/> <form:errors path="affiliatedDay" cssClass="text-warning"/>
                    </div>
                </div>

                <div class="row mb-1">
                    <div class="col-8">
                        <form:label path="percentageCommission">Pourcentage de comission</form:label>
                    </div>
                    <div class="col-4">
                        <form:input path="percentageCommission" type="number" step=".01" class="form-control w-50"
                                    required="required"/> <form:errors path="percentageCommission"
                                                                       cssClass="text-warning"/>
                    </div>
                </div>

                <div class="row mb-1">
                    <div class="col-8">
                        <form:label path="maxSuccessiveSponsor">Nombre maximum de parrain succéssif</form:label>
                    </div>
                    <div class="col">
                        <form:input path="maxSuccessiveSponsor" type="text" class="form-control-plaintext fw-bold w-50"
                                    readonly="true"
                                    required="required" id="maxSuccessiveSponsor"/> <form:errors
                            path="maxSuccessiveSponsor" cssClass="text-warning"/>
                    </div>
                    <div class="col-3">
                        <button type="button" class="btn btn-sm btn-primary" onclick="increaseMaxSuccessiveSponsor()">
                            <em class="fa fa-plus"></em>
                        </button>
                        <button type="button" class="btn btn-sm btn-primary" id="decreaseMaxSuccessiveSponsorBtn"
                                onclick="decreaseMaxSuccessiveSponsor()">
                            <em class="fa fa-minus"></em>
                        </button>
                    </div>
                </div>
                <hr>
                <div id="divPercentageCommissionSponsors"
                     data-listPercentage="${parameters.percentageCommissionSponsor}">

                </div>


                <button type="submit" class="btn btn-success mt-2">Sauvegarder</button>
            </form:form>
        </div>
    </div>
</div>
<%@ include file="../common/footer.jsp" %>

<script>

    let currentNumber = 0;
    let maxNumber = 0;
    const maxSuccessiveSponsor = $('#maxSuccessiveSponsor');
    const decreaseMaxSuccessiveSponsorBtn = $('#decreaseMaxSuccessiveSponsorBtn');
    const divPercentageCommissionSponsors = $('#divPercentageCommissionSponsors');

    const increaseMaxSuccessiveSponsor = () => {
        currentNumber = parseInt(maxSuccessiveSponsor.val());
        currentNumber++;
        maxSuccessiveSponsor.val(currentNumber);
        addPercentageCommissionSponsor(currentNumber);
        if (currentNumber > 1) {
            decreaseMaxSuccessiveSponsorBtn.prop('disabled', false);
        }
    }

    const decreaseMaxSuccessiveSponsor = () => {
        currentNumber = parseInt(maxSuccessiveSponsor.val());
        if (currentNumber > 1) {
            currentNumber--;
            maxSuccessiveSponsor.val(currentNumber);
            removePercentageCommissionSponsor(currentNumber);
            if (currentNumber === 1) {
                decreaseMaxSuccessiveSponsorBtn.prop('disabled', true);
            }
        }
    }

    const addPercentageCommissionSponsor = () => {
        const length = divPercentageCommissionSponsors.find('div.row').length;
        const input = $('<div class="col-8"><input type="number" step=".01" class="form-control w-25" name="percentageCommissionSponsor" value="0" /></div>');
        const label = $('<label class="col-4">Commission pour le parrain n°' + (length + 1) + '</label></div>');
        const div = $('<div class="row"></div>');
        div.append(label);
        div.append(input);
        divPercentageCommissionSponsors.append(div);
    }

    const removePercentageCommissionSponsor = () => {
        divPercentageCommissionSponsors.find('div.row').last().remove();
    }

    $(document).ready(function () {
        const listPercentageCommissionSponsor = divPercentageCommissionSponsors.data('listpercentage');
        listPercentageCommissionSponsor.forEach((value, index) => {
            const input = $('<div class="col-8"><input type="number" step=".01" class="form-control w-25" name="percentageCommissionSponsor" value="' + value + '" /></div>');
            const label = $('<div class="col-4"><label>Commission pour le parrain n°' + (index + 1) + '</label></div>');
            const div = $('<div class="row mb-1"></div>');
            div.append(label);
            div.append(input);
            divPercentageCommissionSponsors.append(div);
        });
    });
</script>