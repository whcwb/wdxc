package com.ldz.util.bean;

import lombok.ToString;

@ToString
public class AddPointResponse {

    private int status;

    private String message;

    private int success_num;

    private  FailInfo fail_info;




    @ToString
    public static  class  FailInfo{

        private TrackPointForReturn[] param_error;

        private TrackPointForReturn[] internal_error;

        public TrackPointForReturn[] getParam_error() {
            return param_error;
        }

        public void setParam_error(TrackPointForReturn[] param_error) {
            this.param_error = param_error;
        }

        public TrackPointForReturn[] getInternal_error() {
            return internal_error;
        }

        public void setInternal_error(TrackPointForReturn[] internal_error) {
            this.internal_error = internal_error;
        }
    }


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getSuccess_num() {
        return success_num;
    }

    public void setSuccess_num(int success_num) {
        this.success_num = success_num;
    }

    public FailInfo getFail_info() {
        return fail_info;
    }

    public void setFail_info(FailInfo fail_info) {
        this.fail_info = fail_info;
    }
}
