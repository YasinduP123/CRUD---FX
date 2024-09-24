package model;

import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SubOrderDetail{

	private String itemCode;
	private Integer orderQty;

}

