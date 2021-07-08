package com.ouertani.safetyalertV2.dto;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class ListPersonsDetailsListStatioNumber {
	private String adress;
	private List<PersonsDetailsListStatioNumber> listPersonsDetailsListStatioNumber;

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ListPersonsDetailsListStatioNumber other = (ListPersonsDetailsListStatioNumber) obj;
		if (adress == null) {
			if (other.adress != null)
				return false;
		} else if (!adress.equals(other.adress))
			return false;
		if (listPersonsDetailsListStatioNumber == null) {
			if (other.listPersonsDetailsListStatioNumber != null)
				return false;
		} else if (!listPersonsDetailsListStatioNumber.equals(other.listPersonsDetailsListStatioNumber))
			return false;
		return true;
	}

}