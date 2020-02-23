package com.sq.transportmanage.gateway.service.common.web.fastjson.filter;

import com.alibaba.fastjson.serializer.ValueFilter;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * @ClassName: BigDecimalFilter
 * @Description: BigDecimal转换器
 * @author: yanyunpeng
 * @date: 2018年12月5日 上午10:34:17
 * 
 */
public class BigDecimalFilter implements ValueFilter {

	@Override
	public Object process(Object object, String name, Object value) {
		if (value != null && (value instanceof BigDecimal || value instanceof Double)) {
			DecimalFormat format = new DecimalFormat("#########0.##");
			return format.format(value);
		}
		return value;
	}

}
