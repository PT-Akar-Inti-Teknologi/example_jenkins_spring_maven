package com.akarinti.preapproved.dto;

import com.akarinti.preapproved.utils.exception.StatusCode;
import lombok.Data;
import org.springframework.data.domain.Page;

@Data
public class MetaPaginationDTO extends ResultDTO {

    private Integer pages;

    private Long elements;

    public MetaPaginationDTO(Page page) {
        this.pages = page.getTotalPages();
        this.elements = page.getTotalElements();
    }

}