package ru.khrebtov.crew_raketa.entity;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import static java.util.Objects.isNull;

public class ValueSpecification implements Specification<Value> {

    private final SearchCriteria criteria;

    public ValueSpecification(SearchCriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<Value> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

        if (isNull(criteria)) {
            return null;
        }

        if (criteria.getOperation().equalsIgnoreCase("gt")) {
            return builder.greaterThan(root.<String>get(criteria.getKey()), criteria.getValue().toString());
        } else if (criteria.getOperation().equalsIgnoreCase("lt")) {
            return builder.lessThan(root.<String>get(criteria.getKey()), criteria.getValue().toString());
        } else if (criteria.getOperation().equalsIgnoreCase("in")) {
            return builder.in(root.<String>get(criteria.getKey()));
        } else if (criteria.getOperation().equalsIgnoreCase("eq")) {
            return builder.equal(root.<String>get(criteria.getKey()), criteria.getValue().toString());
        } else if (criteria.getOperation().equalsIgnoreCase("ctn")) {
            if (root.get(criteria.getKey()).getJavaType() == String.class) {
                return builder.like(root.<String>get(criteria.getKey()), "%" + criteria.getValue() + "%");
            } else {
                return builder.equal(root.get(criteria.getKey()), criteria.getValue());
            }
        }
        return null;
    }
}
