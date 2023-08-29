package co.com.bancolombia.signdocument.txt.iterations;

import co.com.bancolombia.commonsvnt.usecase.util.constants.Numbers;
import co.com.bancolombia.model.signdocument.SDRequestTxt;
import co.com.bancolombia.model.signdocument.TxtConstruction;
import co.com.bancolombia.model.signdocument.gateways.TxtConstructionRepository;
import lombok.RequiredArgsConstructor;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.EMPTY;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.SPACE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsTwo.DATE;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsTwo.DOC_NUMBER;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsTwo.DOC_TYPE_2;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsTwo.ITERATION;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsTwo.SESSION_ID;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsTwo.SIGN_IP;
import static co.com.bancolombia.commonsvnt.usecase.util.constants.ConstantsTwo.SOL_ID;

@RequiredArgsConstructor
public class SDTxtUtilUseCase {

    private final TxtConstructionRepository txtConstructionRepository;

    public List<TxtConstruction> findByAllIteration(String iteration) {
        return txtConstructionRepository.findByAllIteration(iteration);
    }

    public void addIteration(BufferedWriter bw, SDRequestTxt requestTxt, String dataIteration, int numberIteration)
            throws IOException {
        List<TxtConstruction> txtConstructions = findByAllIteration(dataIteration);
        for (TxtConstruction construction : txtConstructions) {
            if (construction.getIterationOrder().equals(Numbers.TWO.getNumber())){
                addLine(bw, SESSION_ID.concat(SPACE).concat(requestTxt.getMessageId()), EMPTY);
                addLine(bw, SOL_ID.concat(SPACE).concat(requestTxt.getAcquisitionId()), EMPTY);
                addLine(bw, DOC_TYPE_2.concat(SPACE).concat(requestTxt.getDocumentType()), EMPTY);
                addLine(bw, DOC_NUMBER.concat(":").concat(SPACE).concat(requestTxt.getDocumentNumber()), EMPTY);
                addLine(bw, SIGN_IP.concat(SPACE).concat(requestTxt.getIp()), EMPTY);
                addLine(bw, DATE.concat(SPACE).concat(requestTxt.getDateRequest()), EMPTY);
            }
            String value = construction.getIterationOrder().equals(Numbers.ONE.getNumber())?
                    Numbers.findByNumber(numberIteration).concat(SPACE).concat(ITERATION).concat(":").concat(SPACE):"";
            addLine(bw, value.concat(construction.getValue()), EMPTY);
        }
        bw.newLine();
    }

    public void addLine(BufferedWriter bw, String title, String value) throws IOException {
        String space = !EMPTY.equals(value)?SPACE:EMPTY;
        bw.write(title.concat(space).concat(value));
        bw.newLine();
    }
}
