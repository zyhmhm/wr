package com.wr.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class UserRoleVo implements Serializable {
    private static final long serialVersionUID = -2350439404868480240L;

    private List<String> roles;
    private String avatar;
    private String introduction;
    private Long userId;
}
