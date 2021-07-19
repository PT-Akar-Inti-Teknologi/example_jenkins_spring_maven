package com.akarinti.preapproved.dto;

import com.akarinti.preapproved.utils.exception.StatusCode;
import lombok.Data;
import org.springframework.data.domain.Page;

@Data
public class ResultPageDTO extends ResultDTO {

    private Integer pages;

    private Long elements;

    public ResultPageDTO(Page page, Object result) {
        this.code = "BO-"+ StatusCode.OK.code();
        this.message = StatusCode.OK.message();
        this.pages = page.getTotalPages();
        this.elements = page.getTotalElements();
        this.result = result;
    }

}