package com.moirae.rosettaflow.grpc.dynamic;

import lombok.Data;

import java.util.List;

@Data
public class DataPolicy {
    private String partyId;
    private String metadataId;
    private String metadataName;
    private Long keyColumn;
    private List<Integer> selectedColumns;
}
