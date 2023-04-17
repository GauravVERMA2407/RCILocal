package gov.rci.controller;


import java.util.Map;
import java.util.SplittableRandom;
import java.util.function.Supplier;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import gov.rci.dto.RequestDto;
import gov.rci.dto.ResponseDto;
import gov.rci.model.Employee;
import gov.rci.repository.EmployeeServiceImpl;

@Controller
public class EmployeeController {
	
	@Autowired
	private RestTemplate restTemplate;
	
    @Autowired
    private EmployeeServiceImpl employeeServiceImpl;
 
    @GetMapping("/")
    public String viewHomePage(Model model) {
        model.addAttribute("allemplist", employeeServiceImpl.getAllEmployee());
        return "index";
    }
 
    @GetMapping("/addnew")
    public String addNewEmployee(Model model) {
        Employee employee = new Employee();
        model.addAttribute("employee", employee);
        
        
        
        StringBuilder generatedOTP = new StringBuilder();
        SplittableRandom splittableRandom = new SplittableRandom();
int lengthOfOTP=6;
        for (int i = 0; i < lengthOfOTP; i++) {

            int randomNumber = splittableRandom.nextInt(0, 9);
            generatedOTP.append(randomNumber);
        }
        System.out.println("OTP::::"  +generatedOTP.toString());
       // return generatedOTP.toString();
        return "newemployee";
    }
 
    @PostMapping("/save")
    public String saveEmployee(@ModelAttribute("employee") Employee employee) {
        employeeServiceImpl.save(employee);
        
        
        
        return "redirect:/";
    }
 
    @GetMapping("/showFormForUpdate/{id}")
    public String updateForm(@PathVariable(value = "id") long id, Model model) {
        Employee employee = employeeServiceImpl.getById(id);
        model.addAttribute("employee", employee);
        return "update";
    }
 
    @GetMapping("/deleteEmployee/{id}")
    public String deleteThroughId(@PathVariable(value = "id") long id) {
        employeeServiceImpl.deleteViaId(id);
        return "redirect:/";
 
    }
    
    @GetMapping("/search-udid")
    public String searchUdid(Model model) {
       
        return "udid";
    }

    
    
    

	@PostMapping(value = { "/udid-details" })

	public String getMarkingCutOutContainerISO(@ModelAttribute("requestDto") RequestDto requestDto,Model model,HttpServletRequest request, HttpSession session) {

		ResponseDto responseDto = null;
		
		requestDto.getUdid();
		
		//String result = "";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
        map.add("udid", requestDto.getUdid());

        HttpEntity<MultiValueMap<String, String>> request1= new HttpEntity<MultiValueMap<String, String>>(map, headers);

        ResponseEntity<String> response = restTemplate.postForEntity( "https://www.swavlambancard.gov.in/Api/getApplicationInformation", request1 , String.class );
        String result=response.getBody();
        
        String Post_URL=result.replaceAll(" ", "%20");
        System.out.println("Name:::"+ Post_URL);
        Supplier<Stream<String>> inputStream=()->Stream.of(Post_URL);
		inputStream.get().forEach(e->System.out.println(e));
        
		JSONParser jsonParse= new JSONParser();
		try {
			JSONObject jsonObject=(JSONObject) jsonParse.parse(result);
			@SuppressWarnings("unchecked")
			String mame= (String) jsonObject.getOrDefault("status","");
			System.out.println("mame:::"+mame);
			
			@SuppressWarnings("unchecked")
			Long multy= (Long) jsonObject.getOrDefault("multi_result","");
			System.out.println("multy:::"+multy);
			
			@SuppressWarnings("unchecked")
			Map res= (Map) jsonObject.getOrDefault("result","");
		//	String res= (String) jsonObject.getOrDefault("result","");
			System.out.println("res:::"+res);
			
			String jsonStr = JSONValue.toJSONString(res);
			
			
			
			
			  JSONParser jsonParse1= new JSONParser();
			  JSONObject jsonResult=(JSONObject) jsonParse1.parse(jsonStr);
			@SuppressWarnings("unchecked")
			String fullname= (String)jsonResult.getOrDefault("fullname","");
			String udid_number= (String)jsonResult.getOrDefault("udid_number","");
			String gender= (String)jsonResult.getOrDefault("gender","");
			String father_name= (String)jsonResult.getOrDefault("father_name","");
			String mother_name= (String)jsonResult.getOrDefault("mother_name","");
			String dob= (String)jsonResult.getOrDefault("dob","");
			String aadhaar_no= (String)jsonResult.getOrDefault("aadhaar_no","");
			
			 
			  model.addAttribute("fullname", fullname);
			  model.addAttribute("udid_number", udid_number);
			  model.addAttribute("gender", gender);
			  model.addAttribute("father_name", father_name);
			  model.addAttribute("mother_name", mother_name);
			  model.addAttribute("dob", dob);
			  model.addAttribute("aadhaar_no", aadhaar_no);
			 
			
		} catch (Exception e2) {
			// TODO: handle exception
		}
		
		
		 
      
        
        return  "udid-details" ;
	
	}
	
}
