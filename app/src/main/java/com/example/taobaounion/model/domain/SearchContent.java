package com.example.taobaounion.model.domain;

import java.util.List;

public class SearchContent {


    private Boolean success;
    private Long code;
    private String message;
    private DataDTO data;

    @Override
    public String toString() {
        return "SearchContent{" +
                "success=" + success +
                ", code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

    public Boolean isSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataDTO getData() {
        return data;
    }

    public void setData(DataDTO data) {
        this.data = data;
    }

    public static class DataDTO {
        private TbkDgMaterialOptionalResponseDTO tbk_dg_material_optional_response;

        public TbkDgMaterialOptionalResponseDTO getTbk_dg_material_optional_response() {
            return tbk_dg_material_optional_response;
        }

        public void setTbk_dg_material_optional_response(TbkDgMaterialOptionalResponseDTO tbk_dg_material_optional_response) {
            this.tbk_dg_material_optional_response = tbk_dg_material_optional_response;
        }

        @Override
        public String toString() {
            return "DataDTO{" +
                    "tbk_dg_material_optional_response=" + tbk_dg_material_optional_response +
                    '}';
        }

        public static class TbkDgMaterialOptionalResponseDTO {
            private ResultListDTO result_list;
            private Long total_results;
            private String request_id;

            public ResultListDTO getResult_list() {
                return result_list;
            }

            public void setResult_list(ResultListDTO result_list) {
                this.result_list = result_list;
            }

            public Long getTotal_results() {
                return total_results;
            }

            public void setTotal_results(Long total_results) {
                this.total_results = total_results;
            }

            public String getRequest_id() {
                return request_id;
            }

            public void setRequest_id(String request_id) {
                this.request_id = request_id;
            }

            @Override
            public String toString() {
                return "TbkDgMaterialOptionalResponseDTO{" +
                        "result_list=" + result_list +
                        ", total_results=" + total_results +
                        ", request_id='" + request_id + '\'' +
                        '}';
            }

            public static class ResultListDTO {
                private List<MapDataDTO> map_data;

                public List<MapDataDTO> getMap_data() {
                    return map_data;
                }

                public void setMap_data(List<MapDataDTO> map_data) {
                    this.map_data = map_data;
                }

                @Override
                public String toString() {
                    return "ResultListDTO{" +
                            "map_data=" + map_data +
                            '}';
                }

                public static class MapDataDTO implements ILinearItemInfo{
                    private Long category_id;
                    private String category_name;
                    private String commission_rate;
                    private String commission_type;
                    private Long coupon_amount;
                    private String coupon_end_time;
                    private String coupon_id;
                    private String coupon_info;
                    private Long coupon_remain_count;
                    private String coupon_share_url;
                    private String coupon_start_fee;
                    private String coupon_start_time;
                    private Long coupon_total_count;
                    private String include_dxjh;
                    private String include_mkt;
                    private String info_dxjh;
                    private String item_description;
                    private Long item_id;
                    private String item_url;
                    private Long level_one_category_id;
                    private String level_one_category_name;
                    private String nick;
                    private Long num_iid;
                    private String pict_url;
                    private String presale_deposit;
                    private Long presale_end_time;
                    private Long presale_start_time;
                    private Long presale_tail_end_time;
                    private Long presale_tail_start_time;
                    private String provcity;
                    private String real_post_fee;
                    private String reserve_price;
                    private Long seller_id;
                    private Long shop_dsr;
                    private String shop_title;
                    private String short_title;
                    private SmallImagesDTO small_images;
                    private String title;
                    private String tk_total_commi;
                    private String tk_total_sales;
                    private String url;
                    private Long user_type;
                    private Long volume;
                    private String white_image;
                    private String x_id;
                    private String zk_final_price;
                    private Long jdd_num;
                    private Object jdd_price;
                    private Object oetime;
                    private Object ostime;

                    @Override
                    public String toString() {
                        return "MapDataDTO{" +
                                "category_id=" + category_id +
                                ", category_name='" + category_name + '\'' +
                                ", commission_rate='" + commission_rate + '\'' +
                                ", commission_type='" + commission_type + '\'' +
                                ", coupon_amount='" + coupon_amount + '\'' +
                                ", coupon_end_time='" + coupon_end_time + '\'' +
                                ", coupon_id='" + coupon_id + '\'' +
                                ", coupon_info='" + coupon_info + '\'' +
                                ", coupon_remain_count=" + coupon_remain_count +
                                ", coupon_share_url='" + coupon_share_url + '\'' +
                                ", coupon_start_fee='" + coupon_start_fee + '\'' +
                                ", coupon_start_time='" + coupon_start_time + '\'' +
                                ", coupon_total_count=" + coupon_total_count +
                                ", include_dxjh='" + include_dxjh + '\'' +
                                ", include_mkt='" + include_mkt + '\'' +
                                ", info_dxjh='" + info_dxjh + '\'' +
                                ", item_description='" + item_description + '\'' +
                                ", item_id=" + item_id +
                                ", item_url='" + item_url + '\'' +
                                ", level_one_category_id=" + level_one_category_id +
                                ", level_one_category_name='" + level_one_category_name + '\'' +
                                ", nick='" + nick + '\'' +
                                ", num_iid=" + num_iid +
                                ", pict_url='" + pict_url + '\'' +
                                ", presale_deposit='" + presale_deposit + '\'' +
                                ", presale_end_time=" + presale_end_time +
                                ", presale_start_time=" + presale_start_time +
                                ", presale_tail_end_time=" + presale_tail_end_time +
                                ", presale_tail_start_time=" + presale_tail_start_time +
                                ", provcity='" + provcity + '\'' +
                                ", real_post_fee='" + real_post_fee + '\'' +
                                ", reserve_price='" + reserve_price + '\'' +
                                ", seller_id=" + seller_id +
                                ", shop_dsr=" + shop_dsr +
                                ", shop_title='" + shop_title + '\'' +
                                ", short_title='" + short_title + '\'' +
                                ", small_images=" + small_images +
                                ", title='" + title + '\'' +
                                ", tk_total_commi='" + tk_total_commi + '\'' +
                                ", tk_total_sales='" + tk_total_sales + '\'' +
                                ", url='" + url + '\'' +
                                ", user_type=" + user_type +
                                ", volume=" + volume +
                                ", white_image='" + white_image + '\'' +
                                ", x_id='" + x_id + '\'' +
                                ", zk_final_price='" + zk_final_price + '\'' +
                                ", jdd_num=" + jdd_num +
                                ", jdd_price=" + jdd_price +
                                ", oetime=" + oetime +
                                ", ostime=" + ostime +
                                '}';
                    }

                    public Long getCategory_id() {
                        return category_id;
                    }

                    public void setCategory_id(Long category_id) {
                        this.category_id = category_id;
                    }

                    public String getCategory_name() {
                        return category_name;
                    }

                    public void setCategory_name(String category_name) {
                        this.category_name = category_name;
                    }

                    public String getCommission_rate() {
                        return commission_rate;
                    }

                    public void setCommission_rate(String commission_rate) {
                        this.commission_rate = commission_rate;
                    }

                    public String getCommission_type() {
                        return commission_type;
                    }

                    public void setCommission_type(String commission_type) {
                        this.commission_type = commission_type;
                    }

                    public Long getCoupon_amount() {
                        return coupon_amount;
                    }

                    public void setCoupon_amount(Long coupon_amount) {
                        this.coupon_amount = coupon_amount;
                    }

                    public String getCoupon_end_time() {
                        return coupon_end_time;
                    }

                    public void setCoupon_end_time(String coupon_end_time) {
                        this.coupon_end_time = coupon_end_time;
                    }

                    public String getCoupon_id() {
                        return coupon_id;
                    }

                    public void setCoupon_id(String coupon_id) {
                        this.coupon_id = coupon_id;
                    }

                    public String getCoupon_info() {
                        return coupon_info;
                    }

                    public void setCoupon_info(String coupon_info) {
                        this.coupon_info = coupon_info;
                    }

                    public Long getCoupon_remain_count() {
                        return coupon_remain_count;
                    }

                    public void setCoupon_remain_count(Long coupon_remain_count) {
                        this.coupon_remain_count = coupon_remain_count;
                    }

                    public String getCoupon_share_url() {
                        return coupon_share_url;
                    }

                    public void setCoupon_share_url(String coupon_share_url) {
                        this.coupon_share_url = coupon_share_url;
                    }

                    public String getCoupon_start_fee() {
                        return coupon_start_fee;
                    }

                    public void setCoupon_start_fee(String coupon_start_fee) {
                        this.coupon_start_fee = coupon_start_fee;
                    }

                    public String getCoupon_start_time() {
                        return coupon_start_time;
                    }

                    public void setCoupon_start_time(String coupon_start_time) {
                        this.coupon_start_time = coupon_start_time;
                    }

                    public Long getCoupon_total_count() {
                        return coupon_total_count;
                    }

                    public void setCoupon_total_count(Long coupon_total_count) {
                        this.coupon_total_count = coupon_total_count;
                    }

                    public String getInclude_dxjh() {
                        return include_dxjh;
                    }

                    public void setInclude_dxjh(String include_dxjh) {
                        this.include_dxjh = include_dxjh;
                    }

                    public String getInclude_mkt() {
                        return include_mkt;
                    }

                    public void setInclude_mkt(String include_mkt) {
                        this.include_mkt = include_mkt;
                    }

                    public String getInfo_dxjh() {
                        return info_dxjh;
                    }

                    public void setInfo_dxjh(String info_dxjh) {
                        this.info_dxjh = info_dxjh;
                    }

                    public String getItem_description() {
                        return item_description;
                    }

                    public void setItem_description(String item_description) {
                        this.item_description = item_description;
                    }

                    public Long getItem_id() {
                        return item_id;
                    }

                    public void setItem_id(Long item_id) {
                        this.item_id = item_id;
                    }

                    public String getItem_url() {
                        return item_url;
                    }

                    public void setItem_url(String item_url) {
                        this.item_url = item_url;
                    }

                    public Long getLevel_one_category_id() {
                        return level_one_category_id;
                    }

                    public void setLevel_one_category_id(Long level_one_category_id) {
                        this.level_one_category_id = level_one_category_id;
                    }

                    public String getLevel_one_category_name() {
                        return level_one_category_name;
                    }

                    public void setLevel_one_category_name(String level_one_category_name) {
                        this.level_one_category_name = level_one_category_name;
                    }

                    public String getNick() {
                        return nick;
                    }

                    public void setNick(String nick) {
                        this.nick = nick;
                    }

                    public Long getNum_iid() {
                        return num_iid;
                    }

                    public void setNum_iid(Long num_iid) {
                        this.num_iid = num_iid;
                    }

                    public String getPict_url() {
                        return pict_url;
                    }

                    public void setPict_url(String pict_url) {
                        this.pict_url = pict_url;
                    }

                    public String getPresale_deposit() {
                        return presale_deposit;
                    }

                    public void setPresale_deposit(String presale_deposit) {
                        this.presale_deposit = presale_deposit;
                    }

                    public Long getPresale_end_time() {
                        return presale_end_time;
                    }

                    public void setPresale_end_time(Long presale_end_time) {
                        this.presale_end_time = presale_end_time;
                    }

                    public Long getPresale_start_time() {
                        return presale_start_time;
                    }

                    public void setPresale_start_time(Long presale_start_time) {
                        this.presale_start_time = presale_start_time;
                    }

                    public Long getPresale_tail_end_time() {
                        return presale_tail_end_time;
                    }

                    public void setPresale_tail_end_time(Long presale_tail_end_time) {
                        this.presale_tail_end_time = presale_tail_end_time;
                    }

                    public Long getPresale_tail_start_time() {
                        return presale_tail_start_time;
                    }

                    public void setPresale_tail_start_time(Long presale_tail_start_time) {
                        this.presale_tail_start_time = presale_tail_start_time;
                    }

                    public String getProvcity() {
                        return provcity;
                    }

                    public void setProvcity(String provcity) {
                        this.provcity = provcity;
                    }

                    public String getReal_post_fee() {
                        return real_post_fee;
                    }

                    public void setReal_post_fee(String real_post_fee) {
                        this.real_post_fee = real_post_fee;
                    }

                    public String getReserve_price() {
                        return reserve_price;
                    }

                    public void setReserve_price(String reserve_price) {
                        this.reserve_price = reserve_price;
                    }

                    public Long getSeller_id() {
                        return seller_id;
                    }

                    public void setSeller_id(Long seller_id) {
                        this.seller_id = seller_id;
                    }

                    public Long getShop_dsr() {
                        return shop_dsr;
                    }

                    public void setShop_dsr(Long shop_dsr) {
                        this.shop_dsr = shop_dsr;
                    }

                    public String getShop_title() {
                        return shop_title;
                    }

                    public void setShop_title(String shop_title) {
                        this.shop_title = shop_title;
                    }

                    public String getShort_title() {
                        return short_title;
                    }

                    public void setShort_title(String short_title) {
                        this.short_title = short_title;
                    }

                    public SmallImagesDTO getSmall_images() {
                        return small_images;
                    }

                    public void setSmall_images(SmallImagesDTO small_images) {
                        this.small_images = small_images;
                    }

                    @Override
                    public String getCover() {
                        return getPict_url();
                    }

                    public String getTitle() {
                        return title;
                    }

                    public void setTitle(String title) {
                        this.title = title;
                    }

                    public String getTk_total_commi() {
                        return tk_total_commi;
                    }

                    public void setTk_total_commi(String tk_total_commi) {
                        this.tk_total_commi = tk_total_commi;
                    }

                    public String getTk_total_sales() {
                        return tk_total_sales;
                    }

                    public void setTk_total_sales(String tk_total_sales) {
                        this.tk_total_sales = tk_total_sales;
                    }

                    public String getUrl() {
                        return url;
                    }

                    public void setUrl(String url) {
                        this.url = url;
                    }

                    public Long getUser_type() {
                        return user_type;
                    }

                    public void setUser_type(Long user_type) {
                        this.user_type = user_type;
                    }

                    @Override
                    public String getFinalPrice() {
                        return getZk_final_price();
                    }

                    @Override
                    public Long getCouponAmount() {
                        return coupon_amount;
                    }

                    public long getVolume() {
                        return volume;
                    }

                    public void setVolume(Long volume) {
                        this.volume = volume;
                    }

                    public String getWhite_image() {
                        return white_image;
                    }

                    public void setWhite_image(String white_image) {
                        this.white_image = white_image;
                    }

                    public String getX_id() {
                        return x_id;
                    }

                    public void setX_id(String x_id) {
                        this.x_id = x_id;
                    }

                    public String getZk_final_price() {
                        return zk_final_price;
                    }

                    public void setZk_final_price(String zk_final_price) {
                        this.zk_final_price = zk_final_price;
                    }

                    public Long getJdd_num() {
                        return jdd_num;
                    }

                    public void setJdd_num(Long jdd_num) {
                        this.jdd_num = jdd_num;
                    }

                    public Object getJdd_price() {
                        return jdd_price;
                    }

                    public void setJdd_price(Object jdd_price) {
                        this.jdd_price = jdd_price;
                    }

                    public Object getOetime() {
                        return oetime;
                    }

                    public void setOetime(Object oetime) {
                        this.oetime = oetime;
                    }

                    public Object getOstime() {
                        return ostime;
                    }

                    public void setOstime(Object ostime) {
                        this.ostime = ostime;
                    }

                    public static class SmallImagesDTO {
                        @Override
                        public String toString() {
                            return "SmallImagesDTO{" +
                                    "string=" + string +
                                    '}';
                        }

                        private List<String> string;

                        public List<String> getString() {
                            return string;
                        }

                        public void setString(List<String> string) {
                            this.string = string;
                        }
                    }
                }
            }
        }
    }
}
