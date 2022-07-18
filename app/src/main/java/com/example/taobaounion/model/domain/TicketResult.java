package com.example.taobaounion.model.domain;

import java.io.Serializable;

public class TicketResult implements Serializable {


    private Boolean success;
    private Integer code;
    private String message;
    private DataDTOX data;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataDTOX getData() {
        return data;
    }

    public void setData(DataDTOX data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "TicketResult{" +
                "success=" + success +
                ", code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

    public static class DataDTOX implements Serializable {
        @Override
        public String toString() {
            return "DataDTOX{" +
                    "tbk_tpwd_create_response=" + tbk_tpwd_create_response +
                    '}';
        }

        private TbkTpwdCreateResponseDTO tbk_tpwd_create_response;

        public TbkTpwdCreateResponseDTO getTbk_tpwd_create_response() {
            return tbk_tpwd_create_response;
        }

        public void setTbk_tpwd_create_response(TbkTpwdCreateResponseDTO tbk_tpwd_create_response) {
            this.tbk_tpwd_create_response = tbk_tpwd_create_response;
        }

        public static class TbkTpwdCreateResponseDTO implements Serializable {
            private DataDTO data;
            private String request_id;

            @Override
            public String toString() {
                return "TbkTpwdCreateResponseDTO{" +
                        "data=" + data +
                        ", request_id='" + request_id + '\'' +
                        '}';
            }

            public DataDTO getData() {
                return data;
            }

            public void setData(DataDTO data) {
                this.data = data;
            }

            public String getRequest_id() {
                return request_id;
            }

            public void setRequest_id(String request_id) {
                this.request_id = request_id;
            }

            public static class DataDTO implements Serializable {
                private String model;

                public String getModel() {
                    return model;
                }

                public void setModel(String model) {
                    this.model = model;
                }

                @Override
                public String toString() {
                    return "DataDTO{" +
                            "model='" + model + '\'' +
                            '}';
                }
            }
        }
    }
}
