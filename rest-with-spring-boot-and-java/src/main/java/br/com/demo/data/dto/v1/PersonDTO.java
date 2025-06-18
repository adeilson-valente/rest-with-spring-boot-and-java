package br.com.demo.data.dto.v1;

import com.fasterxml.jackson.annotation.*;
import com.github.dozermapper.core.Mapping;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;

@JsonPropertyOrder({"id", "firstName", "lastName", "address", "gender"})
public class PersonDTO extends RepresentationModel<PersonDTO> implements Serializable {
    private  static  final long serialVersionUID = 1L;

    @JsonProperty("id")
    @Mapping("id")
    private Long key;
    private String firstName;
    private String lastName;
    private String address;
    private String gender;

    public PersonDTO(){

    }

    public Long getKey() {
        return key;
    }

    public void setKey(Long key) {
        this.key = key;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PersonDTO personVO)) return false;
        if (!super.equals(o)) return false;

        if (getKey() != null ? !getKey().equals(personVO.getKey()) : personVO.getKey() != null) return false;
        if (getFirstName() != null ? !getFirstName().equals(personVO.getFirstName()) : personVO.getFirstName() != null)
            return false;
        if (getLastName() != null ? !getLastName().equals(personVO.getLastName()) : personVO.getLastName() != null)
            return false;
        if (getAddress() != null ? !getAddress().equals(personVO.getAddress()) : personVO.getAddress() != null)
            return false;
        return getGender() != null ? getGender().equals(personVO.getGender()) : personVO.getGender() == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getKey() != null ? getKey().hashCode() : 0);
        result = 31 * result + (getFirstName() != null ? getFirstName().hashCode() : 0);
        result = 31 * result + (getLastName() != null ? getLastName().hashCode() : 0);
        result = 31 * result + (getAddress() != null ? getAddress().hashCode() : 0);
        result = 31 * result + (getGender() != null ? getGender().hashCode() : 0);
        return result;
    }
}
