package model.temporalView;

import clarifai2.dto.prediction.Concept;
import clarifai2.dto.prediction.Frame;
import model.common.Node;
import model.common.enums.SensoryEffectType;

import java.util.ArrayList;
import java.util.List;

public class SEExtractionServiceResponse {

    private List<Frame> clarifaiRawData;
    private Node applicationNode;
    private List<SensoryEffectType> selectedSensoryEffects;
    private List<SensoryEffectConcept> sensoryEffectsConceptList;
    private int effectActivationMatrix[][];

    public SEExtractionServiceResponse(Node applicationNode, List<SensoryEffectType> selectedSensoryEffects) {

        this.applicationNode = applicationNode;
        this.selectedSensoryEffects = selectedSensoryEffects;

    }

    public List<SensoryEffectConcept> getSensoryEffectsConceptList() {
        return sensoryEffectsConceptList;
    }

    public int[][] getEffectActivationMatrix() {
        return effectActivationMatrix;
    }

    public void setClarifaiRawData(List<Frame> data) {

        this.clarifaiRawData = data;

    }

    public void translateClarifaiRawDataToSE(){

        // Definindo quais tags estarão relacionadas a ativação de quais efeitos
        sensoryEffectsConceptList = new ArrayList<SensoryEffectConcept>();

        for (int i = 0; i < selectedSensoryEffects.size(); i++) {

            switch (selectedSensoryEffects.get(i)) {
                case WIND:
                    sensoryEffectsConceptList.add(new SensoryEffectConcept(SensoryEffectType.WIND, new String[] { "wind", "storm" }));
                    break;
                case WATER_SPRAYER:
                    sensoryEffectsConceptList
                            .add(new SensoryEffectConcept(SensoryEffectType.WATER_SPRAYER, new String[] { "waves" }));
                    break;
                case VIBRATION:
                    sensoryEffectsConceptList
                            .add(new SensoryEffectConcept(SensoryEffectType.VIBRATION, new String[] { "gunshot", "explosion" }));
                    break;
                case COLD:
                    sensoryEffectsConceptList.add(new SensoryEffectConcept(SensoryEffectType.COLD,
                            new String[] { "cold", "snow" }));
                    break;
                case HOT:
                    sensoryEffectsConceptList.add(new SensoryEffectConcept(SensoryEffectType.HOT,
                            new String[] { "heat", "sun" }));
                    break;
                case SCENT:
                    sensoryEffectsConceptList
                            .add(new SensoryEffectConcept(SensoryEffectType.SCENT, new String[] { "flower", "garden" }));
                    break;
                case LIGHT:
                    sensoryEffectsConceptList
                            .add(new SensoryEffectConcept(SensoryEffectType.LIGHT, new String[] { "sun", "bright", "light" }));
                    break;
                case FOG:
                    sensoryEffectsConceptList.add(new SensoryEffectConcept(SensoryEffectType.FOG, new String[] { "fog", "clouds" }));
                    break;
                case FLASH:
                    sensoryEffectsConceptList
                            .add(new SensoryEffectConcept(SensoryEffectType.FLASH, new String[] { "lightning", "gunshot" }));
                    break;
                case RAINSTORM:
                    sensoryEffectsConceptList.add(new SensoryEffectConcept(SensoryEffectType.RAINSTORM, new String[] { "storm" }));
                    break;
                default:
                    break;
            }
        }

        final int qtdSegs = applicationNode.getDuration().intValue();
        // resposta do clarifai vem por data.size(); mas ele sempre acha que tem 1 seg a
        // mais

        final int qtdSensoryEffects = sensoryEffectsConceptList.size();

        // Em ultimo caso usa isso
        effectActivationMatrix = new int[qtdSensoryEffects][qtdSegs];

        // criar uma matriz que vai conter nas suas linhas os efeitos sensoriais e nas
        // colunas os segundos de vídeo
        for (int conceptListID = 0; conceptListID < sensoryEffectsConceptList.size(); conceptListID++) {

            for (int i = 0; i < qtdSegs; i++) {
                List<Concept> finaldata = clarifaiRawData.get(i).concepts();
                // System.out.println("Segundo:"+i);

                // para cada conceito
                for (int j = 0; j < finaldata.size(); j++) {
                    Concept con = finaldata.get(j);
                    // System.out.println(j+":" + con.name());

                    // ver se o conceito é associado a um conceito de efeito sensorial conhecido
                    for (int k = 0; k < sensoryEffectsConceptList.get(conceptListID).getSecond().length; k++) {
                        // popular vetores com segundos
                        if (con.name().contains(sensoryEffectsConceptList.get(conceptListID).getSecond()[k])) {
                            effectActivationMatrix[conceptListID][i] = 1;
                            break;
                        }
                    }

                }

            }

        }

    }

}
