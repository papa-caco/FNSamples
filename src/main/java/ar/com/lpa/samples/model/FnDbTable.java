package ar.com.lpa.samples.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FnDbTable {
    private String tableName;
    private int rowCount;
    private int securityIdCount;
}
