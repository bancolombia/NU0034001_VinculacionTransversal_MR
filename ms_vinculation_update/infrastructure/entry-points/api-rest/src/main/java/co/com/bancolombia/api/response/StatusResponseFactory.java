package co.com.bancolombia.api.response;

import co.com.bancolombia.api.model.validatestatus.ValidateStatusClausesResponseData;
import co.com.bancolombia.api.model.validatestatus.ValidateStatusResponse;
import co.com.bancolombia.api.model.validatestatus.ValidateStatusResponseData;
import co.com.bancolombia.api.model.validatestatus.ValidateStatusResponseDataStartList;
import co.com.bancolombia.commonsvnt.api.model.util.MetaResponse;
import co.com.bancolombia.commonsvnt.api.model.util.UserInfoRequest;
import co.com.bancolombia.commonsvnt.model.activity.Activity;
import co.com.bancolombia.commonsvnt.model.startlist.StartList;
import co.com.bancolombia.commonsvnt.usecase.util.constants.Numbers;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.DATE_FORMAT_TIME_MILLISECONDS;

public class StatusResponseFactory {

    private StatusResponseFactory() {

    }

    public static ValidateStatusResponse buildValidateStatusResponse(UserInfoRequest request, List<StartList> ret) {
        List<ValidateStatusResponseDataStartList> startList = ret.stream()
                .map(r -> ValidateStatusResponseDataStartList.builder()
                        .acquisitionId(r.getAcquisitionId()).documentType(r.getDocumentType())
                        .documentNumber(r.getDocumentNumber()).registrationDate(r.getRegistrationDate())
                        .codeStateAcquisition(r.getCodeStateAcquisition())
                        .nameStateAcquisition(r.getNameStateAcquisition())
                        .clausesList(r.getClauseAcquisitionCheckLists().stream().map(
                                c -> {String sClause;
                                    if (c.getDateAcceptClause() != null && c.isAcceptClause()) {
                                        sClause = Numbers.ONE.getNumber();
                                    } else {
                                        sClause = (c.getDateAcceptClause() == null && !c.isAcceptClause())
                                                ? Numbers.TWO.getNumber() : Numbers.ZERO.getNumber();
                                    }
                                    return ValidateStatusClausesResponseData.builder()
                                            .clauseCode(c.getMatrixTypeAcquisitionClause().getClause().getCode())
                                            .clauseName(c.getMatrixTypeAcquisitionClause().getClause().getName())
                                            .acceptanceDateClauses(c.getDateAcceptClause() == null ? null :
                                                    new SimpleDateFormat(DATE_FORMAT_TIME_MILLISECONDS).format(
                                                            c.getDateAcceptClause())).acceptClauses(sClause).build();
                                }).collect(Collectors.toList()))
                        .activitiesList(r.getActivitiesList().stream().map(a -> Activity.builder()
                                .idActivity(a.getIdActivity()).nameActivity(a.getNameActivity())
                                .codeStateActivity(a.getCodeStateActivity()).nameStateActivity(a.getNameStateActivity())
                                .build()).collect(Collectors.toList())).build()).collect(Collectors.toList());
        ValidateStatusResponseData data = ValidateStatusResponseData.builder().startList(startList).build();
        return ValidateStatusResponse.builder().data(data).meta(MetaResponse.fromMeta(request.getMeta())).build();
    }
}
