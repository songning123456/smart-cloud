package com.scframework.smartclouddemo.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author sonin
 * @date 2021/8/27 8:20
 */
@Data
@ApiModel(value="IpPort对象", description="IpPort对象")
public class IpPort {

    @ApiModelProperty(value = "Ip地址")
    private String ip;

    @ApiModelProperty(value = "Port端口号")
    private String port;

}
