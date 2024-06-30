package com.bhq.ius.domain.specification;


import com.bhq.ius.domain.specification.criteria.SearchCriteria;
import com.bhq.ius.domain.specification.criteria.SearchOperation;
import com.bhq.ius.utils.DataUtil;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.regex.Pattern;


public class GenericSpecification<T> implements Specification<T> {

    private final SearchCriteria searchCriteria;
    public GenericSpecification (final SearchCriteria searchCriteria, Class<T> clazz) {
        super();
        String dataType = findDataType(searchCriteria.getKey(), clazz);
        if (dataType.equals("Integer")) {
            searchCriteria.setValue(DataUtil.convertToDataType(Integer.class, searchCriteria.getValue().toString()));
            searchCriteria.setDataType(Integer.class);
        }
        if (dataType.equals("Long")) {
            searchCriteria.setValue(DataUtil.convertToDataType(Long.class, searchCriteria.getValue().toString()));
            searchCriteria.setDataType(Long.class);

        }
        if (dataType.equals("Double")) {
            searchCriteria.setValue(DataUtil.convertToDataType(Double.class, searchCriteria.getValue().toString()));
            searchCriteria.setDataType(Double.class);

        }
        if (dataType.equals("String")) {
//            searchCriteria.setValue(DataUtils.convertToDataType(String.class, searchCriteria.getValue().toString()));
        }
        if (dataType.equals("Float")) {
            searchCriteria.setValue(DataUtil.convertToDataType(Float.class, searchCriteria.getValue().toString()));
            searchCriteria.setDataType(Float.class);

        }
        this.searchCriteria = searchCriteria;
    }

    @Override
    public Specification<T> and(Specification<T> other) {
        return Specification.super.and(other);
    }

    @Override
    public Specification<T> or(Specification<T> other) {
        return Specification.super.or(other);
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        Object strToSearch = searchCriteria.getValue();
        switch (Objects.requireNonNull(SearchOperation.getSimpleOperation(searchCriteria.getOperation()))) {
            case CONTAINS:
                return criteriaBuilder.like(criteriaBuilder.lower(root.get(searchCriteria.getKey())), "%" + strToSearch.toString().trim() + "%");
            case DOES_NOT_CONTAIN:
                return criteriaBuilder.notLike(criteriaBuilder.lower(root.get(searchCriteria.getKey())), "%" + strToSearch.toString().trim() + "%");

            case BEGINS_WITH:

                return criteriaBuilder.like(criteriaBuilder.lower(root.get(searchCriteria.getKey())), strToSearch.toString().trim().toLowerCase() + "%");
            case DOES_NOT_BEGIN_WITH:
                return criteriaBuilder.notLike(criteriaBuilder.lower(root.get(searchCriteria.getKey())), strToSearch.toString().trim().toLowerCase() + "%");
            case ENDS_WITH:
                return criteriaBuilder.like(criteriaBuilder.lower(root.get(searchCriteria.getKey())), "%" + strToSearch.toString().trim());
            case DOES_NOT_END_WITH:
                return criteriaBuilder.notLike(criteriaBuilder.lower(root.get(searchCriteria.getKey())), "%" + strToSearch.toString().trim());
            case EQUAL:
                if (strToSearch instanceof String) {
                    return criteriaBuilder.equal(criteriaBuilder.lower(root.<String>get(searchCriteria.getKey())), strToSearch.toString().trim());
                } else {
                    return criteriaBuilder.equal(root.<Object>get(searchCriteria.getKey()), strToSearch);
                }

            case NOT_EQUAL:
                if (strToSearch instanceof String) {
                    return criteriaBuilder.notEqual(criteriaBuilder.lower(root.get(searchCriteria.getKey())), strToSearch.toString().trim());
                } else {
                    return criteriaBuilder.notEqual(root.<Object>get(searchCriteria.getKey()), strToSearch);
                }
            case NUL:
                return criteriaBuilder.isNull(criteriaBuilder.lower(root.get(searchCriteria.getKey())));
            case NOT_NULL:
                return criteriaBuilder.isNotNull(criteriaBuilder.lower(root.get(searchCriteria.getKey())));
            case GREATER_THAN:
                return criteriaBuilder.greaterThan(criteriaBuilder.lower(root.get(searchCriteria.getKey())), strToSearch.toString().trim());

            case GREATER_THAN_EQUAL:
                return criteriaBuilder.greaterThanOrEqualTo(root.<String>get(searchCriteria.getKey()), searchCriteria.getValue().toString().trim());
            case LESS_THAN:
                return criteriaBuilder.lessThan(root.<String>get(searchCriteria.getKey()), searchCriteria.getValue().toString().trim());
            case LESS_THAN_EQUAL:

                return criteriaBuilder.lessThanOrEqualTo(root.<String>get(searchCriteria.getKey()), searchCriteria.getValue().toString().trim());
        }
        return null;
    }

    /**
     * find datatype with fieldname
     *
     * @param fieldName
     * @return
     */
    public String findDataType(String fieldName, Class<T> clazz) {
        String dataType = null;
        Field[] fields = clazz.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            if(field.getType().equals(Enum.class)) {
                dataType = "String";
            } else {
                if (fieldName.equals(field.getName())) {
                    String[] str = field.getType().getTypeName().split(Pattern.quote("."));
                    // str[2] = dataType
                    dataType = str[2];
                    break;
                }
            }
        }
        return dataType;
    }

}
