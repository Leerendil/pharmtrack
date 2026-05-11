package org.vsu.orderservice.utils.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class CommonConstants {
    public static final String API_V1_ORDERS = "/api/v1/orders";
    public static final String API_V1_CARTS = "/api/v1/carts";

    public static final String SELECTED = "/selected";
    public static final String MEDICINE_ID = "/{medicineId}";

    public static final String KEYCLOAK_CLIENT_NAME = "pharmtrack-backend-client";
}
