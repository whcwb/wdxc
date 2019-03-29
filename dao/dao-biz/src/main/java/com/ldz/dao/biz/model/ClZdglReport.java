package com.ldz.dao.biz.model;


import java.io.Serializable;
import javax.persistence.*;

@Table(name = "cl_zdgl_report")
public class ClZdglReport implements Serializable {
    /**
     * 主键
     */
    @Id
    private String id;

    /**
     * 执行时间
     */
    @Column(name = "r_gxsj")
    private String rGxsj;

    /**
     * 终端编号
     */
    @Column(name = "r_zdbh")
    private String rZdbh;

    /**
     * 终端类型
     */
    @Column(name = "r_zdlx")
    private String rZdlx;

    /**
     * 里程总数-当天累加
     */
    @Column(name = "r_lczs")
    private String rLczs;

    /**
     * 驾驶总时长-当天都累加
     */
    @Column(name = "r_jszsc")
    private String rJszsc;

    /**
     * 行程总次数-当天都累加
     */
    @Column(name = "r_xcs")
    private String rXcs;

    /**
     * 急加速次数-当天都累加
     */
    @Column(name = "r_jjs")
    private String rJjs;

    /**
     * 急减速次数-当天都累加
     */
    @Column(name = "r_jjss")
    private String rJjss;

    /**
     * 急转弯次数-当天都累加
     */
    @Column(name = "r_jzw")
    private String rJzw;

    /**
     * 最高时速-当天
     */
    @Column(name = "r_zzss")
    private String rZzss;

    /**
     * 最大行驶里程-当天
     */
    @Column(name = "r_zzxslc")
    private String rZzxslc;

    /**
     * 超速次数-当天都累加
     */
    @Column(name = "r_cscs")
    private String rCscs;

    /**
     * 早高峰出行次数[7-9点]
     */
    @Column(name = "r_zgfcxcs")
    private String rZgfcxcs;

    /**
     * 晚高峰出行次数[17-19点]
     */
    @Column(name = "r_wgfcxcs")
    private String rWgfcxcs;

    /**
     * 深夜出行次数[22-00点]
     */
    @Column(name = "r_sycxcs")
    private String rSycxcs;

    /**
     * 统计日期
     */
    @Column(name = "r_tjrq")
    private String rTjrq;

    private String jgdm;

    private String jgmc;

    /**
     * 0点总里程
     */
    private String lc0;

    /**
     * 0点出行次数
     */
    private String cx0;

    /**
     * 1点总里程
     */
    private String lc1;

    /**
     * 1点出行次数
     */
    private String cx1;

    /**
     * 2点总里程
     */
    private String lc2;

    /**
     * 2点出行次数
     */
    private String cx2;

    private String lc3;

    private String cx3;

    private String lc4;

    private String cx4;

    private String lc5;

    private String cx5;

    private String lc6;

    private String cx6;

    private String lc7;

    private String cx7;

    private String lc8;

    private String cx8;

    private String lc9;

    private String cx9;

    private String lc10;

    private String cx10;

    private String lc11;

    private String cx11;

    private String lc12;

    private String cx12;

    private String lc13;

    private String cx13;

    private String lc14;

    private String cx14;

    private String lc15;

    private String cx15;

    private String lc16;

    private String cx16;

    private String lc17;

    private String cx17;

    private String lc18;

    private String cx18;

    private String lc19;

    private String cx19;

    private String lc20;

    private String cx20;

    private String lc21;

    private String cx21;

    private String lc22;

    private String cx22;

    private String lc23;

    private String cx23;

    @Column(name = "ds_sc")
    private  String dsSc;

    @Column(name = "ds_lc")
    private String dsLc;

    private static final long serialVersionUID = 1L;

    public String getDsSc() {
        return dsSc;
    }

    public void setDsSc(String dsSc) {
        this.dsSc = dsSc;
    }

    public String getDsLc() {
        return dsLc;
    }

    public void setDsLc(String dsLc) {
        this.dsLc = dsLc;
    }

    /**
     * 获取主键
     *
     * @return id - 主键
     */
    public String getId() {
        return id;
    }

    /**
     * 设置主键
     *
     * @param id 主键
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获取执行时间
     *
     * @return r_gxsj - 执行时间
     */
    public String getrGxsj() {
        return rGxsj;
    }

    /**
     * 设置执行时间
     *
     * @param rGxsj 执行时间
     */
    public void setrGxsj(String rGxsj) {
        this.rGxsj = rGxsj;
    }

    /**
     * 获取终端编号
     *
     * @return r_zdbh - 终端编号
     */
    public String getrZdbh() {
        return rZdbh;
    }

    /**
     * 设置终端编号
     *
     * @param rZdbh 终端编号
     */
    public void setrZdbh(String rZdbh) {
        this.rZdbh = rZdbh;
    }

    /**
     * 获取终端类型
     *
     * @return r_zdlx - 终端类型
     */
    public String getrZdlx() {
        return rZdlx;
    }

    /**
     * 设置终端类型
     *
     * @param rZdlx 终端类型
     */
    public void setrZdlx(String rZdlx) {
        this.rZdlx = rZdlx;
    }

    /**
     * 获取里程总数-当天累加
     *
     * @return r_lczs - 里程总数-当天累加
     */
    public String getrLczs() {
        return rLczs;
    }

    /**
     * 设置里程总数-当天累加
     *
     * @param rLczs 里程总数-当天累加
     */
    public void setrLczs(String rLczs) {
        this.rLczs = rLczs;
    }

    /**
     * 获取驾驶总时长-当天都累加
     *
     * @return r_jszsc - 驾驶总时长-当天都累加
     */
    public String getrJszsc() {
        return rJszsc;
    }

    /**
     * 设置驾驶总时长-当天都累加
     *
     * @param rJszsc 驾驶总时长-当天都累加
     */
    public void setrJszsc(String rJszsc) {
        this.rJszsc = rJszsc;
    }

    /**
     * 获取行程总次数-当天都累加
     *
     * @return r_xcs - 行程总次数-当天都累加
     */
    public String getrXcs() {
        return rXcs;
    }

    /**
     * 设置行程总次数-当天都累加
     *
     * @param rXcs 行程总次数-当天都累加
     */
    public void setrXcs(String rXcs) {
        this.rXcs = rXcs;
    }

    /**
     * 获取急加速次数-当天都累加
     *
     * @return r_jjs - 急加速次数-当天都累加
     */
    public String getrJjs() {
        return rJjs;
    }

    /**
     * 设置急加速次数-当天都累加
     *
     * @param rJjs 急加速次数-当天都累加
     */
    public void setrJjs(String rJjs) {
        this.rJjs = rJjs;
    }

    /**
     * 获取急减速次数-当天都累加
     *
     * @return r_jjss - 急减速次数-当天都累加
     */
    public String getrJjss() {
        return rJjss;
    }

    /**
     * 设置急减速次数-当天都累加
     *
     * @param rJjss 急减速次数-当天都累加
     */
    public void setrJjss(String rJjss) {
        this.rJjss = rJjss;
    }

    /**
     * 获取急转弯次数-当天都累加
     *
     * @return r_jzw - 急转弯次数-当天都累加
     */
    public String getrJzw() {
        return rJzw;
    }

    /**
     * 设置急转弯次数-当天都累加
     *
     * @param rJzw 急转弯次数-当天都累加
     */
    public void setrJzw(String rJzw) {
        this.rJzw = rJzw;
    }

    /**
     * 获取最高时速-当天
     *
     * @return r_zzss - 最高时速-当天
     */
    public String getrZzss() {
        return rZzss;
    }

    /**
     * 设置最高时速-当天
     *
     * @param rZzss 最高时速-当天
     */
    public void setrZzss(String rZzss) {
        this.rZzss = rZzss;
    }

    /**
     * 获取最大行驶里程-当天
     *
     * @return r_zzxslc - 最大行驶里程-当天
     */
    public String getrZzxslc() {
        return rZzxslc;
    }

    /**
     * 设置最大行驶里程-当天
     *
     * @param rZzxslc 最大行驶里程-当天
     */
    public void setrZzxslc(String rZzxslc) {
        this.rZzxslc = rZzxslc;
    }

    /**
     * 获取超速次数-当天都累加
     *
     * @return r_cscs - 超速次数-当天都累加
     */
    public String getrCscs() {
        return rCscs;
    }

    /**
     * 设置超速次数-当天都累加
     *
     * @param rCscs 超速次数-当天都累加
     */
    public void setrCscs(String rCscs) {
        this.rCscs = rCscs;
    }

    /**
     * 获取早高峰出行次数[7-9点]
     *
     * @return r_zgfcxcs - 早高峰出行次数[7-9点]
     */
    public String getrZgfcxcs() {
        return rZgfcxcs;
    }

    /**
     * 设置早高峰出行次数[7-9点]
     *
     * @param rZgfcxcs 早高峰出行次数[7-9点]
     */
    public void setrZgfcxcs(String rZgfcxcs) {
        this.rZgfcxcs = rZgfcxcs;
    }

    /**
     * 获取晚高峰出行次数[17-19点]
     *
     * @return r_wgfcxcs - 晚高峰出行次数[17-19点]
     */
    public String getrWgfcxcs() {
        return rWgfcxcs;
    }

    /**
     * 设置晚高峰出行次数[17-19点]
     *
     * @param rWgfcxcs 晚高峰出行次数[17-19点]
     */
    public void setrWgfcxcs(String rWgfcxcs) {
        this.rWgfcxcs = rWgfcxcs;
    }

    /**
     * 获取深夜出行次数[22-00点]
     *
     * @return r_sycxcs - 深夜出行次数[22-00点]
     */
    public String getrSycxcs() {
        return rSycxcs;
    }

    /**
     * 设置深夜出行次数[22-00点]
     *
     * @param rSycxcs 深夜出行次数[22-00点]
     */
    public void setrSycxcs(String rSycxcs) {
        this.rSycxcs = rSycxcs;
    }

    /**
     * 获取统计日期
     *
     * @return r_tjrq - 统计日期
     */
    public String getrTjrq() {
        return rTjrq;
    }

    /**
     * 设置统计日期
     *
     * @param rTjrq 统计日期
     */
    public void setrTjrq(String rTjrq) {
        this.rTjrq = rTjrq;
    }

    /**
     * @return jgdm
     */
    public String getJgdm() {
        return jgdm;
    }

    /**
     * @param jgdm
     */
    public void setJgdm(String jgdm) {
        this.jgdm = jgdm;
    }

    /**
     * @return jgmc
     */
    public String getJgmc() {
        return jgmc;
    }

    /**
     * @param jgmc
     */
    public void setJgmc(String jgmc) {
        this.jgmc = jgmc;
    }

    /**
     * 获取0点总里程
     *
     * @return lc0 - 0点总里程
     */
    public String getLc0() {
        return lc0;
    }

    /**
     * 设置0点总里程
     *
     * @param lc0 0点总里程
     */
    public void setLc0(String lc0) {
        this.lc0 = lc0;
    }

    /**
     * 获取0点出行次数
     *
     * @return cx0 - 0点出行次数
     */
    public String getCx0() {
        return cx0;
    }

    /**
     * 设置0点出行次数
     *
     * @param cx0 0点出行次数
     */
    public void setCx0(String cx0) {
        this.cx0 = cx0;
    }

    /**
     * 获取1点总里程
     *
     * @return lc1 - 1点总里程
     */
    public String getLc1() {
        return lc1;
    }

    /**
     * 设置1点总里程
     *
     * @param lc1 1点总里程
     */
    public void setLc1(String lc1) {
        this.lc1 = lc1;
    }

    /**
     * 获取1点出行次数
     *
     * @return cx1 - 1点出行次数
     */
    public String getCx1() {
        return cx1;
    }

    /**
     * 设置1点出行次数
     *
     * @param cx1 1点出行次数
     */
    public void setCx1(String cx1) {
        this.cx1 = cx1;
    }

    /**
     * 获取2点总里程
     *
     * @return lc2 - 2点总里程
     */
    public String getLc2() {
        return lc2;
    }

    /**
     * 设置2点总里程
     *
     * @param lc2 2点总里程
     */
    public void setLc2(String lc2) {
        this.lc2 = lc2;
    }

    /**
     * 获取2点出行次数
     *
     * @return cx2 - 2点出行次数
     */
    public String getCx2() {
        return cx2;
    }

    /**
     * 设置2点出行次数
     *
     * @param cx2 2点出行次数
     */
    public void setCx2(String cx2) {
        this.cx2 = cx2;
    }

    /**
     * @return lc3
     */
    public String getLc3() {
        return lc3;
    }

    /**
     * @param lc3
     */
    public void setLc3(String lc3) {
        this.lc3 = lc3;
    }

    /**
     * @return cx3
     */
    public String getCx3() {
        return cx3;
    }

    /**
     * @param cx3
     */
    public void setCx3(String cx3) {
        this.cx3 = cx3;
    }

    /**
     * @return lc4
     */
    public String getLc4() {
        return lc4;
    }

    /**
     * @param lc4
     */
    public void setLc4(String lc4) {
        this.lc4 = lc4;
    }

    /**
     * @return cx4
     */
    public String getCx4() {
        return cx4;
    }

    /**
     * @param cx4
     */
    public void setCx4(String cx4) {
        this.cx4 = cx4;
    }

    /**
     * @return lc5
     */
    public String getLc5() {
        return lc5;
    }

    /**
     * @param lc5
     */
    public void setLc5(String lc5) {
        this.lc5 = lc5;
    }

    /**
     * @return cx5
     */
    public String getCx5() {
        return cx5;
    }

    /**
     * @param cx5
     */
    public void setCx5(String cx5) {
        this.cx5 = cx5;
    }

    /**
     * @return lc6
     */
    public String getLc6() {
        return lc6;
    }

    /**
     * @param lc6
     */
    public void setLc6(String lc6) {
        this.lc6 = lc6;
    }

    /**
     * @return cx6
     */
    public String getCx6() {
        return cx6;
    }

    /**
     * @param cx6
     */
    public void setCx6(String cx6) {
        this.cx6 = cx6;
    }

    /**
     * @return lc7
     */
    public String getLc7() {
        return lc7;
    }

    /**
     * @param lc7
     */
    public void setLc7(String lc7) {
        this.lc7 = lc7;
    }

    /**
     * @return cx7
     */
    public String getCx7() {
        return cx7;
    }

    /**
     * @param cx7
     */
    public void setCx7(String cx7) {
        this.cx7 = cx7;
    }

    /**
     * @return lc8
     */
    public String getLc8() {
        return lc8;
    }

    /**
     * @param lc8
     */
    public void setLc8(String lc8) {
        this.lc8 = lc8;
    }

    /**
     * @return cx8
     */
    public String getCx8() {
        return cx8;
    }

    /**
     * @param cx8
     */
    public void setCx8(String cx8) {
        this.cx8 = cx8;
    }

    /**
     * @return lc9
     */
    public String getLc9() {
        return lc9;
    }

    /**
     * @param lc9
     */
    public void setLc9(String lc9) {
        this.lc9 = lc9;
    }

    /**
     * @return cx9
     */
    public String getCx9() {
        return cx9;
    }

    /**
     * @param cx9
     */
    public void setCx9(String cx9) {
        this.cx9 = cx9;
    }

    /**
     * @return lc10
     */
    public String getLc10() {
        return lc10;
    }

    /**
     * @param lc10
     */
    public void setLc10(String lc10) {
        this.lc10 = lc10;
    }

    /**
     * @return cx10
     */
    public String getCx10() {
        return cx10;
    }

    /**
     * @param cx10
     */
    public void setCx10(String cx10) {
        this.cx10 = cx10;
    }

    /**
     * @return lc11
     */
    public String getLc11() {
        return lc11;
    }

    /**
     * @param lc11
     */
    public void setLc11(String lc11) {
        this.lc11 = lc11;
    }

    /**
     * @return cx11
     */
    public String getCx11() {
        return cx11;
    }

    /**
     * @param cx11
     */
    public void setCx11(String cx11) {
        this.cx11 = cx11;
    }

    /**
     * @return lc12
     */
    public String getLc12() {
        return lc12;
    }

    /**
     * @param lc12
     */
    public void setLc12(String lc12) {
        this.lc12 = lc12;
    }

    /**
     * @return cx12
     */
    public String getCx12() {
        return cx12;
    }

    /**
     * @param cx12
     */
    public void setCx12(String cx12) {
        this.cx12 = cx12;
    }

    /**
     * @return lc13
     */
    public String getLc13() {
        return lc13;
    }

    /**
     * @param lc13
     */
    public void setLc13(String lc13) {
        this.lc13 = lc13;
    }

    /**
     * @return cx13
     */
    public String getCx13() {
        return cx13;
    }

    /**
     * @param cx13
     */
    public void setCx13(String cx13) {
        this.cx13 = cx13;
    }

    /**
     * @return lc14
     */
    public String getLc14() {
        return lc14;
    }

    /**
     * @param lc14
     */
    public void setLc14(String lc14) {
        this.lc14 = lc14;
    }

    /**
     * @return cx14
     */
    public String getCx14() {
        return cx14;
    }

    /**
     * @param cx14
     */
    public void setCx14(String cx14) {
        this.cx14 = cx14;
    }

    /**
     * @return lc15
     */
    public String getLc15() {
        return lc15;
    }

    /**
     * @param lc15
     */
    public void setLc15(String lc15) {
        this.lc15 = lc15;
    }

    /**
     * @return cx15
     */
    public String getCx15() {
        return cx15;
    }

    /**
     * @param cx15
     */
    public void setCx15(String cx15) {
        this.cx15 = cx15;
    }

    /**
     * @return lc16
     */
    public String getLc16() {
        return lc16;
    }

    /**
     * @param lc16
     */
    public void setLc16(String lc16) {
        this.lc16 = lc16;
    }

    /**
     * @return cx16
     */
    public String getCx16() {
        return cx16;
    }

    /**
     * @param cx16
     */
    public void setCx16(String cx16) {
        this.cx16 = cx16;
    }

    /**
     * @return lc17
     */
    public String getLc17() {
        return lc17;
    }

    /**
     * @param lc17
     */
    public void setLc17(String lc17) {
        this.lc17 = lc17;
    }

    /**
     * @return cx17
     */
    public String getCx17() {
        return cx17;
    }

    /**
     * @param cx17
     */
    public void setCx17(String cx17) {
        this.cx17 = cx17;
    }

    /**
     * @return lc18
     */
    public String getLc18() {
        return lc18;
    }

    /**
     * @param lc18
     */
    public void setLc18(String lc18) {
        this.lc18 = lc18;
    }

    /**
     * @return cx18
     */
    public String getCx18() {
        return cx18;
    }

    /**
     * @param cx18
     */
    public void setCx18(String cx18) {
        this.cx18 = cx18;
    }

    /**
     * @return lc19
     */
    public String getLc19() {
        return lc19;
    }

    /**
     * @param lc19
     */
    public void setLc19(String lc19) {
        this.lc19 = lc19;
    }

    /**
     * @return cx19
     */
    public String getCx19() {
        return cx19;
    }

    /**
     * @param cx19
     */
    public void setCx19(String cx19) {
        this.cx19 = cx19;
    }

    /**
     * @return lc20
     */
    public String getLc20() {
        return lc20;
    }

    /**
     * @param lc20
     */
    public void setLc20(String lc20) {
        this.lc20 = lc20;
    }

    /**
     * @return cx20
     */
    public String getCx20() {
        return cx20;
    }

    /**
     * @param cx20
     */
    public void setCx20(String cx20) {
        this.cx20 = cx20;
    }

    /**
     * @return lc21
     */
    public String getLc21() {
        return lc21;
    }

    /**
     * @param lc21
     */
    public void setLc21(String lc21) {
        this.lc21 = lc21;
    }

    /**
     * @return cx21
     */
    public String getCx21() {
        return cx21;
    }

    /**
     * @param cx21
     */
    public void setCx21(String cx21) {
        this.cx21 = cx21;
    }

    /**
     * @return lc22
     */
    public String getLc22() {
        return lc22;
    }

    /**
     * @param lc22
     */
    public void setLc22(String lc22) {
        this.lc22 = lc22;
    }

    /**
     * @return cx22
     */
    public String getCx22() {
        return cx22;
    }

    /**
     * @param cx22
     */
    public void setCx22(String cx22) {
        this.cx22 = cx22;
    }

    /**
     * @return lc23
     */
    public String getLc23() {
        return lc23;
    }

    /**
     * @param lc23
     */
    public void setLc23(String lc23) {
        this.lc23 = lc23;
    }

    /**
     * @return cx23
     */
    public String getCx23() {
        return cx23;
    }

    /**
     * @param cx23
     */
    public void setCx23(String cx23) {
        this.cx23 = cx23;
    }

    public enum InnerColumn {
        id("id"),
        rGxsj("r_gxsj"),
        rZdbh("r_zdbh"),
        rZdlx("r_zdlx"),
        rLczs("r_lczs"),
        rJszsc("r_jszsc"),
        rXcs("r_xcs"),
        rJjs("r_jjs"),
        rJjss("r_jjss"),
        rJzw("r_jzw"),
        rZzss("r_zzss"),
        rZzxslc("r_zzxslc"),
        rCscs("r_cscs"),
        rZgfcxcs("r_zgfcxcs"),
        rWgfcxcs("r_wgfcxcs"),
        rSycxcs("r_sycxcs"),
        rTjrq("r_tjrq"),
        jgdm("jgdm"),
        jgmc("jgmc"),
        lc0("lc0"),
        cx0("cx0"),
        lc1("lc1"),
        cx1("cx1"),
        lc2("lc2"),
        cx2("cx2"),
        lc3("lc3"),
        cx3("cx3"),
        lc4("lc4"),
        cx4("cx4"),
        lc5("lc5"),
        cx5("cx5"),
        lc6("lc6"),
        cx6("cx6"),
        lc7("lc7"),
        cx7("cx7"),
        lc8("lc8"),
        cx8("cx8"),
        lc9("lc9"),
        cx9("cx9"),
        lc10("lc10"),
        cx10("cx10"),
        lc11("lc11"),
        cx11("cx11"),
        lc12("lc12"),
        cx12("cx12"),
        lc13("lc13"),
        cx13("cx13"),
        lc14("lc14"),
        cx14("cx14"),
        lc15("lc15"),
        cx15("cx15"),
        lc16("lc16"),
        cx16("cx16"),
        lc17("lc17"),
        cx17("cx17"),
        lc18("lc18"),
        cx18("cx18"),
        lc19("lc19"),
        cx19("cx19"),
        lc20("lc20"),
        cx20("cx20"),
        lc21("lc21"),
        cx21("cx21"),
        lc22("lc22"),
        cx22("cx22"),
        lc23("lc23"),
        cx23("cx23"),
        dsSc("ds_sc"),
        dsLc("dsLc");

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