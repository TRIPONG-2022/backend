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

    private String description;

    private Integer priorityNum;

    @OneToMany(mappedBy = "resource", cascade = CascadeType.ALL)
    private List<RoleResource> roleResources = new ArrayList<>();

    ////-----편의 메소드-----
    //자원 생성
    public Resource(String resourceName, ResourceType resourceType,
                    String description, Integer priorityNum, List<RoleResource> roleResources){
        this.resourceName = resourceName;
        this.resourceType = resourceType;
        this.description = description;
        this.priorityNum = priorityNum;
        for(RoleResource roleResource : roleResources){
            this.addRoleResource(roleResource);
        }
    }

    //의존주입
    public void addRoleResource(RoleResource roleResource){
        roleResources.add(roleResource);
        roleResource.injectResource(this);
    }

}
