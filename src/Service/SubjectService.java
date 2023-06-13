package Service;

import java.util.Map;

import Model.Divisions;
import Model.Examinee;

public interface SubjectService {
	
	Examinee checkConditionSubject(Examinee examinee,  Map<String, Divisions> mapDivision);
}
