package com.railinc.r2dq.util;

import static com.google.common.collect.Collections2.transform;

import java.util.ArrayList;
import java.util.Collection;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.google.common.base.Function;
import com.railinc.r2dq.domain.InboundMessage;

public class QueryHelper {
	
	public static <T> PagedCollection<T> emptyPagedCollection(PagingParameters paging) {
		return new PagedCollection<T>(new ArrayList<T>(0), new PagingResults(paging, 0));
	}
	
	public static <F,T> PagedCollection<T> query(PagingParameters paging,Criteria c,Function<F,T> xform) {
		return query(paging,c,null, xform);
	}
	
	public static <T> PagedCollection<T> query(PagingParameters paging, Criteria c) {
		return query(paging,c,null,null);
	}
	
	public static <T> PagedCollection<T> query(PagingParameters paging, Criteria c, Order o) {
		return query(paging,c,o,null);
	}

	public static <F,T> PagedCollection<T> query(PagingParameters paging, Criteria c, Order o, Function<F,T> xform) {
		c.setProjection(Projections.rowCount());
		int count = ((Long) c.uniqueResult()).intValue();
		
		c.setProjection(null);
		c.setFirstResult(paging.getPage() * paging.getPageSize()).setMaxResults(paging.getPageSize());
		
		c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		if (o != null) {
			c.addOrder(o);
		}
		@SuppressWarnings("unchecked")
		Collection<F> fs = (Collection<F>) c.list();
		
		if (xform == null) {
			xform = new Function<F,T>() {
				@SuppressWarnings("unchecked")
				@Override
				public T apply(F input) {
					return (T) input;
				}};
		}

		Collection<T> results = transform(fs, xform);
		PagedCollection<T> paged = new PagedCollection<T>(results, new PagingResults(paging, count));
		return paged;
	}
	
	public static void freeTextSearchFor(Criteria c, CriteriaValue<String> criteriaValue, String ... hibernateProperties) {
		if (criteriaValue.isSpecifiedAndNotNull()) {
			Criterion root = null;
			for (String prop : hibernateProperties) {
				Criterion il = Restrictions.ilike(prop, criteriaValue.value(), MatchMode.ANYWHERE);
				root =  (root == null) ? il : Restrictions.or(root,il);
			}
			if (root != null) {
				c.add(root);
			}
		}
	}
	
	public static void eqOrLikeOrIsNull(Criteria c, CriteriaValue<String> value, String hibernateProperty) {
		if (value.isSpecifiedAndLike()) {
			c.add(Restrictions.ilike(hibernateProperty, value.value(), MatchMode.ANYWHERE));
		} else {
			eqOrIsNull(c, value, hibernateProperty);
		}
	}

	public static <T> void eqOrIsNull(Criteria c, CriteriaValue<T> value, String hibernateProperty) {
		if (value.isSpecifiedAndNotNull()) {
			c.add(Restrictions.eq(hibernateProperty, value.value()));
		} else if (value.isSpecifiedAndNull()) {
			c.add(Restrictions.isNull(hibernateProperty));
		}
	}
	
	public static <T> void eqOrIsNotNull(Criteria c, CriteriaValue<T> value, String hibernateProperty) {
		if (value.isSpecifiedAndNotNull()) {
			c.add(Restrictions.eq(hibernateProperty, value.value()));
		} else if (value.isSpecifiedAndNull()) {
			c.add(Restrictions.isNotNull(hibernateProperty));
		}
	}
	
	public static <T> void isNullIfTrue(Criteria c, CriteriaValue<Boolean> value, String hibernateProperty) {
		if (value.isSpecified()) {
			if (value.value().booleanValue() == true) {
				c.add(Restrictions.isNull(hibernateProperty));	
			} else {
				c.add(Restrictions.isNotNull(hibernateProperty));
			}
		}
	}
	
	public static <T> void isNullIfFalse(Criteria c, CriteriaValue<Boolean> value, String hibernateProperty) {
		if (value.isSpecified()) {
			if (value.value().booleanValue() == false) {
				c.add(Restrictions.isNull(hibernateProperty));
			} else {
				c.add(Restrictions.isNotNull(hibernateProperty));	
			}
		}
	}
	
	public static void freeTextSearchFor(Criteria c, CriteriaValue<ArrayList<String>> data, String propertyInboundData) {
		if (data.isSpecifiedAndNotNull()) {
			Criterion root = null;
			for (String s : data.value()) {
				Criterion ilike = Restrictions.ilike(InboundMessage.PROPERTY_DATA, s , MatchMode.ANYWHERE);
				if (root == null) { root = ilike; } else { root = Restrictions.or(root, ilike);}
			}
			c.add(root);
		} else if (data.isSpecifiedAndNull()) {
			c.add(Restrictions.isNull(InboundMessage.PROPERTY_DATA));
		}
		
	}
	public static <T> void inOrIsNull(Criteria c, CriteriaValue<ArrayList<T>> criteriaValues,
			String hibernateProperty) {
		if (criteriaValues.isSpecifiedAndNotNull()) {
			c.add(Restrictions.in(hibernateProperty, criteriaValues.value()));
		} else if (criteriaValues.isSpecifiedAndNull()) {
			c.add(Restrictions.isNull(hibernateProperty));
		}
				
	}

	public static void eqOrIsNull(Criteria c, String propertyType) {
		// TODO Auto-generated method stub
		
	}
}
