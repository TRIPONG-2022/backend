package tripong.backend.entity.role;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Resource {

    @Id
    @GeneratedValue
    private Long id;

    private String resourceName;

    private String resourceType;

    private String methodName;

    private int orderNum;

    @OneToMany(mappedBy = "resource")
    private List<RoleResource> roleResources = new ArrayList<>();

}
