package tripong.backend.entity.role;


import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class RoleResource {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resource_id")
    private Resource resource;


    ///
    @Builder
    public RoleResource(Role role, Resource resource){
        this.role= role;
        this.resource = resource;
    }

    public void injectResource(Resource resource){
        this.resource = resource;
    }



}
