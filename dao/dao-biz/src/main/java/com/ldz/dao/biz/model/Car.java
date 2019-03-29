package com.ldz.dao.biz.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Table(name = "car",schema = "ubi")
public class Car implements Serializable {
    @Id
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    /**
     * 设备编号
     */
    private String zdbh;

    /**
     * 设备类型
     */
    private Byte zdlx;

    /**
     * 车牌号
     */
    @Column(name = "plate_no")
    private String plateNo;

    /**
     * 车架号
     */
    @Column(name = "vin_no")
    private String vinNo;

    /**
     * 发动机号
     */
    private String engine;

    /**
     * 车主
     */
    @Column(name = "car_owner")
    private String carOwner;

    /**
     * 品牌
     */
    private String brand;

    /**
     * 车型
     */
    private String model;

    /**
     * 行驶证图片
     */
    private String xsz;

    /**
     * 登记日期
     */
    @Column(name = "register_date")
    private Date registerDate;

    /**
     * 新车购置价
     */
    private Integer price;

    /**
     * 座位数
     */
    private Integer place;

    /**
     * 排水量
     */
    private Float displacement;

    /**
     * 过户次数
     */
    private Integer transfer;

    /**
     * 贷款车
     */
    private Boolean loan;

    /**
     * 备注
     */
    private String remark;

    /**
     * 商业险到期时间
     */
    @Column(name = "commercial_expiry")
    private Date commercialExpiry;

    /**
     * 交强险到期时间
     */
    @Column(name = "compulsory_expiry")
    private Date compulsoryExpiry;

    /**
     * 报价状态
     */
    @Column(name = "quote_status")
    private Short quoteStatus;

    /**
     * 核保状态
     */
    @Column(name = "underwrite_status")
    private Short underwriteStatus;

    /**
     * 剩余里程
     */
    @Column(name = "remaining_mileage")
    private Integer remainingMileage;

    /**
     * 购买时间
     */
    @Column(name = "buy_date")
    private Date buyDate;

    /**
     * 总里程
     */
    private Integer mileage;

    /**
     * 上年投保公司
     */
    @Column(name = "last_year_insurance_company")
    private Integer lastYearInsuranceCompany;

    /**
     * 车辆状态
     */
    private Boolean status;

    /**
     * 续保状态
     */
    private Boolean insure;

    /**
     * 创建时间
     */
    @Column(name = "created_at")
    private Date createdAt;

    /**
     * 更新时间
     */
    @Column(name = "updated_at")
    private Date updatedAt;

    private static final long serialVersionUID = 1L;

    /**
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return user_id
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * @param userId
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * 获取设备编号
     *
     * @return zdbh - 设备编号
     */
    public String getZdbh() {
        return zdbh;
    }

    /**
     * 设置设备编号
     *
     * @param zdbh 设备编号
     */
    public void setZdbh(String zdbh) {
        this.zdbh = zdbh;
    }

    /**
     * 获取设备类型
     *
     * @return zdlx - 设备类型
     */
    public Byte getZdlx() {
        return zdlx;
    }

    /**
     * 设置设备类型
     *
     * @param zdlx 设备类型
     */
    public void setZdlx(Byte zdlx) {
        this.zdlx = zdlx;
    }

    /**
     * 获取车牌号
     *
     * @return plate_no - 车牌号
     */
    public String getPlateNo() {
        return plateNo;
    }

    /**
     * 设置车牌号
     *
     * @param plateNo 车牌号
     */
    public void setPlateNo(String plateNo) {
        this.plateNo = plateNo;
    }

    /**
     * 获取车架号
     *
     * @return vin_no - 车架号
     */
    public String getVinNo() {
        return vinNo;
    }

    /**
     * 设置车架号
     *
     * @param vinNo 车架号
     */
    public void setVinNo(String vinNo) {
        this.vinNo = vinNo;
    }

    /**
     * 获取发动机号
     *
     * @return engine - 发动机号
     */
    public String getEngine() {
        return engine;
    }

    /**
     * 设置发动机号
     *
     * @param engine 发动机号
     */
    public void setEngine(String engine) {
        this.engine = engine;
    }

    /**
     * 获取车主
     *
     * @return car_owner - 车主
     */
    public String getCarOwner() {
        return carOwner;
    }

    /**
     * 设置车主
     *
     * @param carOwner 车主
     */
    public void setCarOwner(String carOwner) {
        this.carOwner = carOwner;
    }

    /**
     * 获取品牌
     *
     * @return brand - 品牌
     */
    public String getBrand() {
        return brand;
    }

    /**
     * 设置品牌
     *
     * @param brand 品牌
     */
    public void setBrand(String brand) {
        this.brand = brand;
    }

    /**
     * 获取车型
     *
     * @return model - 车型
     */
    public String getModel() {
        return model;
    }

    /**
     * 设置车型
     *
     * @param model 车型
     */
    public void setModel(String model) {
        this.model = model;
    }

    /**
     * 获取行驶证图片
     *
     * @return xsz - 行驶证图片
     */
    public String getXsz() {
        return xsz;
    }

    /**
     * 设置行驶证图片
     *
     * @param xsz 行驶证图片
     */
    public void setXsz(String xsz) {
        this.xsz = xsz;
    }

    /**
     * 获取登记日期
     *
     * @return register_date - 登记日期
     */
    public Date getRegisterDate() {
        return registerDate;
    }

    /**
     * 设置登记日期
     *
     * @param registerDate 登记日期
     */
    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
    }

    /**
     * 获取新车购置价
     *
     * @return price - 新车购置价
     */
    public Integer getPrice() {
        return price;
    }

    /**
     * 设置新车购置价
     *
     * @param price 新车购置价
     */
    public void setPrice(Integer price) {
        this.price = price;
    }

    /**
     * 获取座位数
     *
     * @return place - 座位数
     */
    public Integer getPlace() {
        return place;
    }

    /**
     * 设置座位数
     *
     * @param place 座位数
     */
    public void setPlace(Integer place) {
        this.place = place;
    }

    /**
     * 获取排水量
     *
     * @return displacement - 排水量
     */
    public Float getDisplacement() {
        return displacement;
    }

    /**
     * 设置排水量
     *
     * @param displacement 排水量
     */
    public void setDisplacement(Float displacement) {
        this.displacement = displacement;
    }

    /**
     * 获取过户次数
     *
     * @return transfer - 过户次数
     */
    public Integer getTransfer() {
        return transfer;
    }

    /**
     * 设置过户次数
     *
     * @param transfer 过户次数
     */
    public void setTransfer(Integer transfer) {
        this.transfer = transfer;
    }

    /**
     * 获取贷款车
     *
     * @return loan - 贷款车
     */
    public Boolean getLoan() {
        return loan;
    }

    /**
     * 设置贷款车
     *
     * @param loan 贷款车
     */
    public void setLoan(Boolean loan) {
        this.loan = loan;
    }

    /**
     * 获取备注
     *
     * @return remark - 备注
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置备注
     *
     * @param remark 备注
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * 获取商业险到期时间
     *
     * @return commercial_expiry - 商业险到期时间
     */
    public Date getCommercialExpiry() {
        return commercialExpiry;
    }

    /**
     * 设置商业险到期时间
     *
     * @param commercialExpiry 商业险到期时间
     */
    public void setCommercialExpiry(Date commercialExpiry) {
        this.commercialExpiry = commercialExpiry;
    }

    /**
     * 获取交强险到期时间
     *
     * @return compulsory_expiry - 交强险到期时间
     */
    public Date getCompulsoryExpiry() {
        return compulsoryExpiry;
    }

    /**
     * 设置交强险到期时间
     *
     * @param compulsoryExpiry 交强险到期时间
     */
    public void setCompulsoryExpiry(Date compulsoryExpiry) {
        this.compulsoryExpiry = compulsoryExpiry;
    }

    /**
     * 获取报价状态
     *
     * @return quote_status - 报价状态
     */
    public Short getQuoteStatus() {
        return quoteStatus;
    }

    /**
     * 设置报价状态
     *
     * @param quoteStatus 报价状态
     */
    public void setQuoteStatus(Short quoteStatus) {
        this.quoteStatus = quoteStatus;
    }

    /**
     * 获取核保状态
     *
     * @return underwrite_status - 核保状态
     */
    public Short getUnderwriteStatus() {
        return underwriteStatus;
    }

    /**
     * 设置核保状态
     *
     * @param underwriteStatus 核保状态
     */
    public void setUnderwriteStatus(Short underwriteStatus) {
        this.underwriteStatus = underwriteStatus;
    }

    /**
     * 获取剩余里程
     *
     * @return remaining_mileage - 剩余里程
     */
    public Integer getRemainingMileage() {
        return remainingMileage;
    }

    /**
     * 设置剩余里程
     *
     * @param remainingMileage 剩余里程
     */
    public void setRemainingMileage(Integer remainingMileage) {
        this.remainingMileage = remainingMileage;
    }

    /**
     * 获取购买时间
     *
     * @return buy_date - 购买时间
     */
    public Date getBuyDate() {
        return buyDate;
    }

    /**
     * 设置购买时间
     *
     * @param buyDate 购买时间
     */
    public void setBuyDate(Date buyDate) {
        this.buyDate = buyDate;
    }

    /**
     * 获取总里程
     *
     * @return mileage - 总里程
     */
    public Integer getMileage() {
        return mileage;
    }

    /**
     * 设置总里程
     *
     * @param mileage 总里程
     */
    public void setMileage(Integer mileage) {
        this.mileage = mileage;
    }

    /**
     * 获取上年投保公司
     *
     * @return last_year_insurance_company - 上年投保公司
     */
    public Integer getLastYearInsuranceCompany() {
        return lastYearInsuranceCompany;
    }

    /**
     * 设置上年投保公司
     *
     * @param lastYearInsuranceCompany 上年投保公司
     */
    public void setLastYearInsuranceCompany(Integer lastYearInsuranceCompany) {
        this.lastYearInsuranceCompany = lastYearInsuranceCompany;
    }

    /**
     * 获取车辆状态
     *
     * @return status - 车辆状态
     */
    public Boolean getStatus() {
        return status;
    }

    /**
     * 设置车辆状态
     *
     * @param status 车辆状态
     */
    public void setStatus(Boolean status) {
        this.status = status;
    }

    /**
     * 获取续保状态
     *
     * @return insure - 续保状态
     */
    public Boolean getInsure() {
        return insure;
    }

    /**
     * 设置续保状态
     *
     * @param insure 续保状态
     */
    public void setInsure(Boolean insure) {
        this.insure = insure;
    }

    /**
     * 获取创建时间
     *
     * @return created_at - 创建时间
     */
    public Date getCreatedAt() {
        return createdAt;
    }

    /**
     * 设置创建时间
     *
     * @param createdAt 创建时间
     */
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * 获取更新时间
     *
     * @return updated_at - 更新时间
     */
    public Date getUpdatedAt() {
        return updatedAt;
    }

    /**
     * 设置更新时间
     *
     * @param updatedAt 更新时间
     */
    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public enum InnerColumn {
        id("id"),
        userId("user_id"),
        zdbh("zdbh"),
        zdlx("zdlx"),
        plateNo("plate_no"),
        vinNo("vin_no"),
        engine("engine"),
        carOwner("car_owner"),
        brand("brand"),
        model("model"),
        xsz("xsz"),
        registerDate("register_date"),
        price("price"),
        place("place"),
        displacement("displacement"),
        transfer("transfer"),
        loan("loan"),
        remark("remark"),
        commercialExpiry("commercial_expiry"),
        compulsoryExpiry("compulsory_expiry"),
        quoteStatus("quote_status"),
        underwriteStatus("underwrite_status"),
        remainingMileage("remaining_mileage"),
        buyDate("buy_date"),
        mileage("mileage"),
        lastYearInsuranceCompany("last_year_insurance_company"),
        status("status"),
        insure("insure"),
        createdAt("created_at"),
        updatedAt("updated_at");

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