package com.bhq.ius.domain.specification.criteria;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SearchCriteria {
    private String key;
    private String operation;
    private Object value;
    private Object dataType;

    public SearchCriteria(String key, String operation, Object value) {
        super();
        this.key = key;
        this.operation = operation;
        this.value = value;
    }
}
