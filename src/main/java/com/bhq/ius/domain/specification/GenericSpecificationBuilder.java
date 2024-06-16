package com.thanhbinh.dms.domain.specification;


import com.thanhbinh.dms.domain.specification.criteria.SearchCriteria;
import com.thanhbinh.dms.domain.specification.criteria.SearchOperation;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class GenericSpecificationBuilder<T> {

    private final List<SearchCriteria> params;

    @Getter
    @Setter
    private Class<T> clazz;

    public GenericSpecificationBuilder() {
        this.params = new ArrayList<>();
    }

    public GenericSpecificationBuilder(final List<SearchCriteria> params) {
        this.params = params;
    }

    public GenericSpecificationBuilder with(String key, String operation, String value) {
        params.add(new SearchCriteria(key, operation, value));
        return this;
    }

    public GenericSpecificationBuilder with(SearchCriteria searchCriteria) {
        params.add(searchCriteria);
        return this;
    }

    public Specification<T> build() {
        if(params.size() == 0) {
            return null;
        }

        Specification<T> result = new GenericSpecification(params.get(0), clazz);

        for(int id = 1; id < params.size(); id ++) {
            SearchCriteria searchCriteria = params.get(id);

            result = SearchOperation.getDataOption(searchCriteria.getOperation()) == SearchOperation.ALL
                    ? Specification.where(result).and(new GenericSpecification<T>(searchCriteria, clazz))
                    : Specification.where(result).or(new GenericSpecification<T>(searchCriteria, clazz));
        }

        return result;
    }

}
