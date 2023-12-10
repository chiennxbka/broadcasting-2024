package com.mintpot.broadcasting.repository.specs;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class GenericSpecification<T> implements Specification<T> {

  private final List<SearchCriteria> list;

  public GenericSpecification() {
    this.list = new ArrayList<>();
  }

  public void add(SearchCriteria criteria) {
    list.add(criteria);
  }

  @Override
  public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
    //create a new predicate list
    List<Predicate> predicates = new ArrayList<>();
    //add add criteria to predicates
    list.forEach(item -> {
      switch (item.getOperation()) {
        case GREATER_THAN:
          predicates.add(builder.greaterThan(
              root.get(item.getKey()), item.getValue().toString()));
          break;
        case LESS_THAN:
          predicates.add(builder.lessThan(
              root.get(item.getKey()), item.getValue().toString()));
          break;
        case GREATER_THAN_EQUAL:
          predicates.add(builder.greaterThanOrEqualTo(
              root.get(item.getKey()), item.getValue().toString()));
          break;
        case LESS_THAN_EQUAL:
          predicates.add(builder.lessThanOrEqualTo(
              root.get(item.getKey()), item.getValue().toString()));
          break;
        case NOT_EQUAL:
          predicates.add(builder.notEqual(
              root.get(item.getKey()), item.getValue()));
          break;
        case EQUAL:
          predicates.add(builder.equal(
              root.get(item.getKey()), item.getValue()));
          break;
        case MATCH:
          predicates.add(builder.like(
              builder.lower(root.get(item.getKey())),
              "%" + item.getValue().toString().toLowerCase() + "%"));
          break;
        case MATCH_START:
          predicates.add(builder.like(
              builder.lower(root.get(item.getKey())),
              "%" + item.getValue().toString().toLowerCase()));
          break;
        case MATCH_END:
          predicates.add(builder.like(
              builder.lower(root.get(item.getKey())),
              item.getValue().toString().toLowerCase() + "%"));
          break;
        case IN:
          predicates.add(builder.in(root.get(item.getKey())).value(item.getValue()));
          break;
        case NOT_IN:
          predicates.add(builder.not(root.get(item.getKey())).in(item.getValue()));
          break;
      }
    });
    return builder.and(predicates.toArray(new Predicate[0]));
  }
}
