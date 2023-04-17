package gov.rci.dto;

import lombok.Data;

@Data
public class UdidDto {

	private  Long pwd_application_id;
	private String udid_number;
	private String fullname;
	
	private String father_name;
	private String district_name;
}
