package co.com.bancolombia.generatepdf;

import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import co.com.bancolombia.commonsvnt.model.checklist.CheckList;
import co.com.bancolombia.commonsvnt.model.statestep.StateStep;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.CatalogReply;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.ChecklistReply;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.GeographicReply;
import co.com.bancolombia.rabbit.VinculationUpdateUseCase;
import lombok.RequiredArgsConstructor;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.EMPTY;

@RequiredArgsConstructor
public class GeneratePdfUtilOneUseCaseImpl implements GeneratePdfUtilOneUseCase {

    private final VinculationUpdateUseCase vinculationUpdateUseCase;

    private static final int ONE = 1;
    private static final int TWO = 2;
    private static final int THREE = 3;
    private static final int FOUR = 4;

    @Override
    public String getCatalogName(String parent, String code) {
        CatalogReply reply = vinculationUpdateUseCase.findCatalog(code, parent);
        return reply.isValid() ? reply.getName() : EMPTY;
    }

    @Override
    public String getCatalogCode(String parent, String code) {
        CatalogReply reply = vinculationUpdateUseCase.findCatalog(code, parent);
        return reply.isValid() ? reply.getCode() : EMPTY;
    }

    @Override
    public String getGeographicName(String code, String division, int type) {
        String result = "";

        if (code == null || code.isEmpty()) {
            return result;
        }

        GeographicReply geographicReply;
        switch (type) {
            case ONE:
                geographicReply = getGeographicCatalog(code, null, division);
                result = geographicReply.getNameCity();
                break;
            case TWO:
                geographicReply = getGeographicCatalog(null, code, null);
                result = geographicReply.getNameDepartment();
                break;
            case THREE:
                geographicReply = getGeographicCatalog(null, null, code);
                result = geographicReply.getNameCountry();
                break;
            case FOUR:
                geographicReply = getGeographicCatalog(code, division, null);
                result = geographicReply.getNameCity();
                break;
            default:
                result = "";
                break;
        }
        return result == null ? "" : result;
    }

    @Override
    public CheckList checkListFindByOperation(Acquisition acquisition, String operation) {
        ChecklistReply reply = vinculationUpdateUseCase.checkListStatus(acquisition.getId(), operation);
        return CheckList.builder().state(StateStep.builder().code(reply.getStateOperation()).build()).build();
    }

    private GeographicReply getGeographicCatalog(String city, String department, String country) {
        return vinculationUpdateUseCase.findGeographic(city, department, country);
    }
}
