package com.ldz.job.convert;


import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.Converter;

/**
 * BeanUtils日期对象转换器
 * @author libinbin
 *
 */
public class BeanUtilsDateConverter implements Converter {
	private Object defaultValue = null;

	private boolean useDefault = true;

	public BeanUtilsDateConverter() {
		this.defaultValue = null;
		this.useDefault = true;
	}

	public BeanUtilsDateConverter(Object defaultValue) {
		this.defaultValue = defaultValue;
		this.useDefault = true;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Object convert(Class type, Object value) {
		if (value == null || "".equals(value)) {
			if (useDefault) {
				return (defaultValue);
			} else {
				throw new ConversionException("No value specified");
			}
		}

		if (value instanceof Date || value instanceof Timestamp || value instanceof BigDecimal) {
			return (value);
		}

		try {
			return (Date.valueOf(value.toString()));
		} catch (Exception e) {
			if (useDefault) {
				return (defaultValue);
			} else {
				throw new ConversionException(e);
			}
		}
	}

}
