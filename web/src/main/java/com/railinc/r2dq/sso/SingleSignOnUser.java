package com.railinc.r2dq.sso;

import static com.google.common.collect.Lists.newArrayList;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

public class SingleSignOnUser {

	private String id, email, country, region, city, street, postalCode, phone, employer, givenName, surname, title;
	private final Multimap<String, String> roleIdToMarks = ArrayListMultimap.create();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmployer() {
		return employer;
	}

	public void setEmployer(String employer) {
		this.employer = employer;
	}

	public String getGivenName() {
		return givenName;
	}

	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public String toString() {
		return "SingleSignOnUser [id=" + id + ", email=" + email + ", country=" + country + ", region=" + region
				+ ", city=" + city + ", street=" + street + ", postalCode=" + postalCode + ", phone=" + phone
				+ ", employer=" + employer + ", givenName=" + givenName + ", surname=" + surname + ", title=" + title
				+ "]";
	}



	public void addPermission(String roleId, Collection<String> marks) {
		List<String> m = newArrayList(marks);
		if (m.isEmpty()) {
			m.add("__ROLE__"); // used to hold on to the role itself if marks is
								// empty
		}
		this.roleIdToMarks.putAll(roleId, m);
	}

	public boolean hasPermission(String roleId, String mark) {
		return roleIdToMarks.containsEntry(roleId, mark);
	}

	public boolean hasRole(String roleId) {
		return roleIdToMarks.containsKey(roleId);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SingleSignOnUser other = (SingleSignOnUser) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public Collection<String> roleIds() {
		return Collections.unmodifiableSet(this.roleIdToMarks.keySet());
	}

	public String getFullName() {
		return String.format("%s %s", this.givenName, this.surname);
	}
}
