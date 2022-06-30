package tripong.backend.entity.role;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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

    @Enumerated(EnumType.STRING)
    private ResourceType resourceType;

    private String methodName;

    private int priorityNum;

    @OneToMany(mappedBy = "resource")
    private List<RoleResource> roleResources = new ArrayList<>();

}
