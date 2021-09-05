package com.scframework.smartcloudoauth2.vo;

import lombok.*;

/**
 * @author sonin
 * @date 2021/9/5 15:58
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SaTokenInfoVO {

    private String tokenName;
    private String tokenValue;

}
