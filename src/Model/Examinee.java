package Model;

import java.util.List;
import java.util.Map;

public class Examinee {
	private String id;
	private String name;
	private String divisionId;
	private List<Subjects> lstSubject;
	private Integer summAll;
	private String status;
	private Map<String, Integer> mapSumEachDivision;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDivisionId() {
		return divisionId;
	}

	public void setDivisionId(String divisionId) {
		this.divisionId = divisionId;
	}

	public List<Subjects> getLstSubject() {
		return lstSubject;
	}

	public void setLstSubject(List<Subjects> lstSubject) {
		this.lstSubject = lstSubject;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getSummAll() {
		return summAll;
	}

	public void setSummAll(Integer summAll) {
		this.summAll = summAll;
	}

	public Map<String, Integer> getMapSumEachDivision() {
		return mapSumEachDivision;
	}

	public void setMapSumEachDivision(Map<String, Integer> mapSumEachDivision) {
		this.mapSumEachDivision = mapSumEachDivision;
	}

}
