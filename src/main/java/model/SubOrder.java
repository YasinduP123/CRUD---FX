package model;

import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SubOrder {
	private String orderId;
	private LocalDate orderDate;
	private String custId;
}
