package org.xfy.hotelpricecomparison.dto.request;

import lombok.Data;

/**
 * 供应方入住人信息请求DTO
 * 
 * @author system
 * @since 1.0.0
 */
@Data
public class JinjiangPassengerRequest {

    /**
     * 英文姓名firstName，值需为英文
     */
    private String firstName;

    /**
     * 入住人证件号
     */
    private String guestIDCard;

    /**
     * 入住人证件类型
     */
    private Integer guestIDCardType;

    /**
     * 入住人手机
     */
    private String guestMobile;

    /**
     * 入住人手机国家编码
     */
    private String guestNationCode;

    /**
     * 入住人姓名
     */
    private String guestName;

    /**
     * 英文姓名lastName，值需为英文
     */
    private String lastName;
}
