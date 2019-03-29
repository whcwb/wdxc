package com.ldz.dao.biz.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;

/*
 * 百度鹰眼返回的数据模型
 */
@Getter
@Setter
@ToString
@Table(name = "CL_YY")
public class Clyy implements Serializable{
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	/*
	 * 主键
	 */
	@Id
	@Column(name="ID")
	private String id;
	/*
	 * 终端编号 对应百度鹰眼的entity_name
	 */
	@Column(name = "ZDBH")
	private String zdbh;
	/*
	 * 经度
	 */
	@Column(name = "LONGITUDE")
	private BigDecimal longitude;
	/*
	 * 纬度
	 */
	@Column(name = "LATITUDE")
	private BigDecimal latitude;
	/*
	 * 上传点位的时间
	 */
	@Column(name = "LOCTIME")
	private String loc_time;
	/*
	 * 预留字段
	 */
	@Column(name = "OBJECTKEY")
	private String object_key;
	/*
	 * 方向角
	 */
	@Column(name = "DIRECTION")
	private String direction;
	/*
	 * 速度
	 */
	@Column(name = "SPEED")
	private BigDecimal speed;

	public enum InnerColumn {
		id("ID"),
		zdbh("ZDBH"),
		longitude("LONGITUDE"),
		latitude("LATITUDE"),
		loc_time("LOCTIME"),
		object_key("OBJECTKEY"),
		direction("DIRECTION"),
		speed("SPEED");


		private final String column;

		public String value() {
			return this.column;
		}

		public String getValue() {
			return this.column;
		}

		InnerColumn(String column) {
			this.column = column;
		}

		public String desc() {
			return this.column + " DESC";
		}

		public String asc() {
			return this.column + " ASC";
		}
	}

}
