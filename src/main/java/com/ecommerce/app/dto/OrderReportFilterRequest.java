package com.ecommerce.app.dto;

import java.util.Date;

public record OrderReportFilterRequest(
        Date from,
        Date to
) {
}
