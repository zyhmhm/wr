package com.wr.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class JurisdictionVo implements Serializable {
    private static final long serialVersionUID = -5924768182135702721L;
    private String token;

}
