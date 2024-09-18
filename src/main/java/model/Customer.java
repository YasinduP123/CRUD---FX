package model;


import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
	private String id;
	private String title;
	private String name;
	private LocalDate dob;
	private Double salary;
	private String address;
	private String city;
	private String province;
	private String postalCode;

}
