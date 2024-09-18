package model;

import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    private String itemCode;
    private String description;
    private String packSize;
    private String unitPrice;
    private Integer qtyOnHand;
}
