package ar.com.lpa.samples.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FnOwner {
    private String objectId;
    private FnObjectType fnObjectType;
    private String sourceOwner;
    private String destinationOwner;
    private char status;

    public FnOwner(FnObjectType type, String objectId, String owner) {
        this.fnObjectType = type;
        this.objectId = objectId;
        this.sourceOwner = owner;
        this.status = 'N';
    }
}
