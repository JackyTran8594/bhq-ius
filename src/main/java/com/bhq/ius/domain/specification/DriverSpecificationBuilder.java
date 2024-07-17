package com.bhq.ius.domain.specification;


import com.bhq.ius.domain.entity.Driver;
import com.bhq.ius.domain.specification.criteria.SearchCriteria;
import com.bhq.ius.domain.specification.criteria.SearchOperation;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class DriverSpecificationBuilder {

    private List<SearchCriteria> params;

    public DriverSpecificationBuilder() {
        this.params = new ArrayList<>();
    }

    public DriverSpecificationBuilder(final List<SearchCriteria> params) {
        this.params = params;
    }

    public DriverSpecificationBuilder with(String key, String operation, String value) {
        params.add(new SearchCriteria(key, operation, value));
        return this;
    }

    public DriverSpecificationBuilder with(SearchCriteria searchCriteria) {
        params.add(searchCriteria);
        return this;
    }

    public Specification build(Specification specification, Long courseId) {
        if(params.size() == 0) {
            return specification;
        }

        Specification result = new DriverSpecification(params.get(0), courseId);

        for(int i = 1; i < params.size(); i ++) {
            SearchCriteria searchCriteria = params.get(i);

            result = SearchOperation.getDataOption(searchCriteria.getOperation()) == SearchOperation.ALL
                    ? Specification.where(result).and(new DriverSpecification(params.get(i), courseId))
                    : Specification.where(result).or(new DriverSpecification(params.get(i), courseId));

        }

        return result;
    }




}
