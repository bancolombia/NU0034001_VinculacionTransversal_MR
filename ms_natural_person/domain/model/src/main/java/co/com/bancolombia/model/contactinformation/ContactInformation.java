package co.com.bancolombia.model.contactinformation;

import co.com.bancolombia.common.Auditing;
import co.com.bancolombia.common.annotation.ExecFieldAnnotation;
import co.com.bancolombia.common.interfaces.Mergeable;
import co.com.bancolombia.commonsvnt.model.acquisition.Acquisition;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Data
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
public class ContactInformation extends Auditing implements Mergeable {

    private UUID id;
    @ExecFieldAnnotation("ADDRESSTYPE")
    private String addressType;
    @ExecFieldAnnotation("BRAND")
    private String brand;
    @ExecFieldAnnotation("COMPANYNAME")
    private String companyName;
    @ExecFieldAnnotation("ADDRESS")
    private String address;
    @ExecFieldAnnotation("NEIGHBORHOOD")
    private String neighborhood;
    @ExecFieldAnnotation("CITY")
    private String city;
    @ExecFieldAnnotation("DEPARTMENT")
    private String department;
    @ExecFieldAnnotation("COUNTRY")
    private String country;
    @ExecFieldAnnotation("PHONE")
    private String phone;
    @ExecFieldAnnotation("EXT")
    private String ext;
    @ExecFieldAnnotation("CELLPHONE")
    private String cellphone;
    @ExecFieldAnnotation("EMAIL")
    private String email;
    private Acquisition acquisition;

    /**
     * Set the values in <strong>other</strong> not present in this.
     * This can be made using reflection. But for now, this is the implementation that works.
     *
     * @param other the other object.
     * @return this
     */
    public ContactInformation merge(ContactInformation other) {
        if (other != null) {
            this.id = this.id != null ? this.id : other.id;
            this.addressType = this.addressType != null ? this.addressType : other.addressType;
            this.brand = this.brand != null ? this.brand : other.brand;
            this.companyName = this.companyName != null ? this.companyName : other.companyName;
            this.address = this.address != null ? this.address : other.address;
            this.neighborhood = this.neighborhood != null ? this.neighborhood : other.neighborhood;
            this.city = this.city != null ? this.city : other.city;
            this.department = this.department != null ? this.department : other.department;
            this.country = this.country != null ? this.country : other.country;
            this.phone = this.phone != null ? this.phone : other.phone;
            this.ext = this.ext != null ? this.ext : other.ext;
            this.cellphone = this.cellphone != null ? this.cellphone : other.cellphone;
            this.email = this.email != null ? this.email : other.email;
            this.acquisition = this.acquisition != null ? this.acquisition : other.acquisition;
            return this;
        }
        return null;
    }
}
