package gateway;

import clarifai2.api.ClarifaiBuilder;
import clarifai2.api.ClarifaiClient;
import clarifai2.api.request.model.PredictRequest;
import clarifai2.dto.input.ClarifaiInput;
import clarifai2.dto.model.Model;
import clarifai2.dto.model.output.ClarifaiOutput;
import clarifai2.dto.prediction.Frame;
import model.common.MediaNode;
import model.common.Node;
import model.common.enums.SensoryEffectType;
import model.temporalView.SEExtractionServiceResponse;

import java.io.File;
import java.util.List;

public class SensoryEffectExtractionService implements Runnable{

    model.common.Node applicationNode;
    SEExtractionServiceResponse sEExtractionServiceResponse;

    public SensoryEffectExtractionService(Node applicationNode, List<SensoryEffectType> selectedSensoryEffects){

        this.applicationNode = applicationNode;
        sEExtractionServiceResponse = new SEExtractionServiceResponse(applicationNode, selectedSensoryEffects);

    }

    @Override
    public void run() {

        String myMedia = ((MediaNode) applicationNode).getPath();
        final File currFile = new File(myMedia);

        //TODO put apiKey in an external config file
        final ClarifaiClient client = new ClarifaiBuilder("c6d097d9aeb64d9790b94fbb69c17ae4").buildSync();

        Model<Frame> generalVideoModel = client.getDefaultModels().generalVideoModel();

        PredictRequest<Frame> videoRequest = generalVideoModel.predict()
                .withInputs(ClarifaiInput.forVideo(currFile));
        List<ClarifaiOutput<Frame>> videoResults = videoRequest.executeSync().get();

        List<Frame> data = videoResults.get(0).data();

        sEExtractionServiceResponse.setClarifaiRawData(data);
        sEExtractionServiceResponse.translateClarifaiRawDataToSE();

    }

    public SEExtractionServiceResponse getSEExtractionServiceResponse() {
        return sEExtractionServiceResponse;
    }

}
