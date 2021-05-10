package gateway;

import clarifai2.api.ClarifaiBuilder;
import clarifai2.api.ClarifaiClient;
import clarifai2.api.ClarifaiResponse;
import clarifai2.api.request.model.PredictRequest;
import clarifai2.dto.input.ClarifaiInput;
import clarifai2.dto.model.Model;
import clarifai2.dto.model.output.ClarifaiOutput;
import clarifai2.dto.prediction.Frame;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import model.common.MediaNode;
import model.common.Node;
import model.common.enums.SensoryEffectType;
import model.temporalView.ClarifaiError;
import model.temporalView.SEExtractionServiceResponse;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;


public class SensoryEffectExtractionService extends Service<SEExtractionServiceResponse> {

    Node applicationNode;
    SEExtractionServiceResponse sEExtractionServiceResponse;

    public SensoryEffectExtractionService(Node applicationNode, List<SensoryEffectType> selectedSensoryEffects){

        this.applicationNode = applicationNode;
        sEExtractionServiceResponse = new SEExtractionServiceResponse(applicationNode, selectedSensoryEffects);

    }

    @Override
    protected Task<SEExtractionServiceResponse> createTask() {

        return new Task<SEExtractionServiceResponse>() {

            protected SEExtractionServiceResponse call() {

                String myMedia = ((MediaNode) applicationNode).getPath();
                final File currFile = new File(myMedia);

                //TODO put apiKey in an external config file
                ClarifaiClient client = null;

//                //INFO Raphael's Key
//                client = new ClarifaiBuilder("c6d097d9aeb64d9790b94fbb69c17ae4").buildSync();
                //INFO Douglas's Key
                //client = new ClarifaiBuilder("07619fc6cfd24fe88a902dc4197481a3").buildSync();

                client = new ClarifaiBuilder("c6d097d9aeb64d9790b94fbb69c17ae4")
                        .client(new OkHttpClient.Builder()
                                .connectTimeout(60, TimeUnit.SECONDS)
                                .readTimeout(60, TimeUnit.SECONDS)
                                .writeTimeout(60, TimeUnit.SECONDS)
                                .build()
                        )
                        .buildSync();

                if(client != null){
                    Model<Frame> generalVideoModel = client.getDefaultModels().generalVideoModel();

                    PredictRequest<Frame> videoRequest = generalVideoModel.predict()
                            .withInputs(ClarifaiInput.forVideo(currFile));

                    ClarifaiResponse<List<ClarifaiOutput<Frame>>> clarifaiResponse = videoRequest.executeSync();

                    if(clarifaiResponse.isSuccessful()){

                        List<ClarifaiOutput<Frame>> videoResults = clarifaiResponse.get();

                        List<Frame> data = videoResults.get(0).data();

                        sEExtractionServiceResponse.setClarifaiRawData(data);
                        sEExtractionServiceResponse.translateClarifaiRawDataToSE();

                    }else{

                        ClarifaiError clarifaiError = new ClarifaiError();
                        clarifaiError.setDescription(clarifaiResponse.getStatus().description());
                        if(clarifaiResponse.getStatus().errorDetails() == null){
                            String outputs = clarifaiResponse.rawBody().substring(clarifaiResponse.rawBody().indexOf("outputs"));
                            String description = outputs.substring(outputs.indexOf("description"));
                            String descriptionValue = description.substring(description.indexOf(":")+1, description.indexOf(","));
                            descriptionValue = descriptionValue.replace("\"", "");
                            clarifaiError.setErrorDetails(descriptionValue);
                        }else{
                            clarifaiError.setErrorDetails(clarifaiResponse.getStatus().errorDetails());
                        }

                        sEExtractionServiceResponse.setClarifaiError(clarifaiError);

                    }

                }

                return sEExtractionServiceResponse;

            }
        };
    }
}
