package com.ldz.dwq.common.bean;

/**
 * 序号	命令码	含义
 1	0x00 0x01	读取实时环境参数数据命令
 2	0x00 0x02	写入系统参数配置命令
 3	0x00 0x03	读取系统参数命令
 4	0x00 0x04	读取历史数据命令
 5	0x00 0x05	历史数据重读命令
 6	0x00 0x06	手动控制命令
 7	0x00 0x07	复位气象站命令
 8	0x00 0x08	读取气象站地址命令
 9	0x00 0x09	写气象站地址命令
 10	0x00 0x0A	读取采集仪用户ID
 11	0x00 0x0B	写入采集仪用户ID
 12	0x00 0x0C	读取登录密码命令
 13	0x00 0x0D	写入登录密码命令
 14	0x00 0x0E	通讯协议切换命令
 15	0x00 0x0F	写继电器控制命令
 16	0x00 0x10	读继电器状态命令
 17	0x00 0x11	写继电器打开持续时间命令
 18	0x00 0x12	读继电器闭合持续时间命令
 */
public enum CommandType {
    READ_ENV_PARAM("0001"),
    WRITE_SYS_PARAM_CONFIG("0002"),
    READ_SYS_PARAM("0003"),
    READ_HISTORY_DATA("0004"),
    READ_HISTORY_DATA_AGAIN("0005"),
    MANUAL_CONTROLL("0006"),
    RESET_STATION("0007"),
    READ_STATION_ADDRESS("0008"),
    WRITE_STATION_ADDRESS("0009"),
    READ_COLLECTOR_USER_ID("000A"),
    WRITE_COLLECTOR_USER_ID("000B"),
    READ_LOGIN_PASSWORD("000C"),
    WRITE_LOGIN_PASSWORD("000D"),
    SWITCH_PROTOCOL("000E"),
    WRITE_RELAY("000F"),
    READ_RELAY("0010"),
    WRITE_RELAY_OPEN_TIME("0011"),
    READ_RELAY_OPEN_TIME("0012"),
    HEART_BEAT("0013"),
	;

    CommandType(String code) {
        this.code = code;
    }

    private String code;
    public static CommandType toEnum(String code) {
        for (CommandType commandType : CommandType.values()) {
            if (commandType.getCode().equals(code)){
                return commandType;
            }
        }
        return null;
    }

    public String getCode() {
        return code;
    }
}