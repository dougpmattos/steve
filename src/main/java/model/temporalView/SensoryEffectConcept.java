package model.temporalView;

import java.util.Objects;

public class SensoryEffectConcept {

    public Object seName;
    public String[] seRelatedConcepts;

    public SensoryEffectConcept(Object seName, String[] seRelatedConcepts) {
        this.seName = seName;
        this.seRelatedConcepts = seRelatedConcepts;
    }

    public boolean same(Object seName, Object seRelatedConcepts) {
        return Objects.equals(seName, seRelatedConcepts);
    }

    public Object getFirst() {
        return seName;
    }

    public String[] getSecond() {
        return seRelatedConcepts;
    }

    public void setFirst(Object o) {
        seName = o;
    }

    public void setSecond(String[] o) {
        seRelatedConcepts = (String[]) o;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof SensoryEffectConcept))
            return false;
        SensoryEffectConcept p = (SensoryEffectConcept) obj;
        return same(p.seName, this.seName) && same(p.seRelatedConcepts, this.seRelatedConcepts);
    }

    public String toString() {
        return "Pair{" + seName + ", " + seRelatedConcepts + "}";
    }

}
