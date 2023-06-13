package ServiceImp;

import java.util.HashMap;
import java.util.Map;

import Common.Constants;
import Model.Divisions;
import Model.Examinee;
import Model.Subjects;
import Service.SubjectService;

public class SubjectImp implements SubjectService{

	@Override
	public Examinee checkConditionSubject(Examinee examine,  Map<String, Divisions> mapDivision) {
		try {
			if(examine.getLstSubject() != null) {
				Divisions divisions = mapDivision.get(examine.getDivisionId());
				Map<String, Integer> mapSumEachDivision = new HashMap<>();
				int sumDivisionOfExminee = 0;
				int sumAllSubject = 0;
				for(Subjects sb : examine.getLstSubject()) {	
					
					Integer sumDivision = mapSumEachDivision.get(sb.getDivisionId());
					if(sumDivision != null) {
						sumDivision += sb.getScore();
						mapSumEachDivision.put(sb.getDivisionId(), sumDivision);
					}else {
						sumDivision = sb.getScore();
						mapSumEachDivision.put(sb.getDivisionId(), sumDivision);
					}
					
					if(sb.getDivisionId().equals(examine.getDivisionId())){
						sumDivisionOfExminee += sb.getScore();
					}
					sumAllSubject += sb.getScore();				
				}
				examine.setSummAll(sumAllSubject);
				examine.setMapSumEachDivision(mapSumEachDivision);
				if(sumDivisionOfExminee >=  divisions.getSumConditionDivision() && sumAllSubject >= Constants.CONDITION_SUM_ALL_SUBJECT) {
					examine.setStatus(Constants.STRING_PASS);
				}else {
					examine.setStatus(Constants.STRING_FAIL);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return examine;
	}

	
}
