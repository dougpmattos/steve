package model.spatialView.sensoryEffect.rigidBodyMotion;

public class TermReference {

    private String reference;

    public String getReference() {
        return reference;
    }

    /**
     * The termReference datatype describes a reference to a term. It supports two forms for
     * term referencing: (1) a URI reference
     * and (2) an abbreviated form for referencing terms in classification schemes
     * that have been assigned aliases.
     *
     * @param reference must be in the format value=":[^:]+:[^:]+"/>
     */
    public void setReference(String reference) {
        this.reference = reference;
    }
}
