package Entity;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
public class Customer {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;
    @Basic
    @Column(name = "name")
    private String name;
    @Basic
    @Column(name = "address")
    private String address;
    @Basic
    @Column(name = "city")
    private String city;
    @Basic
    @Column(name = "postal_code")
    private String postalCode;
    @Basic
    @Column(name = "ico_value")
    private String icoValue;
    @Basic
    @Column(name = "dic_value")
    private String dicValue;

    @Basic
    @Column(name = "is_root")
    private boolean isRoot = false;

    public Customer() {
    }

    public Customer(String name, String address, String city, String postalCode, String ico, String dic) {
        this.name = name;
        this.address = address;
        this.city = city;
        this.postalCode = postalCode;
        this.icoValue = ico;
        this.dicValue = dic;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return id == customer.id && Objects.equals(name, customer.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getIco() {
        return icoValue;
    }

    public void setIco(String ico) {
        this.icoValue = ico;
    }

    public String getDic() {
        return dicValue;
    }

    public void setDic(String dic) {
        this.dicValue = dic;
    }

    public boolean isRoot() {return isRoot;}

    public void setRoot(boolean root) {isRoot = root;}
}
