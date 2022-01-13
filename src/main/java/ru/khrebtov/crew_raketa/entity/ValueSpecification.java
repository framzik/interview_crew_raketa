package ru.khrebtov.crew_raketa.entity;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

public class ValueSpecification implements Specification<Values> {

    private final SearchCriteria criteria;

    public ValueSpecification(SearchCriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<Values> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

        if (isNull(criteria)) {
            return null;
        }

        if (criteria.getOperation().equalsIgnoreCase("gt")) {
            return builder.greaterThan(root.get(criteria.getKey()), criteria.getValue().toString());
        } else if (criteria.getOperation().equalsIgnoreCase("lt")) {
            return builder.lessThan(root.get(criteria.getKey()), criteria.getValue().toString());
        } else if (criteria.getOperation().equalsIgnoreCase("in")) {

            return builder.in(root.get(criteria.getKey())).value(initInValues());
        } else if (criteria.getOperation().equalsIgnoreCase("eq")) {
            return builder.equal(root.<String>get(criteria.getKey()), criteria.getValue().toString());
        } else if (criteria.getOperation().equalsIgnoreCase("ctn")) {
            if (root.get(criteria.getKey()).getJavaType() == String.class) {
                return builder.like(root.get(criteria.getKey()), "%" + criteria.getValue() + "%");
            } else {
                return builder.equal(root.get(criteria.getKey()), criteria.getValue());
            }
        }
        return null;
    }

    private List<Long> initInValues() {
        if (criteria.getValue().toString().trim().length() > 0) {
            String[] split = criteria.getValue().toString().split(",");
            return Arrays.stream(split)
                    .map(Long::parseLong)
                    .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }
}
