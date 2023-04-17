package gov.rci.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

import javax.print.attribute.SetOfIntegerSyntax;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import gov.rci.dto.RequestDto;
import gov.rci.dto.ResponseDto;
import gov.rci.dto.UdidDto;
import gov.rci.model.Employee;

@RestController
public class UdidController {
	
	
	@Autowired
	private RestTemplate restTemplate;
	
	@GetMapping(value="/udid_data")
	public String health() {
		 String ok="tested";
		return ok;
	}
	
	
	
	
	
	
	@PostMapping(value = { "/udid-details1" })

	public String getMarkingCutOutContainerISO(@RequestBody RequestDto requestDto,Model model,HttpServletRequest request, HttpSession session) {

		ResponseDto responseDto = null;
		
		requestDto.getUdid();
		
		//String result = "";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
        map.add("udid", requestDto.getUdid());
      //  map.add("key", "21@#$!idud#$@!");
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
			  System.out.println("fullname:::"+fullname);
			  model.addAttribute("fullname", fullname);
			 
			
		} catch (Exception e2) {
			// TODO: handle exception
		}
		
		
		 
      
        
        return  "udid-details" ;
	
	}
	
	
	

}
