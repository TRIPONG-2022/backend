package tripong.backend.dto.account;

import lombok.Data;
import java.util.List;

@Data
public class AddressResponseDto {

    private String city;
    private List<String> district;

    public AddressResponseDto(String city, List<String> district){
        this.city = city;
        this.district = district;
    }
}
