package model;

import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ViewOrderDetailService {
		private String itemCode;
		private String description;
		private String packSize;
		private String unitPrice;
		private int orderQty;

}
