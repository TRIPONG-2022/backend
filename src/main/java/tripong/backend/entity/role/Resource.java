package tripong.backend.entity.role;

import lombok.*;

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

    @OneToMany(mappedBy = "resource", cascade = CascadeType.ALL)
    private List<RoleResource> roleResources = new ArrayList<>();

    //
    @Builder
    public Resource(String resourceName, ResourceType resourceType,
                    String methodName, int priorityNum, List<RoleResource> roleResources){
        this.resourceName = resourceName;
        this.resourceType = resourceType;
        this.methodName = methodName;
        this.priorityNum = priorityNum;
        for(RoleResource roleResource : roleResources){
            this.addRoleResource(roleResource);
        }
    }

    public void addRoleResource(RoleResource roleResource){
        roleResources.add(roleResource);
        roleResource.injectResource(this);
    }

}
