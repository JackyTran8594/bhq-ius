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
    private String condition;

    public SearchCriteria(String key, String operation, Object value) {
        super();
        this.key = key;
        this.operation = operation;
        this.value = value;
    }

    public SearchCriteria(String key, String operation, Object value, String condition) {
        super();
        this.key = key;
        this.operation = operation;
        this.value = value;
        this.condition = condition;
    }
}
