package ar.com.lpa.samples.repository;

import ar.com.lpa.samples.model.FnDbTable;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class FnDbTableRepo {
    private final List<FnDbTable> FnDbTables = new ArrayList<>();
}


