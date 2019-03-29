package com.ldz.sys.constant;

import org.apache.commons.lang.StringUtils;

/**
 */
public class Dict {
    public enum CommonStatus{
        VALID("10","有效"),INVALID("00","无效");
        private String code;
        private String desc;

        public static CommonStatus toEnum(String code){
            if (StringUtils.isEmpty(code))return null;
            for (CommonStatus hpzl : CommonStatus.values()) {
                if(code.equals(hpzl.getCode())){
                    return hpzl;
                }
            }
            return null;
        }

        public static String getDescByCode(String code){
            CommonStatus status = toEnum(code);
            return status == null ? "" : status.getDesc();
        }

        CommonStatus(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }
    public enum OrgType{
        SCHOOL("10","学校"),COMPANY("00","公司");
        private String code;
        private String desc;

        public static OrgType toEnum(String code){
            if (StringUtils.isEmpty(code))return null;
            for (OrgType value : OrgType.values()) {
                if(code.equals(value.getCode())){
                    return value;
                }
            }
            return null;
        }

        public static String getDescByCode(String code){
            OrgType status = toEnum(code);
            return status == null ? "" : status.getDesc();
        }

        OrgType(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }
    public enum Hpzl{
        NOT_REPLY("01","大型汽车"),REJECT("02","小型汽车");
        private String code;
        private String desc;

        public static Hpzl toEnum(String code){
            if (StringUtils.isEmpty(code))return null;
            for (Hpzl hpzl : Hpzl.values()) {
                if(code.equals(hpzl.getCode())){
                    return hpzl;
                }
            }
            return null;
        }

        public static String getDescByCode(String code){
            Hpzl hpzl = toEnum(code);
            return hpzl == null ? "" : hpzl.getDesc();
        }

        Hpzl(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }

    public enum ReplyStatus{
        NOT_REPLY("11","未审批"),REJECT("12","驳回"),PASS("13","审批通过");
        private String code;
        private String desc;

        public static ReplyStatus toEnum(String code){
            if (StringUtils.isEmpty(code))return null;
            for (ReplyStatus r : ReplyStatus.values()) {
                if(code.equals(r.getCode())){
                    return r;
                }
            }
            return null;
        }

        public static String getDescByCode(String code){
            ReplyStatus r = toEnum(code);
            return r == null ? "" : r.getDesc();
        }


        ReplyStatus(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }
    public enum MessageStatus{
        NEW("1","新建"),FINISHED("2","已完成");

        private String code;
        private String desc;

        MessageStatus(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }

    public enum UserStatus{
        VALID("01","有效");

        private String code;
        private String desc;

        UserStatus(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }

    public enum UserType{
        ADMIN("00","管理员"),CUSTOMER("01","客户");

        private String code;
        private String desc;

        UserType(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }

    public enum UserUnitStatus{
        VALID("00","可用"),APPLYING("01","申请中"),REJECT("02","申请未通过");

        private String code;
        private String desc;

        UserUnitStatus(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }
}
