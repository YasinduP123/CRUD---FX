package model;


import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class GetCustomers {
    private String id;
    private String name;
    private String dob;

    public GetCustomers(String name, String dob, Double salary, String address, String city, String province, String postalCode) {
        this.name = name;
        this.dob = dob;
        this.salary = salary;
        this.address = address;
        this.city = city;
        this.province = province;
        this.postalCode = postalCode;
    }

    private Double salary;
    private String address;
    private String city;
    private String province;
    private String postalCode;

}
