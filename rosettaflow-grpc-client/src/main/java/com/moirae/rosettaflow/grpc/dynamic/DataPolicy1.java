package com.moirae.rosettaflow.grpc.dynamic;

import lombok.Data;

import java.util.List;

@Data
public class DataPolicy1 {
    private String partyId;
    private String metadataId;
    private String metadataName;
    private Integer keyColumn;
    private List<Integer> selectedColumns;
}
