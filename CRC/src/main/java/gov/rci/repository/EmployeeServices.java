package gov.rci.repository;

import java.util.List;

import gov.rci.model.Employee;
public interface EmployeeServices {
	
	  List<Employee> getAllEmployee();
	    void save(Employee employee);
	    Employee getById(Long id);
	    void deleteViaId(long id);

}
